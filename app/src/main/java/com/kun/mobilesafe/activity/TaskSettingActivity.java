package com.kun.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.service.LockScreenCleanService;
import com.kun.mobilesafe.utils.ServiceUtils;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;
import com.kun.mobilesafe.view.CheckBoxItem;

/**
 * Created by Vonnie on 2015/12/1.
 */
public class TaskSettingActivity extends BaseActivity {
    private CheckBoxItem cbi_showSysTask,cbi_screenOffClean;
    private SharedPreferencesHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMainContent(R.layout.activity_task_setting);
        setMyTitle("设置");
        initWidget();
        initListener();
        initBackButton(this);
    }

    @Override
    protected void initWidget() {
        cbi_showSysTask= (CheckBoxItem) findViewById(R.id.cbi_showSysTask);
        cbi_screenOffClean= (CheckBoxItem) findViewById(R.id.cbi_screenOffClean);

        spHelper=new SharedPreferencesHelper(this,getString(R.string.sp_name));
        cbi_showSysTask.setChecked(spHelper.getSharedPreferencesBoolean(getString(R.string.sp_text_show_sys_task), false));
        boolean isServiceExist=ServiceUtils.isServiceExist(this, getString(R.string.LockScreenCleanService));
        cbi_screenOffClean.setChecked(isServiceExist);//如果LockScreenCleanService还在内存中，就显示启用锁屏清理
    }

    @Override
    protected void initListener() {

        cbi_showSysTask.setOnCheckedListener(new CheckBoxItem.OnCheckBoxItemCheckedListener() {
            @Override
            public void onChecked() {
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_show_sys_task),true);
            }

            @Override
            public void onUnchecked() {
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_show_sys_task),false);
            }
        });
        final Intent intent=new Intent(this, LockScreenCleanService.class);
        cbi_screenOffClean.setOnCheckedListener(new CheckBoxItem.OnCheckBoxItemCheckedListener() {
            @Override
            public void onChecked() {
                startService(intent);
            }

            @Override
            public void onUnchecked() {
                stopService(intent);
            }
        });
    }




}//end
