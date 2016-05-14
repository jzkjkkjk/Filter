package com.renrenche.filterlibrary;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.List;

/**
 * Created by jiazhenkai on 16/5/13.
 */
public class MultipleFilter extends FrameLayout {

    private static final int DIMEN_MODEL_MASK = 0xff;
    public static final int DIMEN_MODEL_AVERAGE = 1 << 0;
    public static final int DIMEN_MODEL_ADAPTATION = 1 << 1;
    public static final int DIMEN_MODEL_SPECIFY = 1 << 2;
    private MultipleFilterAdapter mAdapter;
    private OnFilterItemClickListener mOnFilterItemClickListener;
    private LinearLayout mFilterContainer;
    private View mMaskView;
    private float mDensity;
    private Filter mCurOpenedFilter;
    private Filter mCurAnimatingFilter;
    private int mDimenModel;
    private Paint mPaint;
    private int mHeaderHeight;
    private int mHeaderSelector;
    private int mItemLayoutId;
    private int mHeaderTextColor;
    private float mHeaderTextSize;

    public MultipleFilter(Context context) {
        this(context, null);
    }

    public MultipleFilter(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultipleFilter(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MultipleFilter(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mDensity = getResources().getDisplayMetrics().density;
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.Filter);
        mItemLayoutId = typedArray.getResourceId(R.styleable.Filter_item_layout, -1);
        if (mItemLayoutId < 0) {
            throw new IllegalArgumentException("Item layout must be specify!");
        }
        mHeaderHeight = typedArray.getDimensionPixelSize(R.styleable.Filter_header_height, getResources().getDimensionPixelSize(R.dimen.default_header_height));
        mDimenModel = typedArray.getInt(R.styleable.Filter_dimen_model, DIMEN_MODEL_AVERAGE);
        mHeaderSelector = typedArray.getResourceId(R.styleable.Filter_header_selector, -1);
        mHeaderTextColor = typedArray.getColor(R.styleable.Filter_header_text_color, Color.DKGRAY);
        mHeaderTextSize = typedArray.getDimension(R.styleable.Filter_header_text_size, 18);
        typedArray.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mHeaderTextSize);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams cLp = (LayoutParams) child.getLayoutParams();
            cLp.topMargin = cLp.topMargin + mHeaderHeight;
        }

        setMask(findViewById(R.id.filter_mask));

        mFilterContainer = new LinearLayout(getContext());
        mFilterContainer.setOrientation(LinearLayout.HORIZONTAL);
        addView(mFilterContainer, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setMask(View mask) {
        mMaskView = mask;
        if (mMaskView != null) {
            mMaskView.getBackground().setAlpha(0);
            mMaskView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCurAnimatingFilter == null && mCurOpenedFilter != null) {
                        mCurOpenedFilter.close();
                    }
                }
            });
            mMaskView.setClickable(false);
            if (mAdapter != null && mAdapter.getCount() > 0) {
                int count = mAdapter.getCount();
                for (int i = 0; i < count; i++) {
                    Filter item = (Filter) findViewWithTag(mAdapter.getItemKey(i));
                    item.setMask(mMaskView);
                }
            }
        }
    }

    public void setAdapter(MultipleFilterAdapter adapter) {
        mAdapter = adapter;
        if (mAdapter != null && mAdapter.getCount() > 0) {
            int count = mAdapter.getCount();
            for (int i = 0; i < count; i++) {
                Filter item = new Filter(getContext());
                item.setTag(mAdapter.getItemKey(i));
                item.setTitle(mAdapter.getItemKey(i));
                addItem(item, i);
            }
        }
    }

    private void addItem(Filter item, int index) {
        switch (mDimenModel & DIMEN_MODEL_MASK) {
            case DIMEN_MODEL_AVERAGE://均分
                mFilterContainer.addView(item, new ViewGroup.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT));
                LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) item.getLayoutParams();
                llp.weight = 1;
                break;
            case DIMEN_MODEL_ADAPTATION://自适应(确保能完全展示最长标题)
                List<FilterItemModel> itemValue = mAdapter.getItemValue(index);
                String valueStr = "";
                for (FilterItemModel value : itemValue) {
                    if (valueStr.length() < value.mValue.length()) {
                        valueStr = value.mValue;
                    }
                }
                mFilterContainer.addView(item, new ViewGroup.LayoutParams((int) mPaint.measureText(valueStr) + 40 + dp2Pixel(15) + dp2Pixel(40), ViewGroup.LayoutParams.MATCH_PARENT));
                break;
            case DIMEN_MODEL_SPECIFY://指定
                mFilterContainer.addView(item, new ViewGroup.LayoutParams(mAdapter.getItemWidthDimen(index), ViewGroup.LayoutParams.MATCH_PARENT));
                break;
        }
        if (index != 0) {
            LinearLayout.LayoutParams llp = (LinearLayout.LayoutParams) item.getLayoutParams();
            llp.leftMargin = dp2Pixel(0.5f);
        }
        if (mOnFilterItemClickListener != null) {
            item.setOnFilterItemClickListener(mOnFilterItemClickListener);
        }
        if (mMaskView != null) {
            item.setMask(mMaskView);
        }
        item.setHeaderHeight(mHeaderHeight);
        item.setHeaderSelector(mHeaderSelector);
        item.setHeaderTextColor(mHeaderTextColor);
        item.setHeaderTextSize(mHeaderTextSize);
        item.setAdapter(new FilterAdapter(mAdapter.getItemValue(index), mItemLayoutId));
        item.setOnFilterStatusChangedListener(new Filter.OnFilterStatusChangedListener() {

            @Override
            public void onFilterOpened(Filter filter) {
                mCurOpenedFilter = filter;
                mMaskView.setClickable(true);
            }

            @Override
            public void onFilterClosed(Filter filter) {
                mCurOpenedFilter = null;
                mMaskView.setClickable(false);
            }

            @Override
            public void onFilterAnimating(Filter filter) {
                mCurAnimatingFilter = filter;
            }
        });
        item.registerLock(new Filter.Lock() {
            @Override
            public boolean shouldLock(Filter filter) {
                boolean temp = (mCurOpenedFilter != null && mCurOpenedFilter != filter)
                        || (mCurAnimatingFilter != null && mCurAnimatingFilter != filter);
                if (temp && mCurOpenedFilter != null) {
                    mCurOpenedFilter.close();
                }
                return temp;
            }
        });
    }

    public void setOnFilterItemClickListener(OnFilterItemClickListener listener) {
        mOnFilterItemClickListener = listener;
        if (mAdapter != null && mAdapter.getCount() > 0) {
            int count = mAdapter.getCount();
            for (int i = 0; i < count; i++) {
                Filter item = (Filter) findViewWithTag(mAdapter.getItemKey(i));
                item.setOnFilterItemClickListener(mOnFilterItemClickListener);
            }
        }
    }

    public boolean isAnimating() {
        return mCurAnimatingFilter != null && mCurAnimatingFilter.isAnimating();
    }

    public boolean isOpened() {
        return mCurOpenedFilter != null && mCurOpenedFilter.isOpened();
    }

    public void close() {
        if (mCurOpenedFilter != null) {
            mCurOpenedFilter.close();
            mCurOpenedFilter = null;
        }
    }

    private int dp2Pixel(float dp) {
        return (int) (dp * mDensity + 0.5f);
    }
}
