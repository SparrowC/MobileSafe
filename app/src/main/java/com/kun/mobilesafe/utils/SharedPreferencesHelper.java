package com.kun.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Vector;

/**
 * Created by Vonnie on 2015/11/8.
 * SharedPreferences的帮助类
 */
public class SharedPreferencesHelper {
    private Context mContext;
    private SharedPreferences sp;

    /**
     *
     * @param pPContext
     * @param name SharedPreferences的名字
     */
    public SharedPreferencesHelper(Context pPContext, String name) {
        mContext = pPContext;
        sp=mContext.getSharedPreferences(name,Context.MODE_PRIVATE);
    }

    /**
     * 获取SharedPreferences的key键对应String值
     * @param key   The name of the preference to retrieve.
     * @return
     */
    public String getSharedPreferencesString(String key,String defValue)
    {
        return sp.getString(key,defValue);
    }
    /**
     * Retrieve a boolean value from the preferences.
     *
     * @param key The name of the preference to retrieve.
     * @param defValue Value to return if this preference does not exist.
     *
     * @return Returns the preference value if it exists, or defValue.  Throws
     * ClassCastException if there is a preference with this name that is not
     * a boolean.
     *
     * @throws ClassCastException
     */
    public boolean getSharedPreferencesBoolean(String key,boolean defValue)
    {
        return sp.getBoolean(key, defValue);
    }
    public int getSharedPreferencesInt(String key,int defValue)
    {
        return sp.getInt(key, defValue);
    }
    public void setSharedPreferencesString(String key,String value)
    {
        SharedPreferences.Editor editor=sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void setSharedPreferencesBoolean(String key,boolean value)
    {
        SharedPreferences.Editor editor=sp.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }
    public void setSharedPreferencesInt(String key,int value)
    {
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
