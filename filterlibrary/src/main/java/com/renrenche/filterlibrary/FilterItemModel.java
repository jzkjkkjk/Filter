package com.renrenche.filterlibrary;

/**
 * Created by jzkcan on 2016/5/13.
 */
public class FilterItemModel {
    public String mCategory;
    public String mValue;

    public FilterItemModel(String value) {
        this(null, value);
    }

    public FilterItemModel(String category, String value) {
        mCategory = category;
        mValue = value;
    }
}
