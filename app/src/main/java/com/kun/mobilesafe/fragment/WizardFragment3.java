package com.kun.mobilesafe.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.activity.ContactActivity;
import com.kun.mobilesafe.beans.Contact;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;

/**
 * Created by Vonnie on 2015/11/7.
 */
public class WizardFragment3 extends Fragment {
    private SharedPreferencesHelper spHelper;
    private EditText et_safe_pn;
    private Button btn_selectContact;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.wizard_pager3,null);
        spHelper=new SharedPreferencesHelper(getActivity(),getString(R.string.sp_name));
        btn_selectContact= (Button) root.findViewById(R.id.btn_selectContact);
        btn_selectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), ContactActivity.class);
                startActivityForResult(intent,1);
            }
        });
        et_safe_pn= (EditText) root.findViewById(R.id.et_safe_pn);
        String text=spHelper.getSharedPreferencesString(getString(R.string.sp_text_setPN), null);
        if(text!=null)
            et_safe_pn.setText(text);
        return root;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==1&&resultCode==0&&data!=null)
        {
            Bundle bundle=data.getExtras();
            Contact contact= (Contact) bundle.get(getString(R.string.safe_pn));
            et_safe_pn.setText(contact.getPhoneNum());
        }
    }

   public boolean SavePhoneNumber()
   {
       String pn=et_safe_pn.getText().toString();
       if(!pn.equals(""))
       {
           spHelper.setSharedPreferencesString(getString(R.string.sp_text_setPN),pn );
           return true;
       }else
       {
           return false;
       }
   }
}
