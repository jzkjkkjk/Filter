package com.renrenche.filterlibrary;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.View;

/**
 * Created by jzkcan on 2016/5/16.
 */
public class SimpleTextView extends View {

    private Paint mTextPaint;
    private int mTextWidth;
    private String mText;
    private float mDensity;

    public SimpleTextView(Context context) {
        super(context);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mDensity = getResources().getDisplayMetrics().density;
    }

    public void setTextSize(float size) {
        mTextPaint.setTextSize(dp2Pixel(size));
    }

    public void setTextColor(int color) {
        mTextPaint.setColor(color);
    }

    public void setText(String text) {
        mText = text;
        mTextWidth = TextUtils.isEmpty(text) ? 0 : (int) mTextPaint.measureText(text);
        invalidate();
    }

    public String getText() {
        return mText;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        if (mText != null) {
            canvas.drawText(mText, (width - mTextWidth) / 2, (height - mTextPaint.descent() - mTextPaint.ascent()) / 2, mTextPaint);
        }
    }

    int dp2Pixel(float dp) {
        return (int) (dp * mDensity + 0.5f);
    }
}
