package com.david.common.ui.alarm;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.R;
import com.david.common.alarm.AlarmControl;
import com.david.common.alarm.AlarmModel;
import com.david.common.alarm.AlarmPriorityMode;
import com.david.common.control.MainApplication;
import com.david.common.serial.command.alert.AlertListCommand;
import com.david.incubator.ui.main.MainActivity;

import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    @Inject
    AlertListCommand alertListCommand;

    private final String[] alarmArray;

    @Inject
    public AlarmAdapter() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
        this.alarmArray = alertListCommand.getAlarmArray();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        String alarmId = alarmArray[position];

        AlarmModel alarmModel = AlarmControl.getAlarmMode(alarmId);
        if(alarmModel != null) {
            holder.tvAlarm.setText(alarmModel.toString());
        }else{
            holder.tvAlarm.setText(alarmId);
        }
        if (Objects.equals(alarmModel.getAlarmPriorityMode(), AlarmPriorityMode.High)) {
            holder.tvAlarm.setBackgroundResource(R.color.alarm_high);
        } else if (Objects.equals(alarmModel.getAlarmPriorityMode(), AlarmPriorityMode.Middle)) {
            holder.tvAlarm.setBackgroundResource(R.color.alarm_middle);
        } else {
            holder.tvAlarm.setBackgroundResource(R.color.alarm_low);
        }
    }

    @Override
    public int getItemCount() {
        return alertListCommand.getACount();
    }
}