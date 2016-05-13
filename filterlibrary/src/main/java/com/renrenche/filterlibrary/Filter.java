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
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by jiazhenkai on 16/4/20.
 */
public class Filter extends FrameLayout implements View.OnClickListener {

    static final int HEADER_HEIGHT = 60;
    private static final int ANIM_DURATION = 300;
    private static final int ALPHA_THRESHOLD = 64;
    private float mDensity;
    private boolean mIsOpened;
    private boolean mIsAnimating;
    private ListView mContentView;
    private TextView mTitleView;
    private View mIndicator;
    private View mMask;
    private ValueAnimator mBgAlphaOpenVam;
    private ValueAnimator mBgAlphaCloseVam;
    private FilterAdapter mAdapter;
    private OnFilterItemClickListener mOnFilterItemClickListener;
    private OnFilterStatusChangedListener mOnFilterStatusChangedListener;
    private Lock mLock;

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

        addView(createContent(context), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(createTitle(context), new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2Pixel(HEADER_HEIGHT)));
        FrameLayout.LayoutParams fl = (LayoutParams) mContentView.getLayoutParams();
        fl.topMargin = dp2Pixel(HEADER_HEIGHT);
        if (isHardwareAccelerated()) {
            mContentView.setLayerType(LAYER_TYPE_HARDWARE, null);
            mIndicator.setLayerType(LAYER_TYPE_HARDWARE, null);
        }

        mMask = this;
        mMask.setOnClickListener(this);
        mBgAlphaOpenVam = ValueAnimator.ofInt(0, ALPHA_THRESHOLD);
        mBgAlphaOpenVam.setEvaluator(new IntEvaluator());
        mBgAlphaOpenVam.setDuration(ANIM_DURATION);
        mBgAlphaOpenVam.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMask.getBackground().setAlpha((int) animation.getAnimatedValue());
            }
        });

        mBgAlphaCloseVam = ValueAnimator.ofInt(ALPHA_THRESHOLD, 0);
        mBgAlphaCloseVam.setEvaluator(new IntEvaluator());
        mBgAlphaCloseVam.setDuration(ANIM_DURATION);
        mBgAlphaCloseVam.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mMask.getBackground().setAlpha((int) animation.getAnimatedValue());
            }
        });
    }

    private View createTitle(Context context) {
        RelativeLayout titleLayout = new RelativeLayout(context);
        titleLayout.setBackgroundColor(Color.RED);//TODO title bg color
        titleLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mIsAnimating || (mLock != null && mLock.shouldLock(Filter.this))) {
                    return;
                }
                if (mIsOpened) {
                    close();
                } else {
                    open();
                }
            }
        });

        mTitleView = new TextView(context);
        mTitleView.setTextColor(Color.WHITE);//TODO title text color
        mTitleView.setText("这是测试");//TODO title text
        mTitleView.setTextSize(20);//TODO title text size
        mTitleView.setPadding(0, dp2Pixel(15), 0, dp2Pixel(15));//TODO title text padding
        titleLayout.addView(mTitleView);
        RelativeLayout.LayoutParams titleRlp = (RelativeLayout.LayoutParams) mTitleView.getLayoutParams();
        titleRlp.addRule(RelativeLayout.CENTER_IN_PARENT);

        ImageView headerIndicator = new ImageView(context);
        mIndicator = headerIndicator;
        headerIndicator.setImageResource(R.mipmap.ic_arrow_gray);//TODO indicator img
        titleLayout.addView(headerIndicator);
        RelativeLayout.LayoutParams indicatorRlp = (RelativeLayout.LayoutParams) headerIndicator.getLayoutParams();
        indicatorRlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        indicatorRlp.addRule(RelativeLayout.CENTER_VERTICAL);
        indicatorRlp.rightMargin = dp2Pixel(15);//TODO indicator img margin right
        return titleLayout;
    }

    private View createContent(Context context) {
        mContentView = new ListView(context);
        mContentView.setBackgroundColor(Color.WHITE);
        mContentView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mOnFilterItemClickListener != null) {
                    close();
                    FilterItemModel item = mAdapter.getItem(position);
                    mOnFilterItemClickListener.onFilterItemClick(item.mTitle, item.mValue);
                }
            }
        });
        return mContentView;
    }

    public void close() {
        mContentView.animate().translationY(-mContentView.getHeight()).setDuration(ANIM_DURATION).start();
        mBgAlphaCloseVam.start();
        mIndicator.animate().rotation(0).setDuration(ANIM_DURATION).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                mIsAnimating = true;
                if (mOnFilterStatusChangedListener != null) {
                    mOnFilterStatusChangedListener.onFilterAnimating(Filter.this);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsOpened = false;
                mIsAnimating = false;
                if (mOnFilterStatusChangedListener != null) {
                    mOnFilterStatusChangedListener.onFilterClosed(Filter.this);
                    mOnFilterStatusChangedListener.onFilterAnimating(null);
                }
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
                if (mOnFilterStatusChangedListener != null) {
                    mOnFilterStatusChangedListener.onFilterAnimating(Filter.this);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mIsOpened = true;
                mIsAnimating = false;
                if (mOnFilterStatusChangedListener != null) {
                    mOnFilterStatusChangedListener.onFilterOpened(Filter.this);
                    mOnFilterStatusChangedListener.onFilterAnimating(null);
                }
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
        //TODO 临时
        final ViewTreeObserver viewTreeObserver = mContentView.getViewTreeObserver();
        viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewTreeObserver.removeOnPreDrawListener(this);
                mContentView.animate().translationY(-mContentView.getHeight()).setDuration(0).start();
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

    public void setAdapter(FilterAdapter adapter) {
        mAdapter = adapter;
        mContentView.setAdapter(adapter);
    }

    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    public void setOnFilterItemClickListener(OnFilterItemClickListener listener) {
        mOnFilterItemClickListener = listener;
    }

    public void setOnFilterStatusChangedListener(OnFilterStatusChangedListener listener) {
        mOnFilterStatusChangedListener = listener;
    }

    public void setMask(View mask) {
        mMask = mask;
        setClickable(false);
    }

    void registerLock(Lock lock) {
        mLock = lock;
    }

    public interface OnFilterStatusChangedListener {
        void onFilterOpened(Filter filter);

        void onFilterAnimating(Filter filter);

        void onFilterClosed(Filter filter);
    }

    interface Lock {
        boolean shouldLock(Filter filter);
    }
}
