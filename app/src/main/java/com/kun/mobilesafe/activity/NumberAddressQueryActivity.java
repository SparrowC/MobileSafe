package com.kun.mobilesafe.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.utils.NumberAddressQueryUtils;

/**
 * Created by Vonnie on 2015/11/15.
 */
public class NumberAddressQueryActivity extends BaseActivity {
    private EditText et_queryAddress;
    private Button btn_query;
    private TextView tv_queryResult;
    @Override
    protected void initWidget() {
        et_queryAddress= (EditText) findViewById(R.id.et_queryAddress);
        btn_query= (Button) findViewById(R.id.btn_query);
        tv_queryResult= (TextView) findViewById(R.id.tv_queryResult);
    }

    @Override
    protected void initListener() {
        btn_query.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number=et_queryAddress.getText().toString();
                String address=NumberAddressQueryUtils.queryNumber(number);
                tv_queryResult.setText(address);
            }
        });
        et_queryAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>=3)
                {
                    String number=et_queryAddress.getText().toString();
                    String address=NumberAddressQueryUtils.queryNumber(number);
                    tv_queryResult.setText(address);
                }else
                    tv_queryResult.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMainContent(R.layout.activity_number_address_query);
        setMyTitle("号码归属地查询");
        initWidget();
        initListener();
        initBackButton(this);
    }
}
