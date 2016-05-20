package com.kun.mobilesafe.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.utils.SharedPreferencesHelper;
import com.kun.mobilesafe.view.CheckBoxItem;

/**
 * Created by Vonnie on 2015/11/7.
 */
public class WizardFragment2 extends Fragment {
    private CheckBoxItem cbi_bind_sim;
    SharedPreferencesHelper spHelper;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.wizard_pager2,null);
        initWidget(root);
        initListener(root);
        return root;
    }

    private void initWidget(View pRoot)
    {
        spHelper=new SharedPreferencesHelper(getActivity(),getString(R.string.sp_name));
        boolean is_bind=spHelper.getSharedPreferencesBoolean(getString(R.string.sp_text_bind),false);
        cbi_bind_sim= (CheckBoxItem) pRoot.findViewById(R.id.cbi_bind_sim);

        cbi_bind_sim.setChecked(is_bind);
    }
    private void initListener(View pRoot)
    {
        cbi_bind_sim.setOnCheckedListener(new CheckBoxItem.OnCheckBoxItemCheckedListener() {
            @Override
            public void onChecked() {
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_bind), true);

                TelephonyManager manager= (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                spHelper.setSharedPreferencesString(getString(R.string.sp_text_sim_sn), manager.getSimSerialNumber());

            }

            @Override
            public void onUnchecked() {
                spHelper.setSharedPreferencesBoolean(getString(R.string.sp_text_bind),false);
                spHelper.setSharedPreferencesString(getString(R.string.sp_text_sim_sn), "");
            }
        });
    }
    public boolean BindSIM()
    {
        return cbi_bind_sim.isChecked();
    }
}
