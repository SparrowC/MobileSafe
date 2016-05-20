package com.kun.mobilesafe.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.kun.mobilesafe.R;

/**
 * Created by Vonnie on 2015/11/17.
 */
public class MyToast {
    private Context mContext;
    private WindowManager wm;
    private View root;
    private TextView tv_myToast;
    private static MyToast instance;
    private static boolean isRunning;

    private MyToast(Context pContext) {
        mContext = pContext;
        root= LayoutInflater.from(pContext).inflate(R.layout.my_toast,null);
        tv_myToast= (TextView) root.findViewById(R.id.tv_myToast);
        wm= (WindowManager) pContext.getSystemService(Context.WINDOW_SERVICE);
        isRunning=false;
    }
    public static MyToast getInstance(Context pContext)
    {
        if(instance ==null)
            instance =new MyToast(pContext);
        return instance;
    }

    public void showToast(String text,int color)
    {
        isRunning=true;
        tv_myToast.setBackgroundColor(color);
        tv_myToast.setText(text);

        WindowManager.LayoutParams params=new WindowManager.LayoutParams();
        params.height=WindowManager.LayoutParams.WRAP_CONTENT;
        params.width=WindowManager.LayoutParams.WRAP_CONTENT;

        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        params.format = PixelFormat.TRANSLUCENT;
        params.type=WindowManager.LayoutParams.TYPE_TOAST;

        wm.addView(root,params);
    }
    public void closeToast()
    {
        if(isRunning)
        {
            wm.removeView(root);
            root=null;
            instance=null;
            isRunning=false;
        }
    }
}
