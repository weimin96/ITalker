package com.aoliao.example.factory.model.db.view;

import com.aoliao.example.factory.model.db.AppDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;

/**
 * 群成员对应的用户的简单信息表
 *
 * @author 你的奥利奥
 * @version 2017/8/25
 */

@QueryModel(database = AppDatabase.class)
public class MemberUserModel {
    @Column
    public String userId; // User-id/Member-userId
    @Column
    public String name; // User-name
    @Column
    public String alias; // Member-alias
    @Column
    public String portrait; // User-portrait
}

