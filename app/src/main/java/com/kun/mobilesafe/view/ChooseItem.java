package com.kun.mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kun.mobilesafe.R;

/**
 * Created by Kun on 2015/11/5.
 */
public class ChooseItem extends RelativeLayout {
    private TextView cbi_tvSettingCue, cbi_tvSettingTitleText;
    private String mTitleText, mDefaultText;
    private View mView;

    private void initView(Context context) {
        mView=View.inflate(context, R.layout.choose_item,this);
        //this.addView(View.inflate(context, R.layout.setting_item,null));
        cbi_tvSettingTitleText = (TextView) this.findViewById(R.id.cbi_tvSettingTitleText);
        cbi_tvSettingCue= (TextView) this.findViewById(R.id.cbi_tvSettingCue);

    }

    public ChooseItem(Context context) {
        super(context);
        initView(context);
    }

    public ChooseItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChooseItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.ChooseItem,defStyleAttr,R.style.AppTheme);
        mTitleText=array.getString(R.styleable.ChooseItem_titleText);
        mDefaultText =array.getString(R.styleable.ChooseItem_defaultText);
        initText(mTitleText, mDefaultText);
        array.recycle();
    }

    private void initText(String mTitleText, final String mDefaultText) {
        cbi_tvSettingTitleText.setText(mTitleText);
        cbi_tvSettingCue.setText(mDefaultText);
    }

    //定义自定义方法
    public void setDefaultText(String text)
    {
        cbi_tvSettingCue.setText(text);
    }

}
