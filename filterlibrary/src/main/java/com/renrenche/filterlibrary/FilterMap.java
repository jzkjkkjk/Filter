package com.renrenche.filterlibrary;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jzkcan on 2016/5/18.
 */
public final class FilterMap {
    private List<String> mKeys;
    private List<List<FilterItemModel>> mValues;

    public FilterMap() {
        mKeys = new ArrayList<>();
        mValues = new ArrayList<>();
    }

    public void put(String category, List<FilterItemModel> values) {
        if (category == null || values == null) {
            throw new RuntimeException("Category or values can not be null at FilterMap!");
        }
        mKeys.add(category);
        mValues.add(values);
    }

    public String keyAt(int position) {
        return mKeys.get(position);
    }

    public List<FilterItemModel> valueAt(int position) {
        return mValues.get(position);
    }

    public int size() {
        return Math.min(mKeys.size(), mValues.size());
    }
}
