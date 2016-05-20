package com.kun.mobilesafe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;

/**
 * Created by Vonnie on 2015/11/7.
 */
public class BurglarAlarmActivity extends BaseActivity {
    private Button btn_enterWizard;
    private ImageView iv_safe_lock;
    private TextView tv_safe_pn;
    private SharedPreferencesHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_burglar_alarm);
        addMainContent(R.layout.activity_burglar_alarm);
        setMyTitle("手机防盗");
        initWidget();
        initListener();
        initBackButton(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initWidget();
    }

    @Override
    protected void initWidget() {
        btn_enterWizard= (Button) findViewById(R.id.btn_enterWizard);
        iv_safe_lock= (ImageView) findViewById(R.id.iv_safe_lock);
        tv_safe_pn= (TextView) findViewById(R.id.tv_safe_pn);
        helper=new SharedPreferencesHelper(this,getString(R.string.sp_name));
        String pn=helper.getSharedPreferencesString(getString(R.string.sp_text_setPN),null);
        if(pn!=null)
        {
            tv_safe_pn.setText(pn);
        }
        if(helper.getSharedPreferencesBoolean(getString(R.string.sp_text_open_alarm), false))
        {
            iv_safe_lock.setImageResource(R.drawable.lock);
        }else {
            iv_safe_lock.setImageResource(R.drawable.unlock);
        }
    }

    @Override
    protected void initListener() {
        btn_enterWizard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(WizardActivity.class);
            }
        });

        iv_safe_lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean b=helper.getSharedPreferencesBoolean(getString(R.string.sp_text_open_alarm), false);
                if(!b)
                {//原本关闭，现在打开
                    iv_safe_lock.setImageResource(R.drawable.lock);
                }else {//原本打开，现在关闭
                    iv_safe_lock.setImageResource(R.drawable.unlock);
                }
                helper.setSharedPreferencesBoolean(getString(R.string.sp_text_open_alarm),!b);
            }
        });
    }
}
