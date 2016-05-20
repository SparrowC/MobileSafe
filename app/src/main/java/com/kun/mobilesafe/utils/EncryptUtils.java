package com.kun.mobilesafe.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Vonnie on 2015/11/8.
 * 做md5加密的utils
 */
public class EncryptUtils {
    public static String Encrypt(String str)
    {
        String result="";
        byte[] buf;
        MessageDigest digest;
        try {
            digest=MessageDigest.getInstance("md5");
            buf=digest.digest(str.getBytes());
            for(byte b:buf)
            {
                int num=b&0xff;
                String s=Integer.toString(num);
                if(s.length()==1)
                {
                    result+='0';
                }
                result+=s;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
        //Log.d("TAG",result);
        return result;
    }
}
