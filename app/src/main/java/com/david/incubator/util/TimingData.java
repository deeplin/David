package com.david.incubator.util;

import android.media.AudioManager;
import android.media.SoundPool;
import android.support.annotation.NonNull;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.SystemSetting;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/31 22:26
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class TimingData implements Consumer<Long> {

    @Inject
    DaoControl daoControl;

    public static final String APGAR = "APGAR";
    public static final String CPR = "CPR";

    private String titleString;
    private String textString;

    private boolean apgarSelected;

    private int count;
    private Disposable disposable;
    private Consumer<String> consumer;

    private final SoundPool soundPool;

    public float volume;

    @Inject
    TimingData() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        count = -1;
        textString = "--:--";
        loadVolume();
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(
                (pool, sampleId, status) ->
                        pool.play(sampleId, volume, volume, 0, 0, 1));

        setApgar();
    }

    public void loadVolume() {
        SystemSetting systemSetting = daoControl.getSystemSetting();
        this.volume = (float) (systemSetting.getVolume() / 100.0);
    }

    public synchronized void start() {
        if (disposable == null) {
            count = -1;
            Observable<Long> observable = Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                    .observeOn(Schedulers.io());
            disposable = observable.subscribe(this);
        }
    }

    public synchronized void setApgar() {
        stop();
        titleString = APGAR;
        apgarSelected = true;
    }

    public synchronized void setCpr() {
        stop();
        titleString = CPR;
        apgarSelected = false;
    }

    public synchronized void setConsumer(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public synchronized void stop() {
        if (disposable != null) {
            textString = "--:--";
            if (consumer != null) {
                try {
                    consumer.accept(textString);
                } catch (Exception e) {
                    LogUtils.e(e);
                }
            }
            disposable.dispose();
            disposable = null;
        }
    }

    public synchronized boolean isStarted() {
        return disposable != null;
    }

    public synchronized boolean isApgarSelected() {
        return apgarSelected;
    }

    public synchronized boolean isApgarStarted() {
        return isStarted() && apgarSelected;
    }

    public synchronized boolean isCprStarted() {
        return isStarted() && (!apgarSelected);
    }

    public synchronized String getTitleString() {
        return titleString;
    }

    public String getTextString() {
        return textString;
    }

    public int getCount() {
        return count;
    }

    @Override
    public synchronized void accept(@NonNull Long o) throws Exception {
        count++;
        int countInHour = count / 2 % 3600;

        textString = String.format(Locale.US, "%02d:%02d", countInHour / 60, countInHour % 60);

        if (consumer != null) {
            consumer.accept(textString);
        }
        if (count % 2 == 1) {
            return;
        }

        if (isCprStarted()) {
            if (countInHour % 30 == 27) {
                if ((countInHour == 57) || (countInHour == 4 * 60 + 57) || (countInHour == 9 * 60 + 57)) {
                    playSound(R.raw.cpr2);
                } else if (countInHour < 600) {
                    playSound(R.raw.cpr1);
                }
            }
        } else if (isApgarStarted()) {
            if ((countInHour == 60) || countInHour == 3 * 60 || (countInHour == 5 * 60) || (countInHour == 10 * 60)) {
                playSound(R.raw.apgar);
            }
        }
    }

    private void playSound(int soundId) {
        Observable.just(this).observeOn(Schedulers.newThread())
                .subscribe(timingData -> soundPool.load(
                        MainApplication.getInstance().getApplicationContext(), soundId, 0));
    }
}