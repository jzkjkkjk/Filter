package com.renrenche.filterlibrary;

import java.util.List;

/**
 * Created by jzkcan on 2016/5/13.
 */
public final class MultipleFilterAdapter {

    private FilterMap mData;
    private int[] mWidthDimens;

    public MultipleFilterAdapter(FilterMap data) {
        this(data, null);
    }

    public MultipleFilterAdapter(FilterMap data, int[] widthDimens) {
        mData = data;
        mWidthDimens = widthDimens;
        if (widthDimens != null && data != null) {
            if (widthDimens.length != data.size()) {
                throw new IllegalArgumentException("Data count and dimention count shoulde be the same！");
            }
        }
    }

    int getCount() {
        return mData.size();
    }

    String getItemCategory(int position) {
        return mData.keyAt(position);
    }

    List<FilterItemModel> getItemValue(int position) {
        return mData.valueAt(position);
    }

    int getItemWidthDimen(int position) {
        int temp = mWidthDimens == null ? -1 : mWidthDimens[position];
        if (temp < 0) {
            throw new IllegalArgumentException("Width dimention cannot be negative");
        }
        return temp;
    }
}
