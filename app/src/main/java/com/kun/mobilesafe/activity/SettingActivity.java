package com.kun.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.SettingInjectorService;
import android.os.Bundle;
import android.view.View;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.service.BlackNumberService;
import com.kun.mobilesafe.service.NumberAddressQueryService;
import com.kun.mobilesafe.utils.ServiceUtils;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;
import com.kun.mobilesafe.view.CheckBoxItem;
import com.kun.mobilesafe.view.ChooseItem;

/**
 * Created by Kun on 2015/11/5.
 */
public class SettingActivity extends BaseActivity {

    private CheckBoxItem cbi_update,cbi_numberAddressQuery,cbi_blackNumber;
    private ChooseItem ci_address_bkg,ci_address_style;
    private SharedPreferencesHelper spHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_setting);
        addMainContent(R.layout.activity_setting);
        setMyTitle("设置中心");
        initWidget();
        initListener();
        initBackButton(this);
    }

    @Override
    protected void initWidget() {
        cbi_update= (CheckBoxItem) findViewById(R.id.cbi_update);
        cbi_numberAddressQuery= (CheckBoxItem) findViewById(R.id.cbi_numberAddressQuery);
        cbi_blackNumber= (CheckBoxItem) findViewById(R.id.cbi_blackNumber);
//        ci_address_style= (ChooseItem) findViewById(R.id.ci_address_style);
        ci_address_bkg= (ChooseItem) findViewById(R.id.ci_address_bkg);

        spHelper=new SharedPreferencesHelper(this,getString(R.string.sp_name));
        boolean b=spHelper.getSharedPreferencesBoolean(getString(R.string.sp_text_update),true);
        cbi_update.setChecked(b);
        cbi_numberAddressQuery.setChecked(ServiceUtils.isServiceExist(this,getString(R.string.NumberAddressQueryServiceName)));
        cbi_blackNumber.setChecked(ServiceUtils.isServiceExist(this,getString(R.string.BlackNumberServiceName)));

    }

    @Override
    protected void initListener() {
        //是否检测更新的设置
        cbi_update.setOnCheckedListener(new CheckBoxItem.OnCheckBoxItemCheckedListener() {
            @Override
            public void onChecked() {
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_update), true);
            }

            @Override
            public void onUnchecked() {
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_update), false);
            }
        });

        //来电归属地设置
        final Intent showAddressIntent=new Intent(SettingActivity.this, NumberAddressQueryService.class);
        cbi_numberAddressQuery.setOnCheckedListener(new CheckBoxItem.OnCheckBoxItemCheckedListener() {
            @Override
            public void onChecked() {
                startService(showAddressIntent);
            }

            @Override
            public void onUnchecked() {
                stopService(showAddressIntent);
            }
        });

        //黑名单设置
        final Intent blackNumberIntent=new Intent(SettingActivity.this, BlackNumberService.class);
        cbi_blackNumber.setOnCheckedListener(new CheckBoxItem.OnCheckBoxItemCheckedListener() {
            @Override
            public void onChecked() {
                startService(blackNumberIntent);
            }

            @Override
            public void onUnchecked() {
                stopService(blackNumberIntent);
            }
        });

        final String[] bkgs=getResources().getStringArray(R.array.toast_bkgs);
        final int curBKG=spHelper.getSharedPreferencesInt(getString(R.string.sp_text_bkg),0);
        ci_address_bkg.setDefaultText(bkgs[curBKG]);
        ci_address_bkg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(SettingActivity.this);
                final AlertDialog dlg=builder.create();
                builder.setTitle(getString(R.string.setBkgTitle))
                        .setSingleChoiceItems(bkgs, curBKG, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                spHelper.setSharedPreferencesInt(getString(R.string.sp_text_bkg), which);
                                ci_address_bkg.setDefaultText(bkgs[which]);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        });
    }
}
