package com.kun.mobilesafe.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kun.mobilesafe.R;

/**
 * Created by Kun on 2015/11/5.
 */
public class CheckBoxItem extends RelativeLayout {
    private TextView cbi_tvSettingCue, cbi_tvSettingTitleText;
    private CheckBox cbi_checkbox;
    private String mTitleText, mCheckedCueText,mUncheckedCueText;
    private OnCheckBoxItemCheckedListener checkBoxItemCheckedListener=null;

    public interface OnCheckBoxItemCheckedListener{
        void onChecked();
        void onUnchecked();
    }
    private View mView;
    private void initView(Context context) {
        mView=View.inflate(context, R.layout.setting_item,this);
        //this.addView(View.inflate(context, R.layout.setting_item,null));
        cbi_tvSettingTitleText = (TextView) this.findViewById(R.id.cbi_tvSettingTitleText);
        cbi_tvSettingCue= (TextView) this.findViewById(R.id.cbi_tvSettingCue);
        cbi_checkbox= (CheckBox) this.findViewById(R.id.cbi_checkbox);
        //checkBoxItemCheckedListener= (OnCheckBoxItemCheckedListener) this;
    }

    public CheckBoxItem(Context context) {
        super(context);
        initView(context);
    }

    public CheckBoxItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        //initView(context);
    }

    public CheckBoxItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
        TypedArray array=context.obtainStyledAttributes(attrs,R.styleable.CheckBoxItem,defStyleAttr,R.style.AppTheme);
        mTitleText=array.getString(R.styleable.CheckBoxItem_titleText);
        mCheckedCueText =array.getString(R.styleable.CheckBoxItem_checkedCueText);
        mUncheckedCueText=array.getString(R.styleable.CheckBoxItem_uncheckedCueText);
        initText(mTitleText, mUncheckedCueText, mCheckedCueText);
        array.recycle();
    }

    private void initText(String mTitleText, final String mUncheckedCueText, final String mCheckedCueText) {
        cbi_tvSettingTitleText.setText(mTitleText);
        cbi_tvSettingCue.setText(mUncheckedCueText);
        mView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cbi_checkbox.setChecked(!cbi_checkbox.isChecked());
                if (cbi_checkbox.isChecked()) {
                    cbi_tvSettingCue.setText(mCheckedCueText);
                    if(checkBoxItemCheckedListener!=null)
                        checkBoxItemCheckedListener.onChecked();
                } else {
                    cbi_tvSettingCue.setText(mUncheckedCueText);
                    if(checkBoxItemCheckedListener!=null)
                        checkBoxItemCheckedListener.onUnchecked();
                }
            }
        });
    }

    //封装的 调用自定义控件时使用的方法

    /**
     * 为CheckBoxItem添加监听器的方法
     * @param listener
     */
    public void setOnCheckedListener(OnCheckBoxItemCheckedListener listener)
    {
        checkBoxItemCheckedListener=listener;
    }

    /**
     * 设置CheckBoxItem是否选中
     * @param b 表示CheckBoxItem是否选中，true代表选中，false代表不选中
     */
    public void setChecked(boolean b)
    {
        cbi_checkbox.setChecked(b);
        if (b) {
            cbi_tvSettingCue.setText(mCheckedCueText);
        } else {
            cbi_tvSettingCue.setText(mUncheckedCueText);
        }
    }
    public boolean isChecked()
    {
        return  cbi_checkbox.isChecked();
    }
}
