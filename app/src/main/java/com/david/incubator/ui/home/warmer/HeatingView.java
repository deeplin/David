package com.david.incubator.ui.home.warmer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.david.R;

/**
 * author: Ling Lin
 * created on: 2018/1/4 22:35
 * email: 10525677@qq.com
 * description:
 */
public class HeatingView extends View {

    private int mHeat;
    private String mHeatString;

    private TextPaint mTextPaint;
    private float mTextHeight;

    private Paint mCirclePaint;
    private Paint mArcPaint;

    public HeatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeatingView, defStyle, 0);

        mHeat = a.getInteger(R.styleable.HeatingView_heat, 0);
        mHeatString = mHeat + "%";

        a.recycle();

        // Update TextPaint and text measurements from attributes
        invalidateTextPaint();
        invalidateCirclePaint();
        invalidateArcPaint();
    }

    private void invalidateTextPaint() {
        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(30);
        mTextPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.c_air));

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    private void invalidateCirclePaint() {
        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(7);
        mCirclePaint.setColor(ContextCompat.getColor(this.getContext(), R.color.info_dark));
        mCirclePaint.setAntiAlias(true);
    }

    private void invalidateArcPaint() {
        mArcPaint = new Paint();
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(14);
        mArcPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.c_air));
        mArcPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingTop = 40;
        int contentWidth = getWidth();
        int contentHeight = getHeight();

        // Draw the text.
        float textWidth = mTextPaint.measureText(mHeatString);
        canvas.drawText(mHeatString, (contentWidth - textWidth) / 2,
                paddingTop + (contentHeight + mTextHeight) / 2,
                mTextPaint);

        float cx = contentWidth / 2.0f;
        float cy = contentHeight / 2.0f;
        float circleRadius = (contentWidth - 20) / 2.0f;
        canvas.drawCircle(cx, cy, circleRadius, mCirclePaint);

        RectF oval = new RectF(8, 8, contentWidth - 8, contentHeight - 8);
        canvas.drawArc(oval, -90, mHeat / 10 * 36 * -1, false, mArcPaint);
    }

    public void setHeat(int mHeat) {
        this.mHeat = mHeat;
        mHeatString = mHeat + "%";
        postInvalidate();
    }
}
