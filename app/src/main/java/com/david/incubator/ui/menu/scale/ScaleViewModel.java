package com.david.incubator.ui.menu.scale;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.Gravity;
import android.widget.Toast;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.data.ShareMemory;
import com.david.common.ui.IViewModel;
import com.david.common.util.ResourceUtil;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

@Singleton
public class ScaleViewModel implements IViewModel {
    @Inject
    ShareMemory shareMemory;
    @Inject
    DaoControl daoControl;

    private final int BABY_WEIGHT_THRESHOLD = 200;
    private final int PUT_DOWN_VIBRATE = 10;
    private final int RAISE_VIBRATE = 500;

    public ObservableInt recordedWeight = new ObservableInt();
    public ObservableBoolean resetMode = new ObservableBoolean(false);
    public ObservableBoolean upwards = new ObservableBoolean(true);
    public ObservableField<String> scaleInfo = new ObservableField<>();

    private int lowestWeight = 0;
    private int targetWeight = 0;
    private int weightCount = 0;
    private int tempLowestWeight = 0;

    private Disposable raiseBabyDisposable = null;
    private Disposable putDownBabyDisposable = null;

    private int weightOffset = 0;

    @Inject
    public ScaleViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    public synchronized void raiseBaby() {
        resetMode.set(true);
        upwards.set(true);
        tempLowestWeight = 0;

        stopDisposable();
        final String raiseInfo = ResourceUtil.getString(R.string.raise_baby);
        raiseBabyDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(12)
                .subscribe(aLong -> {
                    if (aLong <= 10) {
                        readLowestWeight();
                        scaleInfo.set(String.format(Locale.US, "%s%2ds", raiseInfo, (10 - aLong)));
                    } else {
                        upwards.set(false);
                        putDownBaby();
                    }
                });
    }

    private void putDownBaby() {
        final String putDownInfo = ResourceUtil.getString(R.string.put_down_baby);
        putDownBabyDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .take(12)
                .subscribe(bLong -> {
                    if (bLong <= 10) {
                        scaleInfo.set(String.format(Locale.US, "%s%2ds", putDownInfo, (10 - bLong)));
                        readLowestWeight();
                        if (testWeight()) {
                            stopDisposable();
                            initialize();
                        }
                    } else {
                        stopDisposable();
                        initialize();
                        String message = ResourceUtil.getString(R.string.no_record_added);
                        Toast toast = Toast.makeText(MainApplication.getInstance(), message, Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
    }

    public void saveWeight() {
        int newWeight = shareMemory.SC.get();
        if (newWeight < 0) {
            newWeight = 0;
        }
        daoControl.saveWeight(newWeight - weightOffset);
    }


    private void readLowestWeight() {
        int newWeight = shareMemory.SC.get();
        if (Math.abs(newWeight - tempLowestWeight) < PUT_DOWN_VIBRATE) {
            if (newWeight < lowestWeight) {
                lowestWeight = newWeight;
            }
        }
        tempLowestWeight = newWeight;
    }

    private boolean testWeight() {
        int newWeight = shareMemory.SC.get();
        if (newWeight >= lowestWeight + BABY_WEIGHT_THRESHOLD) {
            if (weightCount == 0) {
                targetWeight = newWeight;
                weightCount = 1;
            } else if (Math.abs(targetWeight - newWeight) < RAISE_VIBRATE && putDownBabyDisposable != null) {
                weightCount++;
                targetWeight = newWeight;
                if (weightCount >= 3) {
                    recordedWeight.set(targetWeight - lowestWeight);
                    saveWeight(targetWeight - lowestWeight);
                    return true;
                }
            } else {
                targetWeight = newWeight;
                weightCount = 0;
            }
        }
        return false;
    }

    private void saveWeight(int weight) {
        daoControl.saveWeight(weight);
    }

    @Override
    public void attach() {
        initialize();
    }

    @Override
    public void detach() {
        stopDisposable();
    }

    private void initialize() {
        resetMode.set(false);
        upwards.set(true);

        lowestWeight = Integer.MAX_VALUE;
        targetWeight = 0;
        weightCount = 0;

        raiseBabyDisposable = null;
        putDownBabyDisposable = null;
    }

    private synchronized void stopDisposable() {
        if (putDownBabyDisposable != null) {
            putDownBabyDisposable.dispose();
            putDownBabyDisposable = null;
        }

        if (raiseBabyDisposable != null) {
            raiseBabyDisposable.dispose();
            raiseBabyDisposable = null;
        }
    }
}