package com.kun.mobilesafe.beans;

import java.io.Serializable;

public class Contact implements Serializable{
    private String name;
    private String phoneNum;

    public Contact(String name, String phoneNum) {
        this.name = name;
        this.phoneNum = phoneNum;
    }

    public Contact() {
        this.name = null;
        this.phoneNum = null;
    }


    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
