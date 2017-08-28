package com.aoliao.example.factory.data.message;

import android.support.annotation.NonNull;

import com.aoliao.example.factory.data.BaseDbRepository;
import com.aoliao.example.factory.model.db.Session;
import com.aoliao.example.factory.model.db.Session_Table;
import com.aoliao.example.factory.persistence.Account;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.Collections;
import java.util.List;

/**
 * @author 你的奥利奥
 * @version 2017/8/23
 */

public class SessionRepository extends BaseDbRepository<Session> implements SessionDataSource {
    @Override
    protected boolean isRequired(Session session) {
        //所有会话都需要，不需要过滤
        return true;
    }

    @Override
    public void load(SucceedCallback<List<Session>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Session.class)
                .where(Session_Table.accountId.eq(Account.getUserId()))
                .orderBy(Session_Table.modifyAt,false)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected void insert(Session session) {
        dataList.addFirst(session);
    }
    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        // 复写数据库回来的方法, 进行一次反转
        Collections.reverse(tResult);

        super.onListQueryResult(transaction, tResult);
    }
}
