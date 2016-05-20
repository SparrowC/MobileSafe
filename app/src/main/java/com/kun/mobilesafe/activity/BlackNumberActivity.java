package com.kun.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.kun.mobilesafe.Adapter.BlackNumberAdapter;
import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.beans.BlackNumberBeans;
import com.kun.mobilesafe.database.BlackNumberDBDAO;

import java.util.List;

/**
 * Created by Vonnie on 2015/11/21.
 */
public class BlackNumberActivity extends BaseActivity {
    private TextView iv_addBlackNumber;
    private ImageView iv_back;
    private ListView lv_blackNumber;
    private BlackNumberAdapter mAdapter;
    private BlackNumberDBDAO dao;
    private List<BlackNumberBeans> mBlackNumberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_number);
        initWidget();
        initListener();

    }

    @Override
    protected void initWidget() {
        iv_addBlackNumber= (TextView) findViewById(R.id.iv_addBlackNumber);
        iv_back= (ImageView) findViewById(R.id.iv_back);
        lv_blackNumber= (ListView) findViewById(R.id.lv_blackNumber);
        dao=new BlackNumberDBDAO(this);
        mBlackNumberList=dao.QueryBlackNumber();
        mAdapter=new BlackNumberAdapter(this,mBlackNumberList);
        lv_blackNumber.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        iv_addBlackNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenAddBlackNumberDlg();
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlackNumberActivity.this.finish();
            }
        });
    }

    private void OpenAddBlackNumberDlg() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view= LayoutInflater.from(this).inflate(R.layout.add_black_number,null);
        final EditText et_phoneNumber= (EditText) view.findViewById(R.id.et_phoneNumber);
        final CheckBox cb_call= (CheckBox) view.findViewById(R.id.cb_call);
        final CheckBox cb_sms= (CheckBox) view.findViewById(R.id.cb_sms);
        builder.setTitle("添加黑名单")
                .setView(view)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int i = 0;
                        if (cb_call.isChecked()) {
                            i = 1;
                            if (cb_sms.isChecked()) {
                                i=3;
                            }
                        }else if (cb_sms.isChecked()) {
                            i=2;
                        }
                        BlackNumberBeans beans=new BlackNumberBeans();
                        beans.setMode(i);
                        beans.setNumber(et_phoneNumber.getText().toString());
                        dao.AddBlackNumber(beans);
                        mBlackNumberList.add(beans);
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                })
                .show();
    }
}
