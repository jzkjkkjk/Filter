package com.renrenche.filterlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by jiazhenkai on 16/5/13.
 */
public class MultipleFilter extends LinearLayout{
    public MultipleFilter(Context context) {
        super(context);
    }

    public MultipleFilter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MultipleFilter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultipleFilter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
