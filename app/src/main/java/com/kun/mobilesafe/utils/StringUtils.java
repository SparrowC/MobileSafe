package com.kun.mobilesafe.utils;

import android.content.Context;

/**
 * Created by Kun on 2015/11/5.
 */
public class StringUtils {
    public static String getStringByResID(Context context,int id)
    {
        return context.getResources().getString(id);
    }

}
