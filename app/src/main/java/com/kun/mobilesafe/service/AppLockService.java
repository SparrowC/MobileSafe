package com.kun.mobilesafe.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.AppGuardActivity;
import com.kun.mobilesafe.database.LockAppDBDAO;

import java.util.List;

/**
 * Created by Vonnie on 2015/12/2.
 */
public class AppLockService extends Service {
    private ActivityManager am;
    private boolean isRun;
    private List<String> packName;
    private LockAppDBDAO dao;
    private LockedAppChangedReceiver mLockedAppChangedReceiver;
    private UnlockAppReceiver mUnlockAppReceiver;
    private String unlockAppName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isRun=false;
        unlockAppName="";
        //注册广播
        mLockedAppChangedReceiver =new LockedAppChangedReceiver();
        IntentFilter lockedAppChangedFilter=new IntentFilter();
        lockedAppChangedFilter.addAction("com.kun.mobilesafe.LOCKED_APP_CHANGED");
        registerReceiver(mLockedAppChangedReceiver,lockedAppChangedFilter);

        mUnlockAppReceiver =new UnlockAppReceiver();
        IntentFilter unlockAppFilter=new IntentFilter();
        unlockAppFilter.addAction("com.kun.mobilesafe.UNLOCK_APP");
        registerReceiver(mUnlockAppReceiver,unlockAppFilter);


        am= (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        dao=new LockAppDBDAO(getApplicationContext());
        packName=dao.QueryAllLockApp();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isRun)
                {
                    List<ActivityManager.RunningAppProcessInfo> infos=am.getRunningAppProcesses();
                    //Log.d("AppLockService", infos.get(0).processName);
                    String name=infos.get(0).processName;
                    if(!name.equals(unlockAppName)&&!name.equals(getString(R.string.app_packageName)))
                    {//应用未被解锁
                        if(packName.contains(name))
                        {//受保护的应用
                            Intent intent=new Intent();
                            intent.setClass(getApplicationContext(), AppGuardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("name",name);
                            startActivity(intent);
                        }
                    }

                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();

    }
    
    @Override
    public void onDestroy() {
        isRun=true;
        unregisterReceiver(mLockedAppChangedReceiver);
        mLockedAppChangedReceiver =null;
        unregisterReceiver(mUnlockAppReceiver);
        mUnlockAppReceiver=null;
        super.onDestroy();
    }

    /**
     * 锁屏应用更新的广播接受者
     * 当新增或减少需要保护的应用时，会发一个com.kun.mobilesafe.LOCKED_APP_CHANGED的广播
     * 由该接受者，接受广播
     */
    class LockedAppChangedReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
                packName=dao.QueryAllLockApp();
        }
    }

    /**
     * 当用户输入正确密码时，会发一个广播com.kun.mobilesafe.UNLOCK_APP，
     * 由 该广播接受者处理
     */
    class UnlockAppReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            unlockAppName=intent.getStringExtra("name");
        }
    }
}
