package com.renrenche.filterlibrary;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public final class FilterAdapter extends BaseAdapter {

    private List<FilterItemModel> mData;
    private Context mContext;
    private int mItemLayoutId;

    public FilterAdapter(List<FilterItemModel> data, int itemLayoutId) {
        mData = data;
        mItemLayoutId = itemLayoutId;
        if (mItemLayoutId < 0) {
            throw new IllegalArgumentException("Item layout must be specify!");
        }
    }

    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public FilterItemModel getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    void setContext(Context context) {
        mContext = context;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IFilterItemV item;
        if (convertView == null) {
            item = (IFilterItemV) View.inflate(mContext, mItemLayoutId, null);
        } else {
            item = (IFilterItemV) convertView;
        }
        item.setValue(getItem(position).mValue);
        return (View) item;
    }
}
