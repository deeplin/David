package com.david.common.ui.alarm;

import android.content.Context;
import android.databinding.Observable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.david.R;
import com.david.common.alarm.AlarmControl;
import com.david.common.control.MainApplication;
import com.david.common.serial.command.alert.AlertListCommand;
import com.david.common.ui.IViewModel;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlarmView extends FrameLayout implements IViewModel {

    @Inject
    AlarmControl alarmControl;

    @Inject
    AlarmAdapter alarmAdapter;

    Observable.OnPropertyChangedCallback alarmCallback;

    public AlarmView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.layout_alarm, this);

        RecyclerView recyclerView = findViewById(R.id.rvAlarm);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(alarmAdapter);

        //        recyclerView.addItemDecoration(new DividerItemDecoration(context,
//                DividerItemDecoration.VERTICAL));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

        alarmCallback = new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                io.reactivex.Observable.just(this)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe((o) -> alarmAdapter.notifyDataSetChanged());
            }
        };
    }

    @Override
    public void attach() {
        alarmControl.alarmListUpdated.addOnPropertyChangedCallback(alarmCallback);
    }

    @Override
    public void detach() {
        alarmControl.alarmListUpdated.removeOnPropertyChangedCallback(alarmCallback);
    }
}
