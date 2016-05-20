package com.kun.mobilesafe.database.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.kun.mobilesafe.R;

/**
 * Created by Vonnie on 2015/11/19.
 */
public class BlackNumberDataBaseHelper extends SQLiteOpenHelper {

    private String mDBName;
    private Context mContext;
    public BlackNumberDataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }
    public BlackNumberDataBaseHelper(Context context)
    {
        super(context, "blacknumber.db", null, 1);
        mContext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //db=getWritableDatabase();
        db.execSQL("create table " + mContext.getString(R.string.BlackNumberTable_name) +
                " ( " + mContext.getString(R.string.BN_ID) + " integer primary key autoincrement," +
                mContext.getString(R.string.BN_number) + " string varchar(20)," +
                mContext.getString(R.string.BN_name) + " varchar(20)," +
                mContext.getString(R.string.BN_mode) + " integer)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
