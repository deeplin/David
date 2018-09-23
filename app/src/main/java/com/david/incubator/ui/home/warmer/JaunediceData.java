package com.david.incubator.ui.home.warmer;

import android.support.annotation.NonNull;

import com.david.common.control.AutomationControl;
import com.david.common.control.DaoControl;
import com.david.common.control.MessageSender;
import com.david.common.serial.command.other.LEDCommand;
import com.david.common.ui.ViewUtil;
import com.david.incubator.control.MainApplication;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2017/12/31 22:26
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class JaunediceData implements Consumer<Long> {

    @Inject
    DaoControl daoControl;
    @Inject
    MessageSender messageSender;
    @Inject
    AutomationControl automationControl;

    private boolean clockwise;
    private int countdownMinute;

    private String textString;

    private int count;
    private Consumer<String> consumer;
    private boolean started = false;

    @Inject
    public JaunediceData() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        textString = "--:--";
        setClockwise(true, 2 * 60);
    }

    public synchronized void start() {
        if (!started) {
            messageSender.setLED(LEDCommand.BLUE, false, (aBoolean, baseSerialMessage) -> {
                started = true;
                automationControl.addConsumer(this);
                count = -1;
                if (consumer != null) {
                    consumer.accept(textString);
                }
            });
        }
    }

    public synchronized void stop() {
        if (started) {
            messageSender.setLED(LEDCommand.BLUE, true, (aBoolean, baseSerialMessage) -> {
                started = false;
                automationControl.removeConsumer(this);
                textString = "--:--";
                if (consumer != null) {
                    consumer.accept(textString);
                }
            });
        }
    }

    public synchronized void setClockwise(boolean status, int countdownMinute) {
        count = -1;
        clockwise = status;
        this.countdownMinute = countdownMinute;
    }

    public synchronized void setConsumer(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public synchronized boolean isStarted() {
        return started;
    }

    public synchronized boolean isClockwiseSelected() {
        return clockwise;
    }

    public int getCountdownMinute() {
        return countdownMinute;
    }

    public String getTextString() {
        return textString;
    }

    @Override
    public synchronized void accept(@NonNull Long o) throws Exception {
        count++;

        if (clockwise) {
            textString = ViewUtil.formatJaunediceTime(count / 60, count % 2 == 0);
        } else {
            int remainCount = countdownMinute - count / 60;
            if (remainCount > 0) {
                textString = ViewUtil.formatJaunediceTime(remainCount, count % 2 == 0);
            } else {
                count = -1;
                stop();
            }
        }

        if (consumer != null) {
            consumer.accept(textString);
        }

        if (count % 10 == 9)
            daoControl.increaseJaunediceTime();
    }
}