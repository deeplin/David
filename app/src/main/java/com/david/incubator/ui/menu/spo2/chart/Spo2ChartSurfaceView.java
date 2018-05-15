package com.david.incubator.ui.menu.spo2.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.serial.SerialControl;
import com.david.common.serial.command.spo2.Spo2WaveCommand;
import com.david.common.ui.IViewModel;
import com.david.common.util.AutoUtil;
import com.david.common.util.Pair;
import com.david.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/3 11:40
 * email: 10525677@qq.com
 * description:
 */
public class Spo2ChartSurfaceView extends SurfaceView implements IViewModel {

    @Inject
    Spo2WaveCommand spo2WaveCommand;
    @Inject
    SerialControl serialControl;

    private final long INTERVAL = 15800;// 1000 000/63.5 = 15748 微秒
    private final int POINT_MAXIMUM = 508; //(int) (63.5 * 8);
    /*绘图相关*/
    private final int START_PPG_Y;
    private final int START_SIQ_Y;

    private volatile int bufferAvailable;
    private volatile boolean running;
    private int previousCyPpg;
    private List<Pair<String, String>> bufferList;

    private Disposable disposable;
    private Paint ppgPaint;
    private Paint siqPaint;

    private int xIndex = 0;

    private SurfaceHolder holder;

    public Spo2ChartSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        START_PPG_Y = AutoUtil.getHeight(70);
        START_SIQ_Y = AutoUtil.getHeight(178);

        bufferAvailable = 0;
        previousCyPpg = START_PPG_Y;
        bufferList = new ArrayList<>();

        this.setZOrderOnTop(true);
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);

        ppgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        ppgPaint.setStrokeWidth(3);
        ppgPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.info));

        ppgPaint.setStrokeCap(Paint.Cap.ROUND);
        ppgPaint.setStrokeJoin(Paint.Join.ROUND);

        siqPaint = new Paint();
        siqPaint.setStrokeWidth(3);
        siqPaint.setColor(ContextCompat.getColor(this.getContext(), R.color.info_white));
    }

    @Override
    public void attach() {
        synchronized (this) {
            if (disposable == null) {
                Observable<Long> observable = Observable.interval(500 * 1000, INTERVAL, TimeUnit.MICROSECONDS)
                        .observeOn(Schedulers.io());
                disposable = observable.subscribe(this::draw);
            }
        }
        running = true;
        this.setVisibility(View.VISIBLE);
    }

    @Override
    public void detach() {
        running = false;
        this.setVisibility(View.GONE);
        synchronized (this) {
            if (disposable != null) {
                disposable.dispose();
                disposable = null;
            }
        }
    }

    private int reSend = 32;
    private
    void draw(@NonNull Long aLong) {
        if (!running)
            return;

        /*读数据*/
        if (bufferAvailable == 60 || bufferAvailable == 30 || bufferAvailable == 0) {
            if (reSend <= 0) {
                serialControl.sendAsync(spo2WaveCommand);
                reSend = 64;
            }else{
                reSend --;
            }
        }else{
            if (reSend > 0) {
                reSend--;
            }
        }

        if (bufferAvailable <= 0) {
            bufferList.clear();
            spo2WaveCommand.copyToList(bufferList);
            bufferAvailable = bufferList.size();
        }

        if (bufferAvailable <= 0)
            return;

        /*画图*/
        Pair<String, String> pair = bufferList.get(bufferList.size() - (bufferAvailable--));

        int yPpg;
        if (pair.getKey().length() > 0) {
            yPpg = StringUtil.hexToInteger(pair.getKey());
        } else {
            yPpg = 0;
        }

        int ySiq;
        if (pair.getValue().length() > 0) {
            ySiq = StringUtil.hexToInteger(pair.getValue());
        } else {
            ySiq = 0;
        }

        int cyPpg = (int) (yPpg * (-0.5)) + START_PPG_Y;
        int cySiq = (int) (ySiq * (-0.3)) + START_SIQ_Y;

        Canvas canvas = null;
        try {
            if (running) {
                canvas = holder.lockCanvas(new Rect(xIndex, 0, xIndex + 20, 250));
                if (canvas != null) {
                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
                    canvas.drawLine(xIndex, previousCyPpg, xIndex + 1, cyPpg, ppgPaint);
                    previousCyPpg = cyPpg;
                    if (cySiq == START_SIQ_Y)
                        canvas.drawPoint(xIndex, cySiq, siqPaint);
                    else
                        canvas.drawLine(xIndex, START_SIQ_Y, xIndex, cySiq, siqPaint);
                }
            } else {
                canvas = holder.lockCanvas();
            }
        } finally {
            if (canvas != null)
                holder.unlockCanvasAndPost(canvas);
        }

        xIndex = xIndex % POINT_MAXIMUM;
        xIndex++;
    }
}
