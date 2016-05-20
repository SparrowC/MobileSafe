package com.kun.mobilesafe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.beans.Contact;

import java.util.List;

/**
 * Created by Vonnie on 2015/11/8.
 */
public class ContactListAdapter extends BaseAdapter {

    private List<Contact> mContactList;
    private Context mContext;

    public ContactListAdapter( Context pContext,List<Contact> pContactList) {
        mContactList = pContactList;
        mContext = pContext;
    }

    @Override
    public int getCount() {
        return mContactList.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder=new ViewHolder();
        if(convertView==null)
        {
            convertView= LayoutInflater.from(mContext).inflate(R.layout.contact_item,null);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_contact_name);
            holder.tvPhoneNumber = (TextView) convertView.findViewById(R.id.tv_contact_pn);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tvName.setText(mContactList.get(position).getName());
        holder.tvPhoneNumber.setText(mContactList.get(position).getPhoneNum());
        return convertView;
    }
    class ViewHolder
    {

        public TextView tvPhoneNumber,tvName;
    }
}
