package com.kun.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
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
public class EnterPwdActivityInSafe extends Activity {
    private EditText et_pwd;
    private ImageView iv_back;
    private Button btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7,btn_8,btn_9,btn_enter,btn_X;
    private String pwdStr;
    private SharedPreferencesHelper spHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_enter_pwd);
        initWidget();
        initListener();
    }

    private void initWidget() {
        pwdStr="";
        spHelper=new SharedPreferencesHelper(this,getString(R.string.sp_name));
        et_pwd= (EditText) findViewById(R.id.et_pwd);
        iv_back= (ImageView) findViewById(R.id.iv_back);
        btn_0= (Button) findViewById(R.id.btn_0);
        btn_1= (Button) findViewById(R.id.btn_1);
        btn_2= (Button) findViewById(R.id.btn_2);
        btn_3= (Button) findViewById(R.id.btn_3);
        btn_4= (Button) findViewById(R.id.btn_4);
        btn_5= (Button) findViewById(R.id.btn_5);
        btn_6= (Button) findViewById(R.id.btn_6);
        btn_7= (Button) findViewById(R.id.btn_7);
        btn_8= (Button) findViewById(R.id.btn_8);
        btn_9= (Button) findViewById(R.id.btn_9);
        btn_enter= (Button) findViewById(R.id.btn_enter);
        btn_X= (Button) findViewById(R.id.btn_X);
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EnterPwdActivityInSafe.this.finish();
            }
        });
        btn_0.setOnClickListener(new MyOnClickListener());
        btn_1.setOnClickListener(new MyOnClickListener());
        btn_2.setOnClickListener(new MyOnClickListener());
        btn_3.setOnClickListener(new MyOnClickListener());
        btn_4.setOnClickListener(new MyOnClickListener());
        btn_5.setOnClickListener(new MyOnClickListener());
        btn_6.setOnClickListener(new MyOnClickListener());
        btn_7.setOnClickListener(new MyOnClickListener());
        btn_8.setOnClickListener(new MyOnClickListener());
        btn_9.setOnClickListener(new MyOnClickListener());
        btn_X.setOnClickListener(new MyOnClickListener());
        btn_enter.setOnClickListener(new MyOnClickListener());
    }

    private boolean checkPassword(String pwd)
    {
        if(pwd.equals(spHelper.getSharedPreferencesString(getString(R.string.sp_text_app_lock_pwd),"")))
            return true;
        else
            return false;
    }
    class MyOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.btn_0:
                    pwdStr+="0";
                    break;
                case R.id.btn_1:
                    pwdStr+="1";
                    break;
                case R.id.btn_2:
                    pwdStr+="2";
                    break;
                case R.id.btn_3:
                    pwdStr+="3";
                    break;
                case R.id.btn_4:
                    pwdStr+="4";
                    break;
                case R.id.btn_5:
                    pwdStr+="5";
                    break;
                case R.id.btn_6:
                    pwdStr+="6";
                    break;
                case R.id.btn_7:
                    pwdStr+="7";
                    break;
                case R.id.btn_8:
                    pwdStr+="8";
                    break;
                case R.id.btn_9:
                    pwdStr+="9";
                    break;
                case R.id.btn_enter:
                    if(checkPassword(et_pwd.getText().toString()))
                    {//密码正确
                        Intent intent=new Intent(getApplicationContext(),SetLockedAppActivity.class);
                        startActivity(intent);
                        EnterPwdActivityInSafe.this.finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"密码不正确，请重新输入！",Toast.LENGTH_SHORT).show();
                        pwdStr="";
                    }
                    break;
                case R.id.btn_X:
                    if(pwdStr.length()>0)
                        pwdStr=pwdStr.substring(0,pwdStr.length()-1);
                    break;
            }
            et_pwd.setText(pwdStr);
        }
    }
}
