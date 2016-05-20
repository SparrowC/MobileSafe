package com.kun.mobilesafe.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kun.mobilesafe.R;
import com.kun.mobilesafe.beans.BlackNumberBeans;

import java.util.List;

/**
 * Created by Vonnie on 2015/11/21.
 */
public class BlackNumberAdapter extends BaseAdapter {
    private Context mContext;
    private List<BlackNumberBeans> mBlackNumberList;

    public BlackNumberAdapter(Context context, List<BlackNumberBeans> blackNumberList) {
        mContext = context;
        mBlackNumberList = blackNumberList;
    }


    @Override
    public int getCount() {
        return mBlackNumberList.size();
    }

    @Override
    public Object getItem(int position) {
        return mBlackNumberList.get(position);
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
            convertView= LayoutInflater.from(mContext).inflate(R.layout.black_number_list_item,null);
            holder.tv_blackNumber= (TextView) convertView.findViewById(R.id.tv_blackNumber);
            holder.tv_mode= (TextView) convertView.findViewById(R.id.tv_mode);
            convertView.setTag(holder);
        }else
        {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tv_blackNumber.setText(mBlackNumberList.get(position).getNumber());
        String text="";
        /**
         * 黑名单的模式：0 不拦截，1 拦截电话，2 拦截短息，3 都拦截
         */
        switch (mBlackNumberList.get(position).getMode())
        {
            case 0:
                text="不拦截";
                break;
            case 1:
                text="拦截电话";
                break;
            case 2:
                text="拦截短息";
                break;
            case 3:
                text="全部拦截";
                break;
        }
        holder.tv_mode.setText(text);

        return convertView;
    }

    class ViewHolder
    {
        public TextView tv_blackNumber,tv_mode;
    }
}
