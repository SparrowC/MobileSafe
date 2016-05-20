package com.kun.mobilesafe.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Vonnie on 2015/11/15.
 */
public class VirusQueryUtils {
    private static String path="data/data/com.kun.mobilesafe/files/antivirus.db";
    public static String isVirus(String md5)
    {
        String result = null;
        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null,
                SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select desc from datable where md5=?", new String[]{md5});
        if(cursor.moveToNext()){
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }
}
