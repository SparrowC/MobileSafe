package com.kun.mobilesafe;

import android.test.AndroidTestCase;

import com.kun.mobilesafe.beans.BlackNumberBeans;
import com.kun.mobilesafe.database.BlackNumberDBDAO;

/**
 * Created by Vonnie on 2015/11/19.
 */
public class Test extends AndroidTestCase {
    public void testCreateDB()
    {
        BlackNumberDBDAO blackNumberDBDAO=new BlackNumberDBDAO(getContext());
    }
    public void testADD()
    {
        BlackNumberDBDAO blackNumberDBDAO=new BlackNumberDBDAO(getContext());
        BlackNumberBeans beans=new BlackNumberBeans();
        beans.setNumber("110");
        beans.setName("123");
        beans.setMode(1);
        blackNumberDBDAO.AddBlackNumber(beans);
    }
}
