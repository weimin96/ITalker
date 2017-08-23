package com.aoliao.example.factory.data.user;

import com.aoliao.example.factory.data.BaseDbRepository;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.model.db.User_Table;
import com.aoliao.example.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/21
 */

public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {
    @Override
    public void load(DataSource.SucceedCallback<List<User>> callback) {
        super.load(callback);

        // 加载本地数据库数据
        SQLite.select()
                .from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equals(Account.getUserId());
    }
}
