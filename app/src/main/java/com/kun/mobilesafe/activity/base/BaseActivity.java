package com.kun.mobilesafe.activity.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kun.mobilesafe.R;

/**
 * Created by Kun on 2015/11/5.
 */
public abstract class BaseActivity extends Activity {
    private TextView tv_mouldTitle;
    private ImageView iv_back;
    private FrameLayout frame_mainContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_mould);
        tv_mouldTitle= (TextView) findViewById(R.id.tv_mouldTitle);
        frame_mainContent= (FrameLayout) findViewById(R.id.frame_mainContent);

    }

    protected void init(Activity activity,String title)
    {
        initWidget();
        initListener();
        initBackButton(activity);
        setMyTitle(title);
    }
    protected void initBackButton(final Activity activity)
    {
        iv_back= (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });
    }
    protected void setMyTitle(String title)
    {
        tv_mouldTitle.setText(title);
    }
    protected void addMainContent(int id)
    {
        View view=LayoutInflater.from(this).inflate(id,null);
        frame_mainContent.addView(view);
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
