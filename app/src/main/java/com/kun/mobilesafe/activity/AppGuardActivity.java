package com.kun.mobilesafe.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;

/**
 * Created by Vonnie on 2015/12/3.
 */
public class AppGuardActivity extends Activity {
    private EditText et_pwd;
    private ImageView iv_back,iv_appIcon,iv_lock;
    private SharedPreferencesHelper spHelper;

    private PackageManager pm;
    /**
     * 当前程序锁所保护的应用的包名
     */
    private String packName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_app_guard);

        packName=getIntent().getStringExtra("name");

        initWidget();
        initListener();
    }
    private void initWidget() {
        spHelper=new SharedPreferencesHelper(this,getString(R.string.sp_name));
        et_pwd= (EditText) findViewById(R.id.et_pwd);
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_appIcon= (ImageView) findViewById(R.id.iv_appIcon);
        iv_lock= (ImageView) findViewById(R.id.iv_lock);
        pm = getPackageManager();
        PackageInfo info;
        try {
            info=pm.getPackageInfo(packName,0);
            iv_appIcon.setImageDrawable(info.applicationInfo.loadIcon(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//关闭此Activity
                backToDesk();
            }
        });

        iv_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input=et_pwd.getText().toString();
                if(input==null||input.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"密码错误！",Toast.LENGTH_SHORT).show();
                    return;
                }
                String pwdStr=spHelper.getSharedPreferencesString(getString(R.string.sp_text_app_lock_pwd),"");
                if (input.equals(pwdStr))//密码正确
                {
                    //发送一个广播
                    Intent intent=new Intent();
                    intent.setAction("com.kun.mobilesafe.UNLOCK_APP");
                    intent.putExtra("name",packName);
                    sendBroadcast(intent);
                    AppGuardActivity.this.finish();

                }else {
                    Toast.makeText(getApplicationContext(),"密码错误！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void backToDesk()
    {
        Intent intent=new Intent();
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.HOME");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addCategory("android.intent.category.MONKEY");
        startActivity(intent);
        this.finish();
    }
    @Override
    public void onBackPressed() {
        backToDesk();
    }
}
