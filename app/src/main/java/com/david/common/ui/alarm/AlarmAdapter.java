package com.david.common.ui.alarm;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.david.R;
import com.david.common.alarm.AlarmModel;
import com.david.common.alarm.AlarmPriorityMode;
import com.david.common.serial.command.alert.AlertListCommand;

import java.util.List;
import java.util.Objects;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder> {

    private final AlertListCommand alertListCommand;
    private final List<AlarmModel> alarmModelList;

    public AlarmAdapter(AlertListCommand alertListCommand) {
        this.alertListCommand = alertListCommand;
        this.alarmModelList = alertListCommand.getAlertList();
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm, parent, false);
        return new AlarmViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder holder, int position) {
        AlarmModel alarmModel = alarmModelList.get(position);
        holder.tvAlarm.setText(alarmModel.toString());
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
        return alertListCommand.getAlertCount();
    }
}
