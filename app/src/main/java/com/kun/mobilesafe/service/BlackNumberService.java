package com.kun.mobilesafe.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Telephony;
import android.support.annotation.Nullable;
import android.telephony.SmsMessage;
import android.util.Log;

import com.kun.mobilesafe.beans.BlackNumberBeans;
import com.kun.mobilesafe.database.BlackNumberDBDAO;

import java.util.List;

/**
 * Created by Vonnie on 2015/11/22.
 */
public class BlackNumberService extends Service {
    private InnerSmsReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        mReceiver=new InnerSmsReceiver();
        IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(mReceiver, filter);
        super.onCreate();
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(mReceiver);
        mReceiver=null;
        super.onDestroy();
    }


    //inner class
    class InnerSmsReceiver extends BroadcastReceiver{

        private static final String TAG = "InnerSmsReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle bundle=intent.getExtras();
            Object[] objects= (Object[]) bundle.get("pdus");

            SmsMessage[] messages=new SmsMessage[objects.length];
            BlackNumberDBDAO dao=new BlackNumberDBDAO(context);
            List<String> numbers= BlackNumberBeans.BeansToNumberList(dao.QueryBlackNumberByMode(new String[]{"1","2"}));
            Log.d(TAG,objects.length+"");
            for(int i=0;i<objects.length;i++)
            {
                messages[i]= SmsMessage.createFromPdu((byte[]) objects[i]);
                String number=messages[i].getOriginatingAddress();
                int mode=dao.QueryModeByNumber(number);
                if(mode==2||mode==3)
                {
                    deleteMessage(number);
                    this.abortBroadcast();
                    Log.d(TAG, "已拦截黑名单中短信");
                }

            }
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private void deleteMessage(String number)
        {
            Cursor c = getContentResolver().query(Telephony.Mms.CONTENT_URI, null, null, null, null);
            //c.moveToFirst();
            while (c.moveToNext())
            {
                System.out.println("Inside if loop");
                try
                {
                    String address = c.getString(2);
                    Log.i( TAG, c.getString(2) );
                    if ( address.trim().equals( number ) )
                    {
                        String pid = c.getString(1);
                        String uri = "content://sms/conversations/" + pid;
                        getContentResolver().delete(Uri.parse(uri), null, null);
                        stopSelf();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    }

}
