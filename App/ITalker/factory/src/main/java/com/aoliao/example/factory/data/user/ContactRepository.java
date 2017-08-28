package com.aoliao.example.factory.data.user;

import com.aoliao.example.factory.data.BaseDbRepository;
import com.aoliao.example.factory.data.DataSource;
import com.aoliao.example.factory.model.db.User;
import com.aoliao.example.factory.model.db.UserFollow;
import com.aoliao.example.factory.model.db.UserFollow_Table;
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
        // 本地
//        SQLite.select()
//                .from(User.class)
//                .where(User_Table.isFollow.eq(true))
//                .and(User_Table.id.notEq(Account.getUserId()))
//                .orderBy(User_Table.name, true)
//                .limit(100)
//                .async()
//                .queryListResultCallback(this)
//                .execute();

        // select *from User_Table left join UserFollow_Table on User_Table.id = UserFollow_Table.targetId_id
        // where User_Table.id != Account.getUserId() and UserFollow_Table.originId_id = Account.getUserId()
//        SQLite.select()
//                .from(User.class)
//                .leftOuterJoin(UserFollow.class)
//                .on(User_Table.id.withTable().eq(UserFollow_Table.target_id.withTable()))
//                // 巨坑这里没有自动加上表名区别是哪个表的id
////                .where((User_Table.id.notEq(Account.getUserId())))
//                .where(UserFollow_Table.target_id.notEq(Account.getUserId()))
//                .and(UserFollow_Table.origin_id.eq(Account.getUserId()))
//                .orderBy(User_Table.name, true)
//                .limit(100)
//                .async()
//                .queryListResultCallback(this)
//                .execute();

        SQLite.select()
                .from(User.class)
                .where(User_Table.id.eq(SQLite.select(UserFollow_Table.target_id)
                        .from(UserFollow.class)
                        .where(UserFollow_Table.target_id.notEq(Account.getUserId()))
                        .and(UserFollow_Table.origin_id.eq(Account.getUserId()))))
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
