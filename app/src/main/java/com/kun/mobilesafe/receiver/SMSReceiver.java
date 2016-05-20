package com.kun.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.kun.mobilesafe.R;

/**
 * Created by Vonnie on 2015/11/9.
 */
public class SMSReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(SMS_RECEIVED))
        {
            Bundle bundle=intent.getExtras();
            Object[] pdus= (Object[]) bundle.get("pdus");
            SmsMessage[] messages=new SmsMessage[pdus.length];
            for (int i=0;i<pdus.length;i++)
            {
                messages[i]=SmsMessage.createFromPdu((byte[]) pdus[i]);
                String body=messages[i].getMessageBody();

                if (body.equals(context.getString(R.string.GPS)))
                {//定位指令

                }else  if (body.equals(context.getString(R.string.alarm)))
                {//报警指令

                }else  if (body.equals(context.getString(R.string.wipeData)))
                {//清除数据指令

                }else  if (body.equals(context.getString(R.string.lockScreen)))
                {//锁屏指令

                }
            }
        }
    }
}
