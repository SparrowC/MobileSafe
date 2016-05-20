package com.kun.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.utils.NumberAddressQueryUtils;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;
import com.kun.mobilesafe.view.MyToast;

/**
 * Created by Vonnie on 2015/11/16.
 */
public class OutCallReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String number=getResultData();
        //Toast.makeText(context,NumberAddressQueryUtils.queryNumber(number),Toast.LENGTH_LONG).show();

        SharedPreferencesHelper spHelper=new SharedPreferencesHelper(context,context.getString(R.string.sp_name));
        int[] colors={Color.TRANSPARENT,Color.YELLOW,Color.BLUE,Color.GRAY,Color.GREEN};
        int type=spHelper.getSharedPreferencesInt(context.getString(R.string.sp_text_bkg),0);

        MyToast toast=MyToast.getInstance(context);
        toast.showToast(NumberAddressQueryUtils.queryNumber(number),colors[type]);
    }
}
