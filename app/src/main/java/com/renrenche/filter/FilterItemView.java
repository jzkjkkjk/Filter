package com.renrenche.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renrenche.filterlibrary.IFilterItemV;

/**
 * Created by jzkcan on 2016/5/14.
 */
public class FilterItemView extends LinearLayout implements IFilterItemV {

    private TextView mValueTv;

    public FilterItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mValueTv = (TextView) findViewById(R.id.value);
    }

    @Override
    public void setValue(String value) {
        mValueTv.setText(value);
    }
}