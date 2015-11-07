package com.kun.mobilesafe.activity;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.ui.CheckBoxItem;

/**
 * Created by Kun on 2015/11/5.
 */
public class SettingActivity extends BaseActivity {

    private CheckBoxItem cbi_update;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initWidget();
        initListener();
    }

    @Override
    protected void initWidget() {
        cbi_update= (CheckBoxItem) findViewById(R.id.cbi_update);
        sp=getSharedPreferences("config",MODE_PRIVATE);
        boolean b=sp.getBoolean("update",true);
        cbi_update.setChecked(b);
    }

    @Override
    protected void initListener() {
        cbi_update.setOnCheckedListener(new CheckBoxItem.OnCheckBoxItemCheckedListener() {
            @Override
            public void onChecked() {
                SharedPreferences.Editor editor=sp.edit();
                editor.putBoolean("update",true);
                editor.commit();
            }

            @Override
            public void onUnchecked() {
                SharedPreferences.Editor editor=sp.edit();
                editor.putBoolean("update",false);
                editor.commit();
            }
        });
    }
}
