package com.david.common.ui.alarm;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.david.R;

public class AlarmViewHolder extends RecyclerView.ViewHolder {

    TextView tvAlarm;

    public AlarmViewHolder(View itemView) {
        super(itemView);
        tvAlarm = itemView.findViewById(R.id.tvAlarm);
    }
}
