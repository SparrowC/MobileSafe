package com.kun.mobilesafe.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.beans.BlackNumberBeans;
import com.kun.mobilesafe.database.base.BlackNumberDataBaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vonnie on 2015/11/19.
 */
public class BlackNumberDBDAO {
    private Context mContext;
    private BlackNumberDataBaseHelper mBlackNumberDataBaseHelper;

    public BlackNumberDBDAO(Context pContext) {
        mContext = pContext;
        mBlackNumberDataBaseHelper =new BlackNumberDataBaseHelper(pContext);
    }

   public void AddBlackNumber(BlackNumberBeans pBeans)
    {
        SQLiteDatabase database= mBlackNumberDataBaseHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
//        values.put(mContext.getString(R.string.BN_ID),pBeans.getID());
        values.put(mContext.getString(R.string.BN_number),pBeans.getNumber());
        values.put(mContext.getString(R.string.BN_name),pBeans.getName());
        values.put(mContext.getString(R.string.BN_mode),pBeans.getMode());
        database.insert(mContext.getString(R.string.BlackNumberTable_name),null,values);
    }
    public void DeleteBlackNumber(String number)
   {
       SQLiteDatabase database= mBlackNumberDataBaseHelper.getWritableDatabase();
       database.delete(mContext.getString(R.string.BlackNumberTable_name), "number=?", new String[]{number});
   }
    public void DeleteBlackNumber(BlackNumberBeans pBeans)
   {
       DeleteBlackNumber(pBeans.getNumber());
   }

    public void UpdateBlackNumber(BlackNumberBeans pBeans)
    {
        SQLiteDatabase database= mBlackNumberDataBaseHelper.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(mContext.getString(R.string.BN_ID),pBeans.getID());
        values.put(mContext.getString(R.string.BN_number),pBeans.getNumber());
        values.put(mContext.getString(R.string.BN_name),pBeans.getName());
        values.put(mContext.getString(R.string.BN_mode),pBeans.getMode());
        database.update(mContext.getString(R.string.BlackNumberTable_name), values, mContext.getString(R.string.BN_number) + "=?", new String[]{pBeans.getName()});
    }

    public List<BlackNumberBeans> QueryBlackNumber()
    {
        List<BlackNumberBeans> beansList=new ArrayList<>();
        SQLiteDatabase database= mBlackNumberDataBaseHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from " + mContext.getString(R.string.BlackNumberTable_name), null);

        while (cursor.moveToNext())
        {
            BlackNumberBeans beans=new BlackNumberBeans();
            beans.setID(cursor.getInt(0));
            beans.setNumber(cursor.getString(1));
            beans.setName(cursor.getString(2));
            beans.setMode(cursor.getInt(3));
            beansList.add(beans);
        }
        return beansList;
    }

    public List<BlackNumberBeans> QueryBlackNumberByMode(String[] modes)
    {
        List<BlackNumberBeans> beansList=new ArrayList<>();
        SQLiteDatabase database= mBlackNumberDataBaseHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from " + mContext.getString(R.string.BlackNumberTable_name)+ " where "+mContext.getString(R.string.BN_mode)+" != ? AND "+mContext.getString(R.string.BN_mode)+" != ?", modes);

        while (cursor.moveToNext())
        {
            BlackNumberBeans beans=new BlackNumberBeans();
            beans.setID(cursor.getInt(0));
            beans.setNumber(cursor.getString(1));
            beans.setName(cursor.getString(2));
            beans.setMode(cursor.getInt(3));
            beansList.add(beans);
        }
        return beansList;
    }
    public BlackNumberBeans QueryBlackNumberByNumber(String number)
    {
        SQLiteDatabase database= mBlackNumberDataBaseHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select * from " + mContext.getString(R.string.BlackNumberTable_name) + " where " + mContext.getString(R.string.BN_number) + " = ?", new String[]{number});
        BlackNumberBeans beans=new BlackNumberBeans();
        if (cursor.moveToNext())
        {
            beans.setID(cursor.getInt(0));
            beans.setNumber(cursor.getString(1));
            beans.setName(cursor.getString(2));
            beans.setMode(cursor.getInt(3));
            return beans;
        }else
            return  null;

    }
    public int QueryModeByNumber(String number)
    {
        int mode=0;
        SQLiteDatabase database= mBlackNumberDataBaseHelper.getWritableDatabase();
        Cursor cursor=database.rawQuery("select mode from " + mContext.getString(R.string.BlackNumberTable_name) + " where "+mContext.getString(R.string.BN_number)+" = ?", new String[]{number});
        BlackNumberBeans beans=new BlackNumberBeans();
        if (cursor.moveToNext())
        {
            mode=cursor.getInt(0);
        }
        return mode;
    }


}
