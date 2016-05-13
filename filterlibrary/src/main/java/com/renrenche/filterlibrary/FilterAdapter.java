package com.renrenche.filterlibrary;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FilterAdapter extends BaseAdapter {

    private List<FilterItemModel> mData;
    private Context mContext;

    public FilterAdapter(List<FilterItemModel> data, Context context) {
        mData = data;
        mContext = context;
    }

    public void refreshData(List<FilterItemModel> data) {
        if (mData != null) {
            mData.clear();
        } else {
            mData = new ArrayList<>();
        }
        if (data != null) {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public FilterItemModel getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public TextView getView(int position, View convertView, ViewGroup parent) {

        TextView item;
        if (convertView == null) {
            item = new TextView(mContext);
            item.setTextColor(Color.BLACK);
            item.setPadding(40, 40, 0, 40);
        } else {
            item = (TextView) convertView;
        }
        item.setText(getItem(position).mValue);
        return item;
    }
}
