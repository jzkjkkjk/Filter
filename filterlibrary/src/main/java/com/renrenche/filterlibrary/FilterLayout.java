package com.renrenche.filterlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jiazhenkai on 16/4/20.
 */
public class FilterLayout extends LinearLayout {

    private float mDensity;
    private boolean mHasContentOpen;
    private boolean mIsAnimating;
    private Animation mAnimationIn;
    private Animation mAnimationOut;
    private View mContentView;
    private View mMaskView;
    private View mIndicator;
    private OnClickListener mCloseListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mIsAnimating && mHasContentOpen) {
                close();
            }
        }
    };

    public FilterLayout(Context context) {
        this(context, null, 0);
    }

    public FilterLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FilterLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public FilterLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        mDensity = getResources().getDisplayMetrics().density;
        addView(createHeader(context), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(createContainer(context), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setOnClickListener(mCloseListener);
        initAnim();
        mContentView = findViewById(R.id.filter_content);
        mContentView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        if (isHardwareAccelerated()) {
            mContentView.setLayerType(LAYER_TYPE_HARDWARE, null);
            mMaskView.setLayerType(LAYER_TYPE_HARDWARE, null);
            mIndicator.setLayerType(LAYER_TYPE_HARDWARE, null);
        }
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
                if (!mHasContentOpen) {
                    open();
                } else {
                    close();
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

    private View createContainer(Context context) {
        FrameLayout contentContainer = new FrameLayout(context);
        mMaskView = contentContainer;
        contentContainer.setBackgroundColor(Color.BLACK);
        ViewCompat.setAlpha(contentContainer, 0);
        FrameLayout content = new FrameLayout(context);//TODO content
        content.setId(R.id.filter_content);
        content.setBackgroundColor(Color.WHITE);
        contentContainer.addView(content);
        content.setVisibility(INVISIBLE);
        content.getLayoutParams().height = dp2Pixel(200);
        return contentContainer;
    }

    private void initAnim() {
        mAnimationIn = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_from_top);
        mAnimationIn.setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                mContentView.setVisibility(VISIBLE);
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHasContentOpen = true;
                mIsAnimating = false;
            }
        });
        mAnimationOut = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_to_top);
        mAnimationOut.setAnimationListener(new AnimationAdapter() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mContentView.setVisibility(GONE);
                mHasContentOpen = false;
                mIsAnimating = false;
            }
        });
    }

    private void close() {
        mContentView.startAnimation(mAnimationOut);
        mMaskView.animate().alpha(0).setDuration(300).start();
        mIndicator.animate().rotation(0).setDuration(300).start();
    }

    private void open() {
        mContentView.startAnimation(mAnimationIn);
        mMaskView.animate().alpha(0.5f).setDuration(300).start();
        mIndicator.animate().rotation(180).setDuration(300).start();
    }

    private int dp2Pixel(float dp) {
        return (int) (dp * mDensity + 0.5f);
    }

    private static class AnimationAdapter implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    public static class Adapter<T> {

        public T getItem() {
            return null;
        }

        public int getCount() {
            return 0;
        }

        public View getView(int position) {
            return null;
        }
    }
}
