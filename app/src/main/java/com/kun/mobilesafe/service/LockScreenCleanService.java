package com.kun.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kun.mobilesafe.receiver.ScreenOffReceiver;

/**
 * Created by Vonnie on 2015/12/1.
 */
public class LockScreenCleanService extends Service {
    private ScreenOffReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver=new ScreenOffReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.SCREEN_OFF");
        registerReceiver(mReceiver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
