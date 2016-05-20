package com.kun.mobilesafe.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.SeekBar;

import com.kun.mobilesafe.R;

/**
 * Created by cjj on 2015/9/14.
 */
public class SnailBar extends SeekBar {

    public SnailBar(Context context) {
        super(context);
        init();
    }


    public SnailBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SnailBar(Context context, AttributeSet attrs,  int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.setMax(100);
        this.setThumbOffset(dip2px(getContext(), 20));
        this.setBackgroundResource(R.drawable.sbg);
        int padding = dip2px(getContext(),(float)20);
        this.setPadding(padding, padding, padding, padding);
        this.setProgressDrawable(getResources().getDrawable(R.drawable.snailbar_define_style));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        AnimationDrawable drawable = (AnimationDrawable)this.getThumb();
        drawable.start();
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


}
