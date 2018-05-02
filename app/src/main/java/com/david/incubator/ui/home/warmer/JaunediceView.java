package com.david.incubator.ui.home.warmer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.util.AutoUtil;

import javax.inject.Inject;

import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2018/1/4 22:27
 * email: 10525677@qq.com
 * description:
 */

public class JaunediceView extends View implements Consumer {

    @Inject
    JaunediceData jaunediceData;

    private TextPaint textPaint;

    public JaunediceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        invalidateTextPaint();
    }

    public synchronized void press() {
        if (jaunediceData.isStarted()) {
            jaunediceData.stop();
        } else {
            jaunediceData.start();
        }
    }

    private void invalidateTextPaint() {
        textPaint = new TextPaint();
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.RIGHT);
        textPaint.setTextSize(AutoUtil.getHeight(60));
        textPaint.setTypeface(Typeface.MONOSPACE);
        textPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.info_white));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int contentWidth = getWidth();
        int contentHeight = getHeight();

        canvas.drawText(jaunediceData.getTextString(), contentWidth - 32, contentHeight - 20, textPaint);
    }

    public void attach() {
        jaunediceData.setConsumer(this);
    }

    public void detach() {
        jaunediceData.setConsumer(null);
    }

    @Override
    public void accept(Object o) {
        postInvalidate();
    }
}