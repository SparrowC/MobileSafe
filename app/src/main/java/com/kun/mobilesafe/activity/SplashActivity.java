package com.kun.mobilesafe.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.R;
import com.kun.mobilesafe.beans.UpdateBean;
import com.kun.mobilesafe.database.LockAppDBDAO;
import com.kun.mobilesafe.utils.HTTPUtils;
import com.kun.mobilesafe.utils.JsonUtils;
import com.kun.mobilesafe.utils.ServiceUtils;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SplashActivity extends BaseActivity {

    private TextView tvVersionCtrl;
    private final int UPDATE=0;
    private final int NO_UPDATE=1;
    private final int NETWORK_ERROR=2;
    private final int JSON_ERROR=3;
    private SharedPreferences sp;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what)
            {
                case UPDATE:
                    ShowUpdateDialog(msg.getData());
                    break;
                case NO_UPDATE:
                    enterHome();
                    break;
                case NETWORK_ERROR:
                    showMessage("网络不佳");
                    enterHome();
                    break;
                case JSON_ERROR:
                    showMessage("服务器在维护中");
                    enterHome();
                    break;
            }
        }
    };

    private void ShowUpdateDialog(Bundle data) {
        UpdateBean bean= (UpdateBean) data.get("updateInfo");

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("软件更新")
                .setMessage("软件有更新，是否更新")
                .setNegativeButton("下次再说", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        enterHome();
                    }
                })
                .setPositiveButton("确定更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        enterHome();
                    }
                })
                .show();
    }

    private void enterHome() {
        openActivity(HomeActivity.class);
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initWidget();
        initListener();
        copyDB("address.db");
        copyDB("antivirus.db");
        if(sp.getBoolean(getString(R.string.sp_text_update),false))
            CheckUpdate();
        else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    enterHome();
                }
            }, 1000);
        }

    }
    private void CheckUpdate()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg=new Message();
                try {
                    String jsonStr= HTTPUtils.getJsonString(getResources().getString(R.string.updateURL));
                    UpdateBean bean=JsonUtils.getUpdateBean(SplashActivity.this,jsonStr);
                    if(bean.getVersion().equals(getVersionName()))
                    {
                        msg.what=NO_UPDATE;
                        try {
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else
                    {
                        msg.what=UPDATE;
                        Bundle data=new Bundle();
                        data.putSerializable("updateInfo",bean);
                        msg.setData(data);
                    }
                    mHandler.sendMessage(msg);
                } catch (IOException e) {
                    msg.what=NETWORK_ERROR;
                    e.printStackTrace();
                }catch (JSONException e) {
                    msg.what=JSON_ERROR;
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * copy to file path="data/data/com.kun.mobilesafe/files/address.db";
     */
    private void copyDB(String fileName)
    {
        try {
            File file =new File(getFilesDir(),fileName);
            if(file.exists()&&file.length()>0)
                return;
            InputStream is=getAssets().open(fileName);

            FileOutputStream fos=new FileOutputStream(file);
            byte[] buffer=new byte[1024];
            int len=0;
            while ((len=is.read(buffer))!=-1)
            {
                fos.write(buffer,0,len);
            }
            is.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void initWidget() {
        tvVersionCtrl= (TextView) findViewById(R.id.tvVersionCtrl);
        tvVersionCtrl.setText("版本号：" + getVersionName());
        sp=getSharedPreferences("config",MODE_PRIVATE);
    }

    @Override
    protected void initListener() {

    }

    private String getVersionName()
   {
       String name="";
       try {
           name=getPackageManager().getPackageInfo(getPackageName(),0).versionName;
       } catch (PackageManager.NameNotFoundException e) {
           e.printStackTrace();
       }finally {
           return name;
       }
   }


}
