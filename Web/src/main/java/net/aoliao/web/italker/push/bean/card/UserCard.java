package net.aoliao.web.italker.push.bean.card;

import com.google.gson.annotations.Expose;

import net.aoliao.web.italker.push.bean.db.User;
import net.aoliao.web.italker.push.utils.Hib;

import java.time.LocalDateTime;

/**
 * Created by 你的奥利奥
 * on 2017/8/15
 */
public class UserCard {
    @Expose
    private String id;

    @Expose
    private String name;

    @Expose
    private String phone;

    @Expose
    private String portrait;

    @Expose
    private String desc;

    @Expose
    private int sex = 0;

    //关注数量
    @Expose
    private int follows;

    //粉丝数量
    @Expose
    private int following;

    //我与user当前的关系状态，是否已经关注了这个人
    @Expose
    private boolean isFollow;

    @Expose
    private LocalDateTime modifyAt;

    public String getId() {
        return id;
    }

    public UserCard(final User user) {
        this(user, false);
    }

    public UserCard(final User user, boolean isFollow) {
        this.isFollow = isFollow;

        this.id = user.getId();
        this.name = user.getName();
        this.phone = user.getPhone();
        this.portrait = user.getPortrait();
        this.desc = user.getDescription();
        this.sex = user.getSex();
        this.modifyAt = user.getUpdateAt();

        // user.getFollowers().size()
        // 懒加载会报错，因为没有Session
        Hib.queryOnly(session -> {
            session.load(user,user.getId());
            follows=user.getFollowers().size();
            following=user.getFollowing().size();
        });
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getFollows() {
        return follows;
    }

    public void setFollows(int follows) {
        this.follows = follows;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public boolean isFollow() {
        return isFollow;
    }

    public void setFollow(boolean follow) {
        isFollow = follow;
    }

    public LocalDateTime getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(LocalDateTime modifyAt) {
        this.modifyAt = modifyAt;
    }
}
