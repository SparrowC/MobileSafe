package com.kun.mobilesafe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.database.base.LockAppDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vonnie on 2015/11/19.
 */
public class LockAppDBDAO {
    private Context mContext;
    private LockAppDataBaseHelper mLockAppDataBaseHelper;

    public LockAppDBDAO(Context pContext) {
        mContext = pContext;
        mLockAppDataBaseHelper =new LockAppDataBaseHelper(pContext);
    }

   public void AddLockApp(String name)
    {
        SQLiteDatabase database= mLockAppDataBaseHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(mContext.getString(R.string.LA_name),name);
        database.insert(mContext.getString(R.string.LockAppTable_name), null, values);
    }
    public void DeleteLockApp(String name)
   {
       SQLiteDatabase database= mLockAppDataBaseHelper.getWritableDatabase();
       database.delete(mContext.getString(R.string.LockAppTable_name), "name=?", new String[]{name});
   }


    public List<String> QueryAllLockApp()
    {
        List<String> appList=new ArrayList<>();
        SQLiteDatabase database= mLockAppDataBaseHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from " + mContext.getString(R.string.LockAppTable_name), null);

        while (cursor.moveToNext())
        {
            String name=cursor.getString(0);
            appList.add(name);
        }
        return appList;
    }


    public boolean find(String name)
    {
        SQLiteDatabase database= mLockAppDataBaseHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from " + mContext.getString(R.string.LockAppTable_name) + " where "+mContext.getString(R.string.LA_name)+" = ?", new String[]{name});

        if (cursor.moveToNext())
            return true;
        else
            return false;
    }


}
