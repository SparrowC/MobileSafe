package com.kun.mobilesafe.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;

/**
 * Created by Vonnie on 2015/11/15.
 */
public class CommunicationActivity extends BaseActivity {
    private TextView tv_numberAddressQuery,tv_blackNumber;
    @Override
    protected void initWidget() {
        tv_numberAddressQuery= (TextView) findViewById(R.id.tv_numberAddressQuery);
        tv_blackNumber= (TextView) findViewById(R.id.tv_blackNumber);
    }

    @Override
    protected void initListener() {
        tv_numberAddressQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(NumberAddressQueryActivity.class);
            }
        });
        tv_blackNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivity(BlackNumberActivity.class);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMainContent(R.layout.activity_communication);
        setMyTitle("通讯卫士");
        initWidget();
        initListener();
        initBackButton(this);
    }
}
