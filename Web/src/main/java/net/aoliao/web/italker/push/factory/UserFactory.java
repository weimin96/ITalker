package net.aoliao.web.italker.push.factory;

import com.google.common.base.Strings;

import net.aoliao.web.italker.push.bean.db.User;
import net.aoliao.web.italker.push.utils.Hib;
import net.aoliao.web.italker.push.utils.TextUtil;

import java.util.List;
import java.util.UUID;


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
            if (Strings.isNullOrEmpty(user.getPushId())) {
                //TODO 推送一条退出消息
            }
            //更新设备id
            user.setPushId(pushId);
            return Hib.query(session -> {
                session.saveOrUpdate(user);
                return user;
            });
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
        //出去账户首尾空格
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
        return Hib.query(session -> {
            session.saveOrUpdate(user);
            return user;
        });
    }

    public static User createUser(String account, String name, String password) {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setPhone(account);
        //数据库存储
        return Hib.query(session -> {
            session.save(user);
            return user;
        });
    }

    private static String encodePassword(String password) {
        //去除首尾空格
        password = password.trim();
        //进行md5加密
        password = TextUtil.getMD5(password);
        //再进行一次base64加密
        return TextUtil.encodeBase64(password);
    }
}
