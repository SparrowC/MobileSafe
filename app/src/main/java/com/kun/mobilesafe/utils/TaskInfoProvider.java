package com.kun.mobilesafe.utils;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Debug;

import com.kun.mobilesafe.beans.TaskInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vonnie on 2015/11/28.
 */
public class TaskInfoProvider {
    public static List<TaskInfo> getTaskInfos(Context context)
    {
        List<TaskInfo> taskInfoList=new ArrayList<>();
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm=context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos=am.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo info:appProcessInfos)
        {
            TaskInfo taskInfo=new TaskInfo();
            String packName=info.processName;
            PackageInfo packageInfo;
            try {
                packageInfo =pm.getPackageInfo(packName,0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            Debug.MemoryInfo[] memoryInfos=am.getProcessMemoryInfo(new int[]{info.pid});
            long memorySize=memoryInfos[0].getTotalPss()*1024;
            Drawable icon=packageInfo.applicationInfo.loadIcon(pm);
            String name=  packageInfo.applicationInfo.loadLabel(pm).toString();
            int flags=packageInfo.applicationInfo.flags;

            taskInfo.setUserTask((flags& ApplicationInfo.FLAG_SYSTEM)==0);
//            if((flags& ApplicationInfo.FLAG_SYSTEM)==0)
//            {//用户应用
//                taskInfo.setUserTask(true);
//            }else {//系统应用
//                taskInfo.setUserTask(false);
//            }
            taskInfo.setPackageName(packName);
            taskInfo.setIcon(icon);
            taskInfo.setName(name);
            taskInfo.setMemSize(memorySize);
            taskInfoList.add(taskInfo);
        }
        return taskInfoList;
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static long getTotalMemorySize(Context context)
    {
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        return info.totalMem;
    }
    public static long getAvailMemorySize(Context context)
    {
        ActivityManager am= (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info=new ActivityManager.MemoryInfo();
        am.getMemoryInfo(info);
        return info.availMem;
    }
}
