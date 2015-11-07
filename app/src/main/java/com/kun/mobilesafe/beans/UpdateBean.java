package com.kun.mobilesafe.beans;

import java.io.Serializable;

/**
 * Created by Kun on 2015/11/5.
 */
public class UpdateBean implements Serializable {
    private String version;
    private String describe;
    private String downloadURL;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }
}
