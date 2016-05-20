package com.kun.mobilesafe.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StatFs;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.beans.AppInfo;
import com.kun.mobilesafe.utils.AppInfoProvider;
import com.kun.mobilesafe.utils.DensityUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vonnie on 2015/11/26.
 */
public class AppManagerActivity extends BaseActivity {

    private TextView tv_memorySize,tv_SDCardSize,tv_appPrompt;
    private ListView lv_appManagerList;
    private ProgressBar pb_progressBar;
    private List<AppInfo> mAppInfoList,mUserAppInfoList,mSysAppInfoList;
    private PopupWindow mPopupWindow;
    private static int LOAD_APPINFO_FINNISH=1;
    private AppListAdapter mAdapter;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==AppManagerActivity.LOAD_APPINFO_FINNISH)
            {
                pb_progressBar.setVisibility(View.INVISIBLE);
                mAdapter=new AppListAdapter();
                lv_appManagerList.setAdapter(mAdapter);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle("应用管理");
        addMainContent(R.layout.activity_app_manager);
        initWidget();
        initListener();
        initBackButton(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        fillData();
    }

    @Override
    protected void initWidget() {
        tv_memorySize= (TextView) findViewById(R.id.tv_memorySize);
        tv_SDCardSize= (TextView) findViewById(R.id.tv_SDCardSize);
        //long memorySize=getAvailSpace(Environment.getDataDirectory().getAbsolutePath());


        pb_progressBar= (ProgressBar) findViewById(R.id.pb_progressBar);
        tv_appPrompt= (TextView) findViewById(R.id.tv_appPrompt);
        lv_appManagerList= (ListView) findViewById(R.id.lv_appManagerList);

    }

    @Override
    protected void initListener() {
        lv_appManagerList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mUserAppInfoList != null && mSysAppInfoList != null) {
                    if (firstVisibleItem > mUserAppInfoList.size())
                        tv_appPrompt.setText("系统应用：" + mSysAppInfoList.size() + "个");
                    else
                        tv_appPrompt.setText("用户应用：" + mUserAppInfoList.size() + "个");
                }
                dismissPopupWindow();
            }
        });

        lv_appManagerList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo = new AppInfo();
                if (position < mUserAppInfoList.size()) {
                    appInfo = mUserAppInfoList.get(position);
                } else {
                    if (position > mUserAppInfoList.size()) {
                        appInfo = mSysAppInfoList.get(position - mUserAppInfoList.size() - 1);
                    } else
                        return true;
                }
                dismissPopupWindow();

                View contentView = LayoutInflater.from(AppManagerActivity.this).inflate(R.layout.popun_window, null);
                LinearLayout ll_openApp = (LinearLayout) contentView.findViewById(R.id.ll_openApp);
                LinearLayout ll_uninstallApp = (LinearLayout) contentView.findViewById(R.id.ll_uninstallApp);
                LinearLayout ll_shareApp = (LinearLayout) contentView.findViewById(R.id.ll_shareApp);

                ll_openApp.setOnClickListener(new MyClickListener(appInfo));
                ll_uninstallApp.setOnClickListener(new MyClickListener(appInfo));
                ll_shareApp.setOnClickListener(new MyClickListener(appInfo));
                mPopupWindow = new PopupWindow(contentView, -2, -2);

                int[] location = new int[2];
                view.getLocationInWindow(location);
                mPopupWindow.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, DensityUtil.dip2px(getApplicationContext(), 80), location[1]);
                return true;
            }
        });
    }

    private void fillData()
    {
        long sdSize=getAvailSpace(Environment.getExternalStorageDirectory().getAbsolutePath());
        tv_memorySize.setText("内存可用：" + getAvailMemory());
        tv_SDCardSize.setText("SD卡可用：" + Formatter.formatFileSize(this, sdSize));
        pb_progressBar.setVisibility(View.VISIBLE);
        mAppInfoList=new ArrayList<>();
        mUserAppInfoList=new ArrayList<>();
        mSysAppInfoList=new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mAppInfoList=AppInfoProvider.getAllAppInfos(AppManagerActivity.this);
                for(AppInfo info:mAppInfoList)
                {
                    if(info.isUserApp())
                        mUserAppInfoList.add(info);
                    else
                        mSysAppInfoList.add(info);
                }
//                mUserAppInfoList=AppInfoProvider.getUserAppInfos(mAppInfoList);
//                mSysAppInfoList=AppInfoProvider.getSysAppInfos(mAppInfoList);
                Message message=new Message();
                message.what=AppManagerActivity.LOAD_APPINFO_FINNISH;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 获取路径的可用空间
     * @param path
     * @return
     */
    private long getAvailSpace(String path)
    {
        StatFs statFs=new StatFs(path);
        //statFs.getBlockCount();//获取分区个数
        long size=statFs.getBlockSize();//获取分区大小
        long count=statFs.getAvailableBlocks();
        return size*count;
    }
    private String getAvailMemory() {// 获取android当前可用内存大小

        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
    }

    private void dismissPopupWindow()
    {
        if(mPopupWindow!=null&&mPopupWindow.isShowing())
        {
            mPopupWindow.dismiss();
            mPopupWindow=null;
        }
    }
    private void  startApp(AppInfo info)
    {
        PackageManager pm=getPackageManager();
        Intent intent=pm.getLaunchIntentForPackage(info.getPackageName());
        if(intent!=null)
            startActivity(intent);
        else
            Toast.makeText(getApplicationContext(),"对不起，无法打开此应用！",Toast.LENGTH_SHORT).show();
    }
    private void  uninstallApp(AppInfo info)
    {
        Intent intent=new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setAction("android.intent.action.DELETE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setData(Uri.parse("package:" + info.getPackageName()));
        startActivity(intent);

    }
    private void  shareApp(AppInfo info)
    {
        Intent intent =new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "推荐一款软件，名字叫" + info.getName());
        startActivity(intent);
    }



    class AppListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mUserAppInfoList.size()+mSysAppInfoList.size()+1;
        }

        @Override
        public Object getItem(int position) {
            if(position<mUserAppInfoList.size())
                return mUserAppInfoList.get(position);
            else
                return mSysAppInfoList.get(position-mUserAppInfoList.size()-1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(position==mUserAppInfoList.size())
            {
                TextView textView=new TextView(AppManagerActivity.this);
                textView.setText("系统应用：" + mSysAppInfoList.size() + "个");
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextSize(20);
                return textView;
            }
            ViewHolder holder=new ViewHolder();
            if(convertView==null||convertView.getTag()==null)
            {
                convertView= LayoutInflater.from(AppManagerActivity.this).inflate(R.layout.app_manage_list_item,null);
                holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_appName= (TextView) convertView.findViewById(R.id.tv_appName);
                holder.tv_storeType= (TextView) convertView.findViewById(R.id.tv_storeType);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            AppInfo info=null;
            if(position<mUserAppInfoList.size())
                info= mUserAppInfoList.get(position);
            else
                info= mSysAppInfoList.get(position-mUserAppInfoList.size()-1);
            Drawable drawable=info.getIcon();
            holder.iv_icon.setImageDrawable(drawable);
            holder.tv_appName.setText(info.getName());
            String text="";
            if(info.isInRom())
                text="手机内存";
            else
                text="外部存储";
            holder.tv_storeType.setText(text);
            return convertView;
        }

        class ViewHolder{
            public TextView tv_appName,tv_storeType;
            public ImageView iv_icon;
        }
    }

    private class MyClickListener implements View.OnClickListener {
        private AppInfo info;

        public MyClickListener(AppInfo info) {
            this.info = info;
        }

        @Override
        public void onClick(View v) {
            dismissPopupWindow();
            switch (v.getId())
            {
                case R.id.ll_openApp:
                    startApp(info);
                    break;
                case R.id.ll_uninstallApp:
                    uninstallApp(info);
                    break;
                case R.id.ll_shareApp:
                    shareApp(info);
                    break;
            }

        }
    }
}
