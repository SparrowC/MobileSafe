package com.kun.mobilesafe.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kun.mobilesafe.service.AppLockService;
import com.kun.mobilesafe.utils.ServiceUtils;

import java.util.List;

/**
 * Created by Vonnie on 2015/12/1.
 */
public class ScreenOffReceiver extends BroadcastReceiver {
    private ActivityManager am;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.d("ScreenOffReceiver","锁屏了");
        am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> infoList=am.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo info:infoList)
        {
            am.killBackgroundProcesses(info.processName);
        }
        if(ServiceUtils.isServiceExist(context,"com.kun.mobilesafe.service.AppLockService"))
        {
            //Log.d("ScreenOffReceiver","关闭程序锁服务");
            Intent serviceIntent=new Intent(context, AppLockService.class);
            context.stopService(serviceIntent);
        }
    }
}
