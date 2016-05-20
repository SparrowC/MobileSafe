package com.kun.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * Created by Vonnie on 2015/11/16.
 */
public class ServiceUtils {
    static public boolean isServiceExist(Context pContext,String serviceName)
    {
        ActivityManager am= (ActivityManager) pContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> infos=am.getRunningServices(100);
        for(int i=0;i<infos.size();i++)
        {
            if(infos.get(i).service.getClassName().equals(serviceName))
                return true;
        }
        return false;
    }
}
