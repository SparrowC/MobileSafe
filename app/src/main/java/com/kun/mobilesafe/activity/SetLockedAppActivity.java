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
import com.kun.mobilesafe.database.LockAppDBDAO;
import com.kun.mobilesafe.utils.AppInfoProvider;
import com.kun.mobilesafe.utils.DensityUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Vonnie on 2015/11/26.
 */
public class SetLockedAppActivity extends BaseActivity {

    private TextView tv_memorySize,tv_SDCardSize,tv_appPrompt;
    private ListView lv_appManagerList;
    private ProgressBar pb_progressBar;
    private List<AppInfo> mAppInfoList,mUserAppInfoList,mSysAppInfoList;
    private List<String> packnameList;
    private LockAppDBDAO dao;
    private static int LOAD_APPINFO_FINNISH=1;
    private AppListAdapter mAdapter;

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what== SetLockedAppActivity.LOAD_APPINFO_FINNISH)
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
        setMyTitle("隐私保护");
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

        pb_progressBar= (ProgressBar) findViewById(R.id.pb_progressBar);
        tv_appPrompt= (TextView) findViewById(R.id.tv_appPrompt);
        lv_appManagerList= (ListView) findViewById(R.id.lv_appManagerList);
        dao=new LockAppDBDAO(this);
        packnameList=dao.QueryAllLockApp();
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

            }
        });

        lv_appManagerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AppInfo appInfo;
                if (position < mUserAppInfoList.size()) {
                    appInfo = mUserAppInfoList.get(position);
                } else {
                    if (position > mUserAppInfoList.size()) {
                        appInfo = mSysAppInfoList.get(position - mUserAppInfoList.size() - 1);
                    } else
                        return;
                }
                SetLockedAppActivity.AppListAdapter.ViewHolder holder = (SetLockedAppActivity.AppListAdapter.ViewHolder) view.getTag();
                appInfo.setLocked(!appInfo.isLocked());
                if (appInfo.isLocked()) {
                    holder.iv_lockApp.setImageResource(R.drawable.lock);
                    dao.AddLockApp(appInfo.getPackageName());
                } else {
                    holder.iv_lockApp.setImageResource(R.drawable.unlock);
                    dao.DeleteLockApp(appInfo.getPackageName());
                }
                packnameList = dao.QueryAllLockApp();
                Intent intentBroadcast = new Intent();
                intentBroadcast.setAction("com.kun.mobilesafe.LOCKED_APP_CHANGED");
                sendBroadcast(intentBroadcast);
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
                mAppInfoList=AppInfoProvider.getAllAppInfos(SetLockedAppActivity.this);
                AppInfo selfInfo=null;
                for(AppInfo info:mAppInfoList)
                {
                    if(info.getPackageName().equals(getString(R.string.app_packageName)))
                    {
                        selfInfo=info;
                        continue;
                    }
                    //设置锁
                    if (packnameList.contains(info.getPackageName()))
                        info.setLocked(true);
                    else
                        info.setLocked(false);
                    //对应用分类
                    if(info.isUserApp())
                        mUserAppInfoList.add(info);
                    else
                        mSysAppInfoList.add(info);
                }
                //移除自身
                if(selfInfo!=null)
                    mAppInfoList.remove(selfInfo);

                Message message=new Message();
                message.what= SetLockedAppActivity.LOAD_APPINFO_FINNISH;
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

        return Formatter.formatFileSize(getBaseContext(), mi.availMem);// 将获取的内存大小规格化
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
                TextView textView=new TextView(SetLockedAppActivity.this);
                textView.setText("系统应用：" + mSysAppInfoList.size() + "个");
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextSize(20);
                return textView;
            }
            ViewHolder holder=new ViewHolder();
            if(convertView==null||convertView.getTag()==null)
            {
                convertView= LayoutInflater.from(SetLockedAppActivity.this).inflate(R.layout.set_locked_app_list_item,null);
                holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_appName= (TextView) convertView.findViewById(R.id.tv_appName);
                holder.tv_storeType= (TextView) convertView.findViewById(R.id.tv_storeType);
                holder.iv_lockApp= (ImageView) convertView.findViewById(R.id.iv_lockApp);
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

            if (info.isLocked())
                holder.iv_lockApp.setImageResource(R.drawable.lock);
            else
                holder.iv_lockApp.setImageResource(R.drawable.unlock);
            return convertView;
        }

        class ViewHolder{
            public TextView tv_appName,tv_storeType;
            public ImageView iv_icon,iv_lockApp;
        }
    }

}
