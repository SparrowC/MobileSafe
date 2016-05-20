package com.kun.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.service.AppLockService;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;

/**
 * Created by Vonnie on 2015/12/3.
 */
public class ScreenOnReceiver extends BroadcastReceiver {
    private SharedPreferencesHelper spHelper;
    @Override
    public void onReceive(Context context, Intent intent) {
        spHelper=new SharedPreferencesHelper(context,context.getString(R.string.sp_name));
        boolean startAppLockService=spHelper.getSharedPreferencesBoolean(context.getString(R.string.sp_text_start_app_lock_service),false);
        if(startAppLockService)
        {
            //Log.d("ScreenOnReceiver","开启程序锁服务");
            Intent serviceIntent=new Intent(context, AppLockService.class);
            context.startService(serviceIntent);
        }
    }
}
