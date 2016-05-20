package com.kun.mobilesafe.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.BurglarAlarmActivity;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;

/**
 * Created by Vonnie on 2015/11/7.
 */
public class WizardFragment4 extends Fragment {
    private Button btn_finish;
    private SharedPreferencesHelper spHelper;
    private CheckBox cb_openAlarm;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.wizard_pager4,null);
        btn_finish= (Button) root.findViewById(R.id.btn_finish);
        spHelper=new SharedPreferencesHelper(getActivity(),getString(R.string.sp_name));
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!spHelper.getSharedPreferencesBoolean(getString(R.string.sp_text_configured), false))
                {//未设置向导
                    spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_configured), true);

                    Intent intent=new Intent(WizardFragment4.this.getActivity(), BurglarAlarmActivity.class);
                    WizardFragment4.this.getActivity().startActivity(intent);
                }
                WizardFragment4.this.getActivity().finish();
            }
        });

        cb_openAlarm= (CheckBox) root.findViewById(R.id.cb_openAlarm);
        cb_openAlarm.setChecked(spHelper.getSharedPreferencesBoolean(getString(R.string.sp_text_open_alarm),false));
        cb_openAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_open_alarm),isChecked);
            }
        });

        return root;
    }
}
