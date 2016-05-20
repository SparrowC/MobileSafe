package com.kun.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;

/**
 * Created by Vonnie on 2015/11/8.
 */
public class SIMChangeReceiver extends BroadcastReceiver {
    private SharedPreferencesHelper spHelper;
    private TelephonyManager mTelManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        spHelper=new SharedPreferencesHelper(context,context.getString(R.string.sp_name));
        if(spHelper.getSharedPreferencesBoolean(context.getString(R.string.sp_text_bind),false))
        {
            mTelManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            String tel=spHelper.getSharedPreferencesString(context.getString(R.string.sp_text_sim_sn),null)+"123";
            String realTel=mTelManager.getSimSerialNumber();
            if(!tel.equals(realTel))
            {//换卡了
                Toast.makeText(context,"sim卡已变更！:"+realTel,Toast.LENGTH_LONG).show();
                Log.d("SIM","sim卡已变更！:"+realTel);
            }
        }
    }
}
