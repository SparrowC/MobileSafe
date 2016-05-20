package com.kun.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.kun.mobilesafe.Adapter.HomeGridViewAdapter;
import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.beans.FunctionBean;
import com.kun.mobilesafe.database.LockAppDBDAO;
import com.kun.mobilesafe.receiver.ScreenOffReceiver;
import com.kun.mobilesafe.receiver.ScreenOnReceiver;
import com.kun.mobilesafe.utils.EncryptUtils;
import com.kun.mobilesafe.utils.ServiceUtils;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kun on 2015/11/5.
 */
public class HomeActivity extends BaseActivity {
    private GridView gvHomeGrid;
    //private SharedPreferences sp;
    private SharedPreferencesHelper spHelper;
    private final int PASSWORD_EXIST =1;
    private final int PASSWORD_NOT_EXIST =2;
    private AlertDialog mDialog;
    private EditText et_setup_pwd,et_setup_pwd_config;
    private ScreenOnReceiver mScreenOnReceiver;
    private ScreenOffReceiver mScreenOffReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initWidget();
        initListener();
    }


    @Override
    protected void initWidget() {
        spHelper=new SharedPreferencesHelper(this,getString(R.string.sp_name));
        gvHomeGrid= (GridView) findViewById(R.id.gvHomeGrid);
        gvHomeGrid.setAdapter(new HomeGridViewAdapter(this, getFunction()));

        if(spHelper.getSharedPreferencesBoolean(getString(R.string.sp_text_register_screen_on_off_receiver),false))
        {
            mScreenOffReceiver=new ScreenOffReceiver();
            IntentFilter filter=new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(mScreenOffReceiver, filter);
            mScreenOnReceiver=new ScreenOnReceiver();
            filter=new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_ON);
            registerReceiver(mScreenOnReceiver, filter);
            spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_register_screen_on_off_receiver), true);
        }

    }

    private List<FunctionBean> getFunction() {
        String[] names=getResources().getStringArray(R.array.function_list_name);
        int[] ids=new int[9];
        ids[0]=R.drawable.safe;
        ids[1]=R.drawable.callmsgsafe;
        ids[2]=R.drawable.app;
        ids[3]=R.drawable.taskmanager;
        ids[4]=R.drawable.netmanager;
        ids[5]=R.drawable.trojan;
        ids[6]=R.drawable.sysoptimize;
        ids[7]=R.drawable.atools;
        ids[8]=R.drawable.settings;
        List<FunctionBean> beans=new ArrayList<>();
        for (int i=0;i<names.length;i++)
        {
            FunctionBean bean=new FunctionBean();
            bean.setFunctionImageID(ids[i]);
            bean.setFunctionName(names[i]);
            beans.add(bean);
        }
        return beans;
    }

    @Override
    protected void initListener() {
        gvHomeGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position)
                {
                    case 0:
                        OpenBurglarAlarm();
                        break;
                    case 1:
                        openActivity(CommunicationActivity.class);
                        break;
                    case 2:
                        openActivity(AppManagerActivity.class);
                        break;
                    case 3:
                        openActivity(TaskManagerActivity.class);
                        break;
                    case 4:
                        break;
                    case 5:
                        openActivity(AntiVirusActivity.class);
                        break;
                    case 6:
                        break;
                    case 7:
                        openActivity(AdvancedToolActivity.class);
                        break;
                    case 8:
                        openActivity(SettingActivity.class);
                        break;
                }
            }
        });
    }

    private void OpenBurglarAlarm() {
        String password=spHelper.getSharedPreferencesString(getString(R.string.sp_text_password),null);
        if(password==null)
        {//未设置密码
            openPasswordDialog(PASSWORD_NOT_EXIST,password);
        }else//设置了密码
        {
            openPasswordDialog(PASSWORD_EXIST, password);
        }

    }

    private void openPasswordDialog(final int type, final String pPassword) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        View view = null;
        switch (type)
        {
            case PASSWORD_EXIST:
                builder.setTitle("安全验证");
                view= LayoutInflater.from(this).inflate(R.layout.dialog_password_check,null);
                et_setup_pwd= (EditText) view.findViewById(R.id.et_setup_pwd);
                et_setup_pwd_config=null;
                break;
            case PASSWORD_NOT_EXIST:
                view= LayoutInflater.from(this).inflate(R.layout.dialog_password,null);
                et_setup_pwd= (EditText) view.findViewById(R.id.et_setup_pwd);
                et_setup_pwd_config= (EditText) view.findViewById(R.id.et_setup_pwd_config);
                builder.setTitle("设置密码");
                break;
            default:
                return;
        }
        builder.setView(view)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (type) {
                            case PASSWORD_EXIST:
                                if (pPassword.equals(EncryptUtils.Encrypt(et_setup_pwd.getText().toString().trim()))) {//密码验证成功

                                    if (!spHelper.getSharedPreferencesBoolean(getString(R.string.sp_text_configured), false)) {//没有设置向导,打开向导设置
openActivity(WizardActivity.class);
                                    } else {//已设置向导，进入手机防盗界面
                                        openActivity(BurglarAlarmActivity.class);
                                    }
                                } else {
                                    showMessage("密码错误！");
                                }
                                break;
                            case PASSWORD_NOT_EXIST:
                                String password = et_setup_pwd.getText().toString().trim();
                                if (password.equals(et_setup_pwd_config.getText().toString().trim())) {
                                    showMessage("保存成功");
                                    spHelper.setSharedPreferencesString(getString(R.string.sp_text_password),EncryptUtils.Encrypt(password));
                                } else {
                                    showMessage("两次密码不一致！");
                                }
                                break;
                            default:
                                return;
                        }
                    }
                })
                .show();
    }

}
