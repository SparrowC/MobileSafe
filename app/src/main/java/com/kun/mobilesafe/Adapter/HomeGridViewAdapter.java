package com.kun.mobilesafe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.beans.FunctionBean;

import java.util.List;

/**
 * Created by Kun on 2015/11/5.
 */
public class HomeGridViewAdapter extends BaseAdapter {

    private Context mContent;
    private List<FunctionBean> functions;

    public HomeGridViewAdapter(Context mContent, List<FunctionBean> functions) {
        this.mContent = mContent;
        this.functions = functions;
    }

    @Override
    public int getCount() {
        return functions.size();
    }

    @Override
    public Object getItem(int position) {
        return functions.get(position);
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
            convertView= LayoutInflater.from(mContent).inflate(R.layout.home_list_item,null);
            holder.ivFunctionImage= (ImageView) convertView.findViewById(R.id.ivFunctionImage);
            holder.tvFunctionText= (TextView) convertView.findViewById(R.id.tvFunctionText);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.ivFunctionImage.setImageResource(functions.get(position).getFunctionImageID());
        holder.tvFunctionText.setText(functions.get(position).getFunctionName());
        return convertView;
    }
    class ViewHolder
    {
        public ImageView ivFunctionImage;
        public TextView tvFunctionText;
    }
}
