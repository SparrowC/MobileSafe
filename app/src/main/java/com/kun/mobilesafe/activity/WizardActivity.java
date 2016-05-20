package com.kun.mobilesafe.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.fragment.WizardFragment1;
import com.kun.mobilesafe.fragment.WizardFragment2;
import com.kun.mobilesafe.fragment.WizardFragment3;
import com.kun.mobilesafe.fragment.WizardFragment4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kun on 2015/11/7.
 */
public class WizardActivity extends FragmentActivity {
    private ViewPager wizard_pager;
    private ImageView scroll_bar;
    private List<Fragment> mFragmentList;
    private TextView tv_title;
    private int curPagerItem;
    private float mWidth;
    private String[] title;
    WizardFragment1 fragment1=new WizardFragment1();
    WizardFragment2 fragment2=new WizardFragment2();
    WizardFragment3 fragment3=new WizardFragment3();
    WizardFragment4 fragment4=new WizardFragment4();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard);
        initWidget();
        initListener();
    }

    protected void initWidget() {
        //初始化

        wizard_pager= (ViewPager) findViewById(R.id.wizard_pager);
        mFragmentList=new ArrayList<>();

        mFragmentList.add(fragment1);
        mFragmentList.add(fragment2);
        mFragmentList.add(fragment3);
        mFragmentList.add(fragment4);

        scroll_bar= (ImageView) findViewById(R.id.scroll_bar);
        DisplayMetrics metrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width=metrics.widthPixels;
        ViewGroup.LayoutParams params=scroll_bar.getLayoutParams();
        mWidth=params.width=width/mFragmentList.size();
        scroll_bar.setLayoutParams(params);


        wizard_pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        wizard_pager.setCurrentItem(0);
        curPagerItem=0;

        tv_title= (TextView) findViewById(R.id.tv_title);
        title=getResources().getStringArray(R.array.wizard_pager_title);
        tv_title.setText(title[0]);
    }

    protected void initListener() {
        wizard_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if(curPagerItem==2&&position!=2)
                {
                    if(!fragment3.SavePhoneNumber())
                    {
                        wizard_pager.setCurrentItem(2);
                        Toast.makeText(WizardActivity.this,"安全号码不能为空",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                if(curPagerItem==1&&position!=1)
                {
                    if(!fragment2.BindSIM())
                    {
                        wizard_pager.setCurrentItem(1);
                        Toast.makeText(WizardActivity.this,"请绑定SIM卡",Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                TranslateAnimation animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF,curPagerItem,Animation.RELATIVE_TO_SELF,position,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
                curPagerItem=position;
                animation.setDuration(100);
                animation.setFillAfter(true);
                scroll_bar.startAnimation(animation);
                tv_title.setText(title[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
