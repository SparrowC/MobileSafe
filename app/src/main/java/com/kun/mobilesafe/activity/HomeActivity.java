package com.kun.mobilesafe.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.kun.mobilesafe.Adapter.HomeGridViewAdapter;
import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.beans.FunctionBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kun on 2015/11/5.
 */
public class HomeActivity extends BaseActivity {
    private GridView gvHomeGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initWidget();
        initListener();
    }

    @Override
    protected void initWidget() {
        gvHomeGrid= (GridView) findViewById(R.id.gvHomeGrid);
        gvHomeGrid.setAdapter(new HomeGridViewAdapter(this, getFunction()));
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
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        openActivity(SettingActivity.class);
                        break;
                }
            }
        });
    }

}
