package com.david.common.control;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.david.common.dao.gen.DaoMaster;

public class DatabaseOpenHelper extends DaoMaster.DevOpenHelper {

    public DatabaseOpenHelper(Context context, String name) {
        super(context, name);
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        super.getReadableDatabase().execSQL("PRAGMA temp_store_directory = '/data/data/com.david/databases'");
        return super.getWritableDatabase();
    }
}
