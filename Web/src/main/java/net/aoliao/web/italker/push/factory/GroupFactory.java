package net.aoliao.web.italker.push.factory;

import net.aoliao.web.italker.push.bean.db.Group;
import net.aoliao.web.italker.push.bean.db.GroupMember;
import net.aoliao.web.italker.push.bean.db.User;

import java.util.Set;

/**
 * Created by 你的奥利奥
 * on 2017/8/22
 */
public class GroupFactory {
    public static Group findById(String id) {
        return null;
    }

    public static Group findById(User sender,String id) {
        return null;
    }

    public static Set<GroupMember> getMembers(Group group) {
        return null;
    }
}
