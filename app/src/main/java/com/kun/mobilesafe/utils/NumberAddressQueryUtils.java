package com.kun.mobilesafe.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by Vonnie on 2015/11/15.
 */
public class NumberAddressQueryUtils {
    private static String path="data/data/com.kun.mobilesafe/files/address.db";
    public static String queryNumber(String number)
    {
        String address="";

        SQLiteDatabase database=SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor=null;

        if(number.length()>=3&&number.length()<7)
        {
            cursor=database.rawQuery("select location from data2 where area= ?"
                    , new String[]{Integer.parseInt(number.substring(0, 3))+""});
        }else if(number.length()>=7)
        {
            cursor=database.rawQuery("select location from data2 where id= (select outkey from data1 where id = ?)"
                    , new String[]{number.substring(0, 7)});
            if(cursor.getCount()<1)
            {
                cursor=database.rawQuery("select location from data2 where area= ?"
                        , new String[]{Integer.parseInt(number.substring(0, 3))+""});
            }
        }

        while (cursor.moveToNext())
        {
            address+=cursor.getString(0)+'\n';
        }
        cursor.close();
        if(address.equals(""))
            address="未知地区";
        return address;
    }
}
