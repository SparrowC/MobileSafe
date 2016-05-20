package com.kun.mobilesafe.beans;

import android.graphics.drawable.Drawable;

/**
 * Created by Vonnie on 2015/11/26.
 */
public class AppInfo {
    private Drawable icon;
    private String name;
    private String packageName;
    private boolean inRom;
    private boolean userApp;
    private boolean locked;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public boolean isInRom() {
        return inRom;
    }

    public void setInRom(boolean inRom) {
        this.inRom = inRom;
    }

    public boolean isUserApp() {
        return userApp;
    }

    public void setUserApp(boolean userApp) {
        this.userApp = userApp;
    }


    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    @Override
    public String toString() {
        return "AppInfo{" +
                "icon=" + icon +
                ", name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", inRom=" + inRom +
                ", userApp=" + userApp +
                ", locked=" + locked +
                '}';
    }
}
