package com.aoliao.example.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @author 你的奥利奥
 * @version 2017/8/17
 */
@Database(name = AppDatabase.NAME,version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 2;
}
