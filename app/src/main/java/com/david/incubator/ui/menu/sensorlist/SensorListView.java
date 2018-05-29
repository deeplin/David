package com.david.incubator.ui.menu.sensorlist;

import android.content.Context;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;

import com.david.R;
import com.david.common.ui.BindingConstraintLayout;
import com.david.databinding.ViewSensorListBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SensorListView extends BindingConstraintLayout<ViewSensorListBinding> {

    public ObservableBoolean showView = new ObservableBoolean(true);

    public ObservableInt setImageIcon = new ObservableInt();
    public ObservableField<String> objective = new ObservableField<>();

    public ObservableField<String> value = new ObservableField<>();
    public ObservableInt valueColor = new ObservableInt();

    public ObservableInt rightTopIcon = new ObservableInt();
    public ObservableField<String> rightTopText = new ObservableField<>();
    public ObservableField<String> rightBottom = new ObservableField<>();

    public SensorListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        binding.setViewModel(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.view_sensor_list;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }

    public void showAnimation(boolean status, int imageId, Animation animation) {
        io.reactivex.Observable.just(this)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((obj) -> {
                    synchronized (this) {
                        if (status) {
                            binding.sensorListAnimation.setImageResource(imageId);
                            binding.sensorListAnimation.startAnimation(animation);
                            binding.sensorListAnimation.setVisibility(View.VISIBLE);
                        } else {
                            binding.sensorListAnimation.setImageResource(0);
                            binding.sensorListAnimation.clearAnimation();
                            binding.sensorListAnimation.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void setBackground(int imageId){
        binding.sensorListAnimation.setImageResource(imageId);
    }
}
