package com.kun.mobilesafe.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.kun.mobilesafe.beans.AppInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vonnie on 2015/11/26.
 */
public class AppInfoProvider {
    public static List<AppInfo> getAllAppInfos(Context context)
    {
        List<AppInfo> appInfoList=new ArrayList<>();
        PackageManager pm=context.getPackageManager();
        List<PackageInfo> packageInfos=pm.getInstalledPackages(0);
        for (PackageInfo info:packageInfos)
        {
            AppInfo appInfo=new AppInfo();
            appInfo.setPackageName(info.packageName);
            appInfo.setIcon(info.applicationInfo.loadIcon(pm));
            appInfo.setName(info.applicationInfo.loadLabel(pm).toString());
            int flags=info.applicationInfo.flags;
            if((flags&ApplicationInfo.FLAG_SYSTEM)==0)
            {
                appInfo.setUserApp(true);
            }else {
                appInfo.setUserApp(false);
            }
            if((flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0)
            {//手机内存
                appInfo.setInRom(true);
            }else {
                //手机外存
                appInfo.setInRom(false);
            }

            appInfoList.add(appInfo);
        }

        return appInfoList;
    }

    public static List<AppInfo> getUserAppInfos(List<AppInfo> infoList)
    {
        List<AppInfo> userAppInfos=new ArrayList<>();
        for(AppInfo appInfo:infoList)
        {
            if(appInfo.isUserApp())
                userAppInfos.add(appInfo);
        }
        return userAppInfos;
    }
    public static List<AppInfo> getSysAppInfos(List<AppInfo> infoList)
    {
        List<AppInfo> userAppInfos=new ArrayList<>();
        for(AppInfo appInfo:infoList)
        {
            if(!appInfo.isUserApp())
                userAppInfos.add(appInfo);
        }
        return userAppInfos;
    }
}
