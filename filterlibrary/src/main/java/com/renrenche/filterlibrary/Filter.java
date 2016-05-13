package com.renrenche.filterlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jiazhenkai on 16/4/20.
 */
public class Filter extends FrameLayout implements View.OnClickListener {

    private static final int ANIM_DURATION = 2000;
    private static final int HEADER_HEIGHT = 60;
    private float mDensity;
    private boolean mIsOpened;
    private boolean mIsAnimating;
    private View mContentView;
    private View mIndicator;
    private ValueAnimator mBgAlphaOpenVam;
    private ValueAnimator mBgAlphaCloseVam;

    public Filter(Context context) {
        this(context, null, 0);
    }

    public Filter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Filter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Filter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setBackgroundColor(Color.BLACK);
        getBackground().setAlpha(0);
        mDensity = getResources().getDisplayMetrics().density;
        addView(createContent(context), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(createHeader(context), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2Pixel(HEADER_HEIGHT)));
        mContentView.getLayoutParams().height = dp2Pixel(200);
        FrameLayout.LayoutParams fl = (LayoutParams) mContentView.getLayoutParams();
        fl.topMargin = dp2Pixel(HEADER_HEIGHT);
        setOnClickListener(this);
        if (isHardwareAccelerated()) {
            mContentView.setLayerType(LAYER_TYPE_HARDWARE, null);
            mIndicator.setLayerType(LAYER_TYPE_HARDWARE, null);
        }

        mBgAlphaOpenVam = ValueAnimator.ofInt(0, 128);
        mBgAlphaOpenVam.setEvaluator(new IntEvaluator());
        mBgAlphaOpenVam.setDuration(ANIM_DURATION);
        mBgAlphaOpenVam.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getBackground().setAlpha((int) animation.getAnimatedValue());
            }
        });

        mBgAlphaCloseVam = ValueAnimator.ofInt(128, 0);
        mBgAlphaCloseVam.setEvaluator(new IntEvaluator());
        mBgAlphaCloseVam.setDuration(ANIM_DURATION);
        mBgAlphaCloseVam.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                getBackground().setAlpha((int) animation.getAnimatedValue());
            }
        });
    }

    private View createHeader(Context context) {
        RelativeLayout headerLayout = new RelativeLayout(context);
        headerLayout.setBackgroundColor(Color.RED);//TODO title bg color
        headerLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsAnimating) {
                    return;
                }
                if (mIsOpened) {
                    close();
                } else {
                    open();
                }
            }
        });

        TextView headerTitle = new TextView(context);
        headerTitle.setTextColor(Color.WHITE);//TODO title text color
        headerTitle.setText("这是测试");//TODO title text
        headerTitle.setTextSize(20);//TODO title text size
        headerTitle.setPadding(0, dp2Pixel(15), 0, dp2Pixel(15));//TODO title text padding
        headerLayout.addView(headerTitle);
        RelativeLayout.LayoutParams titleRlp = (RelativeLayout.LayoutParams) headerTitle.getLayoutParams();
        titleRlp.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView headerIndicator = new ImageView(context);
        mIndicator = headerIndicator;
        headerIndicator.setImageResource(R.mipmap.ic_launcher);//TODO indicator img
        headerLayout.addView(headerIndicator);
        RelativeLayout.LayoutParams indicatorRlp = (RelativeLayout.LayoutParams) headerIndicator.getLayoutParams();
        indicatorRlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        indicatorRlp.addRule(RelativeLayout.CENTER_VERTICAL);
        indicatorRlp.leftMargin = dp2Pixel(10);//TODO indicator img margin right
        return headerLayout;
    }

    private View createContent(Context context) {
        FrameLayout content = new FrameLayout(context);//TODO content
        mContentView = content;
        content.setBackgroundColor(Color.WHITE);
        return content;
    }

    public void close() {
        mContentView.animate().translationY(- mContentView.getHeight()).setDuration(ANIM_DURATION).start();
        mBgAlphaCloseVam.start();
        mIndicator.animate().rotation(0).setDuration(ANIM_DURATION).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsOpened = false;
                mIsAnimating = false;
            }
        }).start();
    }

    private void open() {
        mContentView.animate().translationY(0).setDuration(ANIM_DURATION).start();
        mBgAlphaOpenVam.start();
        mIndicator.animate().rotation(180).setDuration(ANIM_DURATION).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsOpened = true;
                mIsAnimating = false;
            }
        }).start();
    }

    public boolean isAnimating() {
        return mIsAnimating;
    }

    public boolean isOpened() {
        return !mIsAnimating && mIsOpened;
    }

    private int dp2Pixel(float dp) {
        return (int) (dp * mDensity + 0.5f);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        final ViewTreeObserver viewTreeObserver = mContentView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewTreeObserver.removeOnPreDrawListener(this);
                mContentView.animate().translationY(- mContentView.getHeight()).setDuration(0).start();
                return true;
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        if (l == this) {
            super.setOnClickListener(l);
        } else {
            throw new IllegalArgumentException("The listener can only be itself!");
        }
    }

    @Override
    public final void onClick(View v) {
        if (mIsAnimating) {
            return;
        }
        if (mIsOpened) {
            close();
        }
    }
}
