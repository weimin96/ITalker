package com.aoliao.example.factory.model.api.group;

import java.util.HashSet;
import java.util.Set;

/**
 * @author 你的奥利奥
 * @version 2017/8/24
 */

public class GroupMemberAddModel {
    private Set<String> users = new HashSet<>();

    public GroupMemberAddModel(Set<String> users) {
        this.users = users;
    }

    public Set<String> getUsers() {
        return users;
    }

    public void setUsers(Set<String> users) {
        this.users = users;
    }
}
