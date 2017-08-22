package com.aoliao.example.factory.data.helper;

import com.aoliao.example.factory.model.db.Session;
import com.aoliao.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * @author 你的奥利奥
 * @version 2017/8/21
 */

public class SessionHelper {
    // 从本地查询Session
    public static Session findFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();
    }
}
