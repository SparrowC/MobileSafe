package com.kun.mobilesafe.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vonnie on 2015/11/19.
 */
public class BlackNumberBeans {
    private int ID;
    private String number;
    private String name;
    /**
     * 黑名单的模式：0 不拦截，1 拦截电话，2 拦截短息，3 全部拦截
     */
    private int mode;


    public int getID() {
        return ID;
    }

    public void setID(int pID) {
        ID = pID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String pNumber) {
        number = pNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String pName) {
        name = pName;
    }


    public int getMode() {
        return mode;
    }

    public void setMode(int pMode) {
        mode = pMode;
    }
    public static List<String> BeansToNumberList(List<BlackNumberBeans> list)
    {
        List<String> numbers=new ArrayList<>();
        for(BlackNumberBeans bean :list)
        {
            numbers.add(new String(bean.getNumber()));
        }
        return numbers;
    }
}
