package com.kun.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.kun.mobilesafe.Adapter.ContactListAdapter;
import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.base.BaseActivity;
import com.kun.mobilesafe.beans.Contact;
import com.kun.mobilesafe.utils.ContactUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vonnie on 2015/11/8.
 */
public class ContactActivity extends BaseActivity {
    private ListView lv_contact;
    private ContactListAdapter mAdapter;
    private List<Contact> mContactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_contact);
        addMainContent(R.layout.activity_contact);
        setMyTitle("联系人");
        initWidget();
        initListener();
        initBackButton(this);
    }

    @Override
    protected void initWidget() {
        lv_contact= (ListView) findViewById(R.id.lv_contact);
        mContactList=ContactUtils.getContact(this);
        mAdapter=new ContactListAdapter(this,mContactList );
        lv_contact.setAdapter(mAdapter);
    }

    @Override
    protected void initListener() {
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=getIntent();
                Bundle bundle=new Bundle();
                bundle.putSerializable(getString(R.string.safe_pn),  mContactList.get(position));
                intent.putExtras(bundle);
                ContactActivity.this.setResult(0,intent);
                ContactActivity.this.finish();
            }
        });
    }
}
