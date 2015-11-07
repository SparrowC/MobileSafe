package com.kun.mobilesafe.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

/**
 * Created by Kun on 2015/11/5.
 */
public abstract class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    protected void showMessage(String text)
    {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }

    protected void openActivity(Class<?> cls)
    {
        Intent intent=new Intent(this,cls);
        startActivity(intent);
    }
    abstract protected  void initWidget();
    abstract protected  void initListener();

}
