package com.david.common.ui;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageButton;

import com.david.common.util.Constant;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import io.reactivex.disposables.Disposable;

/**
 * author: Ling Lin
 * created on: 2018/1/14 17:02
 * email: 10525677@qq.com
 * description:
 */
public abstract class FastUpdateLayout<U extends ViewDataBinding> extends BindingConstraintLayout<U> implements ITabConstraintLayout {

    private volatile Disposable increaseDisposable;
    private volatile Disposable decreaseDisposable;

    private ImageButton increaseButton;
    private ImageButton decreaseButton;

    public FastUpdateLayout(Context context) {
        super(context);
    }

    public FastUpdateLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    protected void setButton(ImageButton increaseButton, ImageButton decreaseButton) {
        this.increaseButton = increaseButton;
        this.decreaseButton = decreaseButton;

        RxView.touches(increaseButton, motionEvent -> motionEvent.getAction() != MotionEvent.ACTION_MOVE)
                .subscribe((MotionEvent motionEvent) -> {
                    synchronized (this) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            stopDisposable();
                            increaseButton.setPressed(true);
                            increaseValue();
                            increaseDisposable = io.reactivex.Observable
                                    .interval(1000, Constant.LONG_CLICK_DELAY, TimeUnit.MILLISECONDS)
                                    .subscribe((Long aLong) -> increaseValue());
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            stopDisposable();
                            increaseButton.setPressed(false);
                        }
                    }
                });

        RxView.touches(this.decreaseButton, motionEvent -> motionEvent.getAction() != MotionEvent.ACTION_MOVE)
                .subscribe((MotionEvent motionEvent) -> {
                    synchronized (this) {
                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            stopDisposable();
                            decreaseButton.setPressed(true);
                            decreaseValue();
                            decreaseDisposable = io.reactivex.Observable
                                    .interval(1000, Constant.LONG_CLICK_DELAY, TimeUnit.MILLISECONDS)
                                    .subscribe((Long aLong) -> decreaseValue());
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            stopDisposable();
                            decreaseButton.setPressed(false);
                        }
                    }
                });
    }

    public synchronized void stopDisposable() {
        if (decreaseDisposable != null) {
            decreaseDisposable.dispose();
            decreaseDisposable = null;
        }
        if (increaseDisposable != null) {
            increaseDisposable.dispose();
            increaseDisposable = null;
        }
        increaseButton.setPressed(false);
        decreaseButton.setPressed(false);
    }

    protected abstract void increaseValue();

    protected abstract void decreaseValue();
}