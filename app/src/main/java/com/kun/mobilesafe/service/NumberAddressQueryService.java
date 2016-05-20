package com.kun.mobilesafe.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.Toast;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.receiver.OutCallReceive;
import com.kun.mobilesafe.utils.NumberAddressQueryUtils;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;
import com.kun.mobilesafe.view.MyToast;

/**
 * Created by Vonnie on 2015/11/16.
 */
public class NumberAddressQueryService extends Service {
    private TelephonyManager tm;
    private OutCallReceive receive;
    private MyPhoneStateListener mListener;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state)
            {
                case TelephonyManager.CALL_STATE_RINGING:
                    String address= NumberAddressQueryUtils.queryNumber(incomingNumber);
//                    Toast.makeText(NumberAddressQueryService.this,address,Toast.LENGTH_LONG);
                    showMyToast(address);
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    MyToast toast=MyToast.getInstance(NumberAddressQueryService.this);
                    toast.closeToast();
                    break;
            }
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        mContext=NumberAddressQueryService.this;
        tm= (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        mListener=new MyPhoneStateListener();

        tm.listen(mListener,PhoneStateListener.LISTEN_CALL_STATE);

        receive=new OutCallReceive();
        IntentFilter filter=new IntentFilter();
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        registerReceiver(receive, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        tm.listen(mListener,PhoneStateListener.LISTEN_NONE);
        mListener=null;

        unregisterReceiver(receive);
        receive=null;
    }

    private void showMyToast(String pAddress) {
        SharedPreferencesHelper spHelper=new SharedPreferencesHelper(mContext,mContext.getString(R.string.sp_name));
        int[] colors={Color.TRANSPARENT,Color.YELLOW,Color.BLUE,Color.GRAY,Color.GREEN};
        int type=spHelper.getSharedPreferencesInt(mContext.getString(R.string.sp_text_bkg),0);

        MyToast toast=MyToast.getInstance(mContext);
        toast.showToast(pAddress,colors[type]);
    }
}
