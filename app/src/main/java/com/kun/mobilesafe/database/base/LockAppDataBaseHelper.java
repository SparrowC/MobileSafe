package com.kun.mobilesafe.database.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kun.mobilesafe.R;

/**
 * Created by Vonnie on 2015/11/19.
 */
public class LockAppDataBaseHelper extends SQLiteOpenHelper {

    private String mDBName;
    private Context mContext;
    public LockAppDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }
    public LockAppDataBaseHelper(Context context)
    {
        super(context, "lockapp.db", null, 1);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + mContext.getString(R.string.LockAppTable_name) +
                " ( " + mContext.getString(R.string.LA_name) + " string varchar(30) )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
