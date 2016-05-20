package com.kun.mobilesafe.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.utils.EncryptUtils;
import com.kun.mobilesafe.utils.VirusQueryUtils;
import com.kun.mobilesafe.view.SnailBar;

import java.util.List;

/**
 * Created by Vonnie on 2015/12/5.
 */
public class AntiVirusActivity extends BaseActivity {
    private static final int SANNING = 1;
    private static final int FINISH = 2;
    private ProgressBar sb_progress;
    private ImageView iv_scan;
    private TextView tv_scanningInfo,tv_prompt;
    private int progress;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case SANNING:
                    ScanInfo scanInfo= (ScanInfo) msg.obj;
                    tv_prompt.setText("正在扫描"+scanInfo.appname);
                    String text="";
                    if(scanInfo.isVirus)
                    {
                        text=scanInfo.appname+"是病毒 "+scanInfo.desc;
                    }else {
                        text=scanInfo.appname+"安全 ";
                    }
                    tv_scanningInfo.setText(text+"\n"+tv_scanningInfo.getText());
                    break;
                case FINISH:
                    tv_prompt.setText("扫描完成");
                    iv_scan.clearAnimation();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMainContent(R.layout.activity_anti_virus);
        init(this,"手机杀毒");

    }

    @Override
    protected void initWidget() {
        sb_progress= (ProgressBar) findViewById(R.id.sb_progress);
        iv_scan= (ImageView) findViewById(R.id.iv_scan);
        tv_scanningInfo= (TextView) findViewById(R.id.tv_scanningInfo);
        tv_prompt= (TextView) findViewById(R.id.tv_prompt);

        sb_progress.setProgress(10);
        Scanner();
    }

    @Override
    protected void initListener() {

    }

    private void Scanner() {
        final PackageManager pm=getPackageManager();
        final List<PackageInfo> infoList=pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
                + PackageManager.GET_SIGNATURES);
        sb_progress.setMax(infoList.size());
        progress=0;
        RotateAnimation ra=new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(1000);
        ra.setRepeatCount(Animation.INFINITE);
        iv_scan.startAnimation(ra);
        new Thread(){
            @Override
            public void run() {
                for (PackageInfo info:infoList)
                {
                    ScanInfo scanInfo=new ScanInfo();
                    scanInfo.packname=info.packageName;
                    scanInfo.appname=info.applicationInfo.loadLabel(pm).toString();
                    String md5= EncryptUtils.Encrypt(info.signatures[0].toCharsString());
                    String result= VirusQueryUtils.isVirus(md5);
                    if(result==null)
                    {//不是病毒
                        scanInfo.isVirus=false;
                        scanInfo.desc=null;
                    }else {
                        scanInfo.isVirus=true;
                        scanInfo.desc=result;
                    }
                    progress++;
                    sb_progress.setProgress(progress);
                    Message msg=new Message();
                    msg.what=SANNING;
                    msg.obj=scanInfo;
                    mHandler.sendMessage(msg);
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Message msg=new Message();
                msg.what= FINISH;
                mHandler.sendMessage(msg);
            }
        }.start();

    }

    class ScanInfo{
        String packname;
        String appname;
        boolean isVirus;
        String desc;
    }

}
