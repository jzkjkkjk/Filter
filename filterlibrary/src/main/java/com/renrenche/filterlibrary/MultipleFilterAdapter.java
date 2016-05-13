package com.renrenche.filterlibrary;

import android.support.v4.util.ArrayMap;

import java.util.List;

/**
 * Created by jzkcan on 2016/5/13.
 */
public class MultipleFilterAdapter {

    private ArrayMap<String, List<FilterItemModel>> mData;
    private int[] mWidthDimens;

    public MultipleFilterAdapter(ArrayMap<String, List<FilterItemModel>> data) {
        this(data, null);
    }

    public MultipleFilterAdapter(ArrayMap<String, List<FilterItemModel>> data, int[] widthDimens) {
        mData = data;
        mWidthDimens = widthDimens;
        if (widthDimens != null && data != null) {
            if (widthDimens.length != data.size()) {
                throw new IllegalArgumentException("Data count and dimention count shoulde be the same！");
            }
        }
    }

    public int getCount() {
        return mData.size();
    }

    public String getItemKey(int position) {
        return mData.keyAt(position);
    }

    public List<FilterItemModel> getItemValue(int position) {
        return mData.get(mData.keyAt(position));
    }

    public int getItemWidthDimen(int position) {
        int temp = mWidthDimens == null ? -1 : mWidthDimens[position];
        if (temp < 0) {
            throw new IllegalArgumentException("Width dimention cannot be negative");
        }
        return temp;
    }
}
