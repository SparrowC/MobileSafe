package com.kun.mobilesafe.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.receiver.ScreenOffReceiver;
import com.kun.mobilesafe.receiver.ScreenOnReceiver;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;

/**
 * Created by Vonnie on 2015/12/2.
 */
public class SetLockPwdActivity extends BaseActivity {
    private EditText et_setPwd1,et_setPwd2;
    private SharedPreferencesHelper spHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMainContent(R.layout.activity_set_lock_pwd);
        init(this,"设置密码");
    }

    @Override
    protected void initWidget() {
        et_setPwd1= (EditText) findViewById(R.id.et_setPwd1);
        et_setPwd2= (EditText) findViewById(R.id.et_setPwd2);
        spHelper=new SharedPreferencesHelper(this,getString(R.string.sp_name));

    }

    @Override
    protected void initListener() {

    }
    public void save(View view)
    {
        String pwd1=et_setPwd1.getText().toString();
        String pwd2=et_setPwd2.getText().toString();
        if(pwd1.equals(""))
        {
            showMessage("密码不能为空！");
            return;
        }
        if (!pwd1.equals(pwd2))
        {
            showMessage("两次密码不相同！");
            return;
        }else {
            spHelper.setSharedPreferencesString(getString(R.string.sp_text_app_lock_pwd),pwd1);
            showMessage("密码设置成功！");
            openActivity(SetLockedAppActivity.class);//打开设置需要锁的app
            this.finish();
        }
    }
}
