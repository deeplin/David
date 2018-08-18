package com.david.incubator.ui.home.warmer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.david.R;
import com.david.common.util.AutoUtil;
import com.david.incubator.control.MainApplication;
import com.david.incubator.util.TimingData;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2018/1/4 22:27
 * email: 10525677@qq.com
 * description:
 */

public class HeatingTimingView extends View implements Consumer {

    @Inject
    TimingData timingData;

    private TextPaint titlePaint;
    private TextPaint textPaint;
    private Paint rectPaint;
    private Bitmap beepBitmap;

    private int rectX;
    private int rectY;
    private int rectWidth;
    private int rectHeightThin;
    private int rectGap;
    private int rectHeightThick;

    public HeatingTimingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        rectX = 26;
        rectY = AutoUtil.getHeight(46);

        rectWidth = 28;
        rectHeightThin = AutoUtil.getHeight(5);
        rectHeightThick = AutoUtil.getHeight(10);
        rectGap = 4;

        invalidateTextPaint();
    }

    public void attach() {
        timingData.setConsumer(this);
        postInvalidate();
    }

    public void detach() {
        timingData.setConsumer(null);
    }

    private void invalidateTextPaint() {
        // Set up a default TextPaint object
        titlePaint = new TextPaint();
        titlePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        titlePaint.setTextAlign(Paint.Align.LEFT);
        titlePaint.setTextSize(AutoUtil.getHeight(30));
        titlePaint.setColor(ContextCompat.getColor(this.getContext(), R.color.info_dark));

        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(AutoUtil.getHeight(60));
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.info_white));

        rectPaint = new Paint();
        rectPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.info_dark));
        rectPaint.setStyle(Paint.Style.FILL);

        beepBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.beep);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int contentWidth = getWidth();
        int contentHeight = getHeight();

        int count = timingData.getCount() / 2 % 3600;
        String title = timingData.getTitleString();
        String textValue = timingData.getTextString();

        /*写标题*/
        canvas.drawText(title, 24, AutoUtil.getHeight(36), titlePaint);

        /*写时间*/
        canvas.drawText(textValue, contentWidth - 8, contentHeight - 20, textPaint);
        /*画长方形*/
        if (timingData.isCprStarted()) {
            for (int index = 0; index < 7; index++) {
                int rx = rectX + index * (rectWidth + rectGap);
                canvas.drawRect(rx, rectY, rx + rectWidth, rectY + rectHeightThin, rectPaint);
            }

            /*去掉前30秒*/
            if (count < 30) {
                return;
            }
            //闪烁闹铃
            if (timingData.getCount() % 2 == 0) {
                for (int index = 0; index < 6; index++) {
                    if ((count == 60 + index) || (count == 4 * 60 + 60 + index) || (count == 9 * 60 + 60 + index)) {
                        canvas.drawBitmap(beepBitmap, 200, 10, null);
                        break;
                    }
                }
            }

            int remainder = count % 30;
            if (remainder >= 0 && remainder < 14) {
                drawRect(canvas, remainder % 7);
            }
        } else if (timingData.isApgarStarted()) {
            if (timingData.getCount() % 2 == 0) {
                for (int index = 0; index < 6; index++) {
                    if ((count == 60 + index) || (count == 3 * 60 + index) || (count == 5 * 60 + index) || (count == 10 * 60 + index)) {
                        canvas.drawBitmap(beepBitmap, 200, 10, null);
                        break;
                    }
                }
            }
        }
    }

    private void drawRect(Canvas canvas, int remainder) {
        for (int index = 0; index <= remainder; index++) {
            int rx = rectX + index * (rectWidth + rectGap);
            int ry = rectY + 8;
            canvas.drawRect(rx, ry, rx + rectWidth, ry + rectHeightThick, rectPaint);
        }
    }

    @Override
    public void accept(Object o) {
        postInvalidate();
    }
}