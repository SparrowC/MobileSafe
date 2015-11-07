package com.kun.mobilesafe.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Kun on 2015/11/5.
 */
public class HTTPUtils {
    public static String getJsonString(String url) throws IOException {
        URL mURL=new URL(url);
        HttpURLConnection conn= (HttpURLConnection) mURL.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);
        if(conn.getResponseCode()==200)
        {
            InputStream inputStream=conn.getInputStream();
            return getStringFromInputStream(inputStream);
        }
        return null;
    }

    private static String getStringFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
        String result="",temp;
        while ((temp=bufferedReader.readLine())!=null)
        {
            result+=temp;
        }
        return result;
    }
}
