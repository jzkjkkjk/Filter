package com.renrenche.filterlibrary;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

public final class FilterAdapter extends BaseAdapter {

    private List<FilterItemModel> mData;
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

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("test", "getView");
        IFilterItemV item;
        if (convertView == null) {
            item = (IFilterItemV) View.inflate(parent.getContext(), mItemLayoutId, null);
        } else {
            item = (IFilterItemV) convertView;
        }
        item.setValue(getItem(position).mValue);
        return (View) item;
    }
}
