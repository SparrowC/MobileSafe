package com.kun.mobilesafe.activity;

import android.app.ActivityManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.beans.TaskInfo;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;
import com.kun.mobilesafe.utils.TaskInfoProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vonnie on 2015/11/27.
 */
public class TaskManagerActivity extends BaseActivity {
    private TextView tv_Memory,tv_taskPrompt;
    private ListView lv_taskManagerList;
    private ProgressBar pb_progressBar;
    private List<TaskInfo> mTaskInfoList,mUserTaskInfoList,mSysTaskInfoList;
    private TaskListAdapter mAdapter;
    private SharedPreferencesHelper spHelper;

    private static int LOAD_TASK_INFO_FINNISH =1;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==TaskManagerActivity.LOAD_TASK_INFO_FINNISH)
            {
                pb_progressBar.setVisibility(View.INVISIBLE);
                //tv_taskPrompt.setText("用户应用：" + mUserTaskInfoList.size() + "个");
                mAdapter=new TaskListAdapter();
                lv_taskManagerList.setAdapter(mAdapter);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyTitle("进程管理");
        addMainContent(R.layout.activity_task_manager);
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
        tv_Memory= (TextView) findViewById(R.id.tv_Memory);
        tv_taskPrompt= (TextView) findViewById(R.id.tv_taskPrompt);
        lv_taskManagerList= (ListView) findViewById(R.id.lv_taskManagerList);
        pb_progressBar= (ProgressBar) findViewById(R.id.pb_progressBar);

        spHelper=new SharedPreferencesHelper(this,getString(R.string.sp_name));
    }

    @Override
    protected void initListener() {
        lv_taskManagerList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mUserTaskInfoList != null && mSysTaskInfoList != null) {
                    if (firstVisibleItem > mUserTaskInfoList.size())
                        tv_taskPrompt.setText("系统进程：" + mSysTaskInfoList.size() + "个");
                    else
                        tv_taskPrompt.setText("用户进程：" + mUserTaskInfoList.size() + "个");
                }

            }
        });
        //item被点击
        lv_taskManagerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == mUserTaskInfoList.size()) {
                    return;
                }
                TaskInfo info;

                if (position < mUserTaskInfoList.size())
                    info = mUserTaskInfoList.get(position);
                else
                    info = mSysTaskInfoList.get(position - mUserTaskInfoList.size() - 1);
                info.setChecked(!info.isChecked());
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void fillData()
    {
        tv_Memory.setText("可用内存/总内存："
                + Formatter.formatFileSize(this, TaskInfoProvider.getAvailMemorySize(this)) + "/"
                + Formatter.formatFileSize(this, TaskInfoProvider.getTotalMemorySize(this)));
        mTaskInfoList=new ArrayList<>();
        mUserTaskInfoList=new ArrayList<TaskInfo>();
        mSysTaskInfoList=new ArrayList<TaskInfo>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mTaskInfoList=TaskInfoProvider.getTaskInfos(getApplicationContext());

                for(TaskInfo info:mTaskInfoList)
                {
                    if(info.isUserTask())
                        mUserTaskInfoList.add(info);
                    else
                        mSysTaskInfoList.add(info);
                }
                Message message=new Message();
                message.what=TaskManagerActivity.LOAD_TASK_INFO_FINNISH;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    public void selectAll(View view)
    {
        for(TaskInfo info:mUserTaskInfoList)
        {
            info.setChecked(true);
        }
        for(TaskInfo info:mSysTaskInfoList)
        {
            info.setChecked(true);
        }
        mAdapter.notifyDataSetChanged();
    }
    public void selectOpp(View view)
    {
        for(TaskInfo info:mUserTaskInfoList)
        {
            info.setChecked(!info.isChecked());
        }
        for(TaskInfo info:mSysTaskInfoList)
        {
            info.setChecked(!info.isChecked());
        }
        mAdapter.notifyDataSetChanged();
    }
    public void clean(View view)
    {
        int count=0;
        long size=0;
        ActivityManager am= (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<TaskInfo> cleanList=new ArrayList<>();
        for(TaskInfo info:mUserTaskInfoList)
        {
            if(info.isChecked())
            {
                if(!info.getName().equals(getString(R.string.app_name)))
                {
                    cleanList.add(info);
                    size+=info.getMemSize();
                    am.killBackgroundProcesses(info.getPackageName());
                }else
                    info.setChecked(false);
            }
        }
        count+=cleanList.size();
        mUserTaskInfoList.removeAll(cleanList);
        mTaskInfoList.removeAll(cleanList);
        cleanList=new ArrayList<>();
        for(TaskInfo info:mSysTaskInfoList)
        {
            if(info.isChecked())
            {
                cleanList.add(info);
                size+=info.getMemSize();
                am.killBackgroundProcesses(info.getPackageName());
            }
        }
        count+=cleanList.size();
        mSysTaskInfoList.removeAll(cleanList);
        mTaskInfoList.removeAll(cleanList);
        mAdapter.notifyDataSetChanged();
        Toast.makeText(this,"共清理"+count+"个应用,和"+Formatter.formatFileSize(this,size)+"的内存",Toast.LENGTH_SHORT).show();
        tv_Memory.setText("可用内存/总内存："
                + Formatter.formatFileSize(this, TaskInfoProvider.getAvailMemorySize(this)) + "/"
                + Formatter.formatFileSize(this, TaskInfoProvider.getTotalMemorySize(this)));
    }
    public void setting(View view)
    {
        openActivity(TaskSettingActivity.class);
    }



    private class TaskListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if(!spHelper.getSharedPreferencesBoolean(getString(R.string.sp_text_show_sys_task),false))
                return mUserTaskInfoList.size()+mSysTaskInfoList.size()+1;
            else
                return mUserTaskInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            if(position<mUserTaskInfoList.size())
                return mUserTaskInfoList.get(position);
            else
                return mSysTaskInfoList.get(position-mUserTaskInfoList.size()-1);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(position==mUserTaskInfoList.size())
            {
                TextView textView=new TextView(TaskManagerActivity.this);
                textView.setText("系统应用：" + mSysTaskInfoList.size() + "个");
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundColor(Color.GRAY);
                textView.setTextSize(20);
                return textView;
            }
            ViewHolder holder=new ViewHolder();
            if(convertView==null||convertView.getTag()==null)
            {
                convertView= LayoutInflater.from(TaskManagerActivity.this).inflate(R.layout.task_manage_list_item,null);
                holder.iv_icon= (ImageView) convertView.findViewById(R.id.iv_icon);
                holder.tv_appName= (TextView) convertView.findViewById(R.id.tv_appName);
                holder.tv_storeType= (TextView) convertView.findViewById(R.id.tv_storeType);
                holder.cb_killTask= (CheckBox) convertView.findViewById(R.id.cb_killTask);
                convertView.setTag(holder);
            }else {
                holder= (ViewHolder) convertView.getTag();
            }
            TaskInfo info=null;
            if(position<mUserTaskInfoList.size())
                info= mUserTaskInfoList.get(position);
            else
                info= mSysTaskInfoList.get(position-mUserTaskInfoList.size()-1);
            Drawable drawable=info.getIcon();
            holder.iv_icon.setImageDrawable(drawable);
            holder.tv_appName.setText(info.getName());
            holder.cb_killTask.setChecked(info.isChecked());
            holder.tv_storeType.setText("内存占用："+Formatter.formatFileSize(getApplicationContext(),info.getMemSize()));
            return convertView;
        }

        class ViewHolder{
            public TextView tv_appName,tv_storeType;
            public ImageView iv_icon;
            public CheckBox cb_killTask;
        }
    }
}
