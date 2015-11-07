package com.kun.mobilesafe.utils;

import android.content.Context;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.beans.UpdateBean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Kun on 2015/11/5.
 */
public class JsonUtils {

    public static UpdateBean getUpdateBean(Context context,String jsonStr) throws JSONException {
        JSONObject jsonObject=new JSONObject(jsonStr);
        JSONObject updateInfo=jsonObject.getJSONObject(StringUtils.getStringByResID(context, R.string.updateInfo));
        UpdateBean bean=new UpdateBean();
        bean.setVersion(updateInfo.getString(StringUtils.getStringByResID(context, R.string.jsonVersion)));
        bean.setDescribe(updateInfo.getString(StringUtils.getStringByResID(context, R.string.jsonDescribe)));
        bean.setDownloadURL(updateInfo.getString(StringUtils.getStringByResID(context, R.string.jsonDownload)));
        return bean;
    }
}
