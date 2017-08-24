package net.aoliao.web.italker.push.factory;

import com.google.common.base.Strings;

import net.aoliao.web.italker.push.bean.db.User;
import net.aoliao.web.italker.push.bean.db.UserFollow;
import net.aoliao.web.italker.push.utils.Hib;
import net.aoliao.web.italker.push.utils.TextUtil;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * Created by 你的奥利奥
 * on 2017/8/15
 */
public class UserFactory {

    public static User findByToken(String token) {
        //只能用户自己使用
        //通过token查用户
        return Hib.query(session -> (User) session.createQuery("from User where token=:token")
                .setParameter("token", token)
                .uniqueResult());
    }

    public static User findByPhone(String phone) {
        return Hib.query(session -> (User) session.createQuery("from User where phone=:inPhone")
                .setParameter("inPhone", phone)
                .uniqueResult());
    }

    public static User findByName(String name) {
        return Hib.query(session -> (User) session.createQuery("from User where name=:name")
                .setParameter("name", name)
                .uniqueResult());
    }

    public static User findById(String id) {
        return Hib.query(session -> session.get(User.class, id));
    }

    //更新用户信息
    public static User update(User user) {
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    /**
     * 给当前用户绑定设备id
     *
     * @param user   user
     * @param pushId pushId
     * @return User
     */
    public static User bindPushId(User user, String pushId) {
        if (Strings.isNullOrEmpty(pushId)) {
            return null;
        }
        //查询是否有其他用户绑定了该设备
        //取消绑定，查询列表不包括自己
        Hib.queryOnly(session -> {
            @SuppressWarnings("unchecked")
            List<User> userList = (List<User>) session.createQuery("from User where lower(pushId)=:pushId and id!=:userId")
                    .setParameter("pushId", pushId.toLowerCase())
                    .setParameter("userId", user.getId())
                    .list();
            for (User u : userList) {
                u.setPushId(null);
                session.saveOrUpdate(u);
            }
        });

        if (pushId.equalsIgnoreCase(user.getPushId())) {
            return user;
        } else {
            //给之前设备推送一条退出消息
            if (!Strings.isNullOrEmpty(user.getPushId())) {
                //推送一条退出消息
                PushFactory.pushLogout(user,user.getPushId());
            }
            //更新设备id
            user.setPushId(pushId);
            return update(user);
        }
    }

    //登陆，判断用户密码是否匹配
    public static User login(String account, String password) {
        final String accountStr = account.trim();
        final String encodePassword = encodePassword(password);
        User user = Hib.query(session -> (User) session.createQuery
                ("from User where phone=:phone and password=:password")
                .setParameter("phone", accountStr)
                .setParameter("password", encodePassword)
                .uniqueResult());
        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /**
     * 用户注册
     * 写入数据库，并返回user
     *
     * @param account  account
     * @param name     name
     * @param password password
     * @return User
     */
    public static User register(String account, String name, String password) {
        //去除账户首尾空格
        account = account.trim();
        //加密
        password = encodePassword(password);

        User user = createUser(account, name, password);
        if (user != null) {
            user = login(user);
        }
        return user;
    }

    /**
     * 登陆
     * 对token进行操作
     *
     * @param user user
     * @return User
     */
    private static User login(User user) {
        //使用一个随机的uuid作为token
        String newToken = UUID.randomUUID().toString();
        newToken = TextUtil.encodeBase64(newToken);
        user.setToken(newToken);
        return update(user);
    }

    public static User createUser(String account, String name, String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhone(account);
        //数据库存储
        return update(user);
    }

    private static String encodePassword(String password) {
        //去除首尾空格
        password = password.trim();
        //进行md5加密
        password = TextUtil.getMD5(password);
        //再进行一次base64加密
        return TextUtil.encodeBase64(password);
    }

    /**
     * 获取我的联系人列表
     *
     * @param self user
     * @return List<User>
     */
    public static List<User> contacts(User self) {
        return Hib.query(session -> {
            //重新加载一次用户信息到self中，和当前的session绑定
            session.load(self, self.getId());
            //获取我关注的人
            Set<UserFollow> flows = self.getFollowing();

            return flows.stream()
                    .map(UserFollow::getTarget)
                    .collect(Collectors.toList());
        });
    }

    /**
     * 关注人的操作
     * @param origin 发起者
     * @param target 被关注者
     * @param alias 备注
     * @return 被关注者
     */
    public static User follow(final User origin,final User target,final String alias){
        UserFollow follow=getUserFollow(origin,target);
        if (follow!=null){
            //已关注，直接返回
            return follow.getTarget();
        }
        return Hib.query(session -> {
            //想要重新操作懒加载的数据，需要重新load一次
            session.load(origin,origin.getId());
            session.load(target,target.getId());

            //我关注人的时候，同时他也关注我
            //所以需要添加两次
            UserFollow originFollow=new UserFollow();
            originFollow.setTarget(target);
            originFollow.setOrigin(origin);
            originFollow.setAlias(alias);

            UserFollow targetFollow=new UserFollow();
            targetFollow.setTarget(origin);
            targetFollow.setOrigin(target);

            session.save(originFollow);
            session.save(targetFollow);
            return target;
        });
    }

    /**
     * 查询两人是否已经关注
     * @param origin 发起者
     * @param target 被关注者
     * @return UserFollow
     */
    public static UserFollow getUserFollow(final User origin,final User target){
        return Hib.query(session -> (UserFollow) session.createQuery("from UserFollow where originId=:originId and targetId=:targetId")
                .setParameter("originId",origin.getId())
                .setParameter("targetId",target.getId())
                .setMaxResults(1)
                .uniqueResult());
    }

    /**
     * 搜索联系人的实现
     *
     * @param name 查询的name，允许为空
     * @return 查询到的用户集合，如果name为空，则返回最近的用户
     */
    @SuppressWarnings("unchecked")
    public static List<User> search(String name) {
        if (Strings.isNullOrEmpty(name))
            name = ""; // 保证不能为null的情况，减少后面的一下判断和额外的错误
        final String searchName = "%" + name + "%"; // 模糊匹配

        return Hib.query(session -> {
            // 查询的条件：name忽略大小写，并且使用like（模糊）查询；
            // 头像和描述必须完善才能查询到
            return (List<User>) session.createQuery("from User where lower(name) like :name and portrait is not null and description is not null")
                    .setParameter("name", searchName)
                    .setMaxResults(20) // 至多20条
                    .list();

        });

    }
}
