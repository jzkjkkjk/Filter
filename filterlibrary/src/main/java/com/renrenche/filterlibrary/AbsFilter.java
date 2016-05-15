package com.renrenche.filterlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by jiazhenkai on 16/5/15.
 */
public abstract class AbsFilter<D> extends FrameLayout {

    private float mDensity;

    public AbsFilter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mDensity = getResources().getDisplayMetrics().density;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AbsFilter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mDensity = getResources().getDisplayMetrics().density;
    }

    public abstract void setMask(View view);

    public abstract void setAdapter(D adapter);

    public abstract void close();

    public abstract boolean isOpened();

    public abstract boolean isAnimating();

    public boolean canGoBack() {
        if (!isAnimating()) {
            if (isOpened()) {
                close();
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    int dp2Pixel(float dp) {
        return (int) (dp * mDensity + 0.5f);
    }
}
