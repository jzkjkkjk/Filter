package com.renrenche.anchorimageview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.renrenche.filter.R;

/**
 * Created by jzkcan on 2016/6/16.
 */
public class AnchorImageView extends View {

    private int mSize;
    private float mRatio;

    private Paint mTextPaint;
    private Paint mBitmapPaint;
    private Bitmap mBitmap;

    public AnchorImageView(Context context) {
        this(context, null);
    }

    public AnchorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnchorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AnchorImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.car_bottom);
        mSize = mBitmap.getByteCount();
        mRatio = mBitmap.getHeight() * 1.0f / mBitmap.getWidth();
        mTextPaint = new Paint();
        mTextPaint.setTextSize(20);
        mTextPaint.setColor(Color.BLACK);

        mBitmapPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = getWidth();
        int h = getHeight();
        Rect src = new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight());
        Rect dest = new Rect((int) (0.1f * w), (int) (h - 0.8f * w * mRatio) / 2, (int) (0.9f * w), (int) (h + 0.8f * w * mRatio) / 2);
        canvas.drawBitmap(mBitmap, src, dest, mBitmapPaint);
        log();
    }

    private void log() {
        Log.e("test", mSize + "");
        Log.e("test", getWidth() + ":" + getHeight());
    }
}
