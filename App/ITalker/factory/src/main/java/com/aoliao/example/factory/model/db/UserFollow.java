package com.aoliao.example.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;

/**
 * @author 你的奥利奥
 * @version 2017/8/28
 */

@Table(database = AppDatabase.class)
public class UserFollow extends BaseDbModel<UserFollow> {
    @PrimaryKey
    private String id;
    @ForeignKey(tableClass = User.class, stubbedRelationship = true)
    private User origin;
    @ForeignKey(tableClass = User.class, stubbedRelationship = true)
    private User target;

    public UserFollow() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getOrigin() {
        return origin;
    }

    public void setOrigin(User origin) {
        this.origin = origin;
    }

    public User getTarget() {
        return target;
    }

    public void setTarget(User target) {
        this.target = target;
    }

    @Override
    public boolean isSame(UserFollow old) {
        return false;
    }

    @Override
    public boolean isUiContentSame(UserFollow old) {
        return false;
    }
}
