package com.kun.mobilesafe.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.service.AppLockService;
import com.kun.mobilesafe.utils.ServiceUtils;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;
import com.kun.mobilesafe.view.CheckBoxItem;

/**
 * Created by Vonnie on 2015/12/2.
 */
public class AdvancedToolActivity extends BaseActivity {
    private CheckBoxItem cbi_appLock;
    private SharedPreferencesHelper spHelper;
    private RelativeLayout rl_setLockApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMainContent(R.layout.activity_advanced_tool);
        init(this, "高级工具");

    }

    @Override
    protected void initWidget() {
        cbi_appLock= (CheckBoxItem) findViewById(R.id.cbi_appLock);
        boolean isRunning=ServiceUtils.isServiceExist(this,"com.kun.mobilesafe.service.AppLockService");
        cbi_appLock.setChecked(isRunning);
        rl_setLockApp= (RelativeLayout) findViewById(R.id.rl_setLockApp);
        spHelper=new SharedPreferencesHelper(this,getString(R.string.sp_name));
    }

    @Override
    protected void initListener() {
        //打开服务
        final Intent appLockServiceIntent=new Intent(this, AppLockService.class);
        cbi_appLock.setOnCheckedListener(new CheckBoxItem.OnCheckBoxItemCheckedListener() {
            @Override
            public void onChecked() {
                startService(appLockServiceIntent);
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_start_app_lock_service), true);

            }

            @Override
            public void onUnchecked() {
                stopService(appLockServiceIntent);
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_start_app_lock_service), false);
            }
        });

        rl_setLockApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spHelper.getSharedPreferencesString(getString(R.string.sp_text_app_lock_pwd),null)==null)//判断是否设置过密码
                    openActivity(SetLockPwdActivity.class);
                else //打开输入密码Activity
                    openActivity(EnterPwdActivityInSafe.class);
            }
        });
    }
}
