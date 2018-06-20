package com.david.incubator.ui.user;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.common.util.ResourceUtil;
import com.david.databinding.LayoutUserTimeBinding;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.util.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.io.DataOutputStream;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:10
 * email: 10525677@qq.com
 * description:
 */

public class UserTimeLayout extends BindingConstraintLayout<LayoutUserTimeBinding>
        implements DatePicker.OnDateChangedListener, TimePicker.OnTimeChangedListener {

    @Inject
    DaoControl daoControl;

    public ObservableField<String> dateString = new ObservableField<>();
    public ObservableField<String> timeString = new ObservableField<>();

    ObservableInt navigationView;
    ButtonControlViewModel buttonControlViewModel;

    private AlertDialog alertDialog = null;

    public UserTimeLayout(Context context, ObservableInt navigationView) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);
        this.navigationView = navigationView;

        binding.setViewModel(this);

        binding.title.setTitle(R.string.time_setup);
        buttonControlViewModel = new ButtonControlViewModel();
        buttonControlViewModel.showUpDown.set(false);
        binding.buttonControl.setViewModel(buttonControlViewModel);

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibOK))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> alertDialog = ViewUtil.buildConfirmDialog(this.getContext(), R.string.time_update,
                        ResourceUtil.getString(R.string.time_update_confirm),
                        (dialog, which) -> {
                            setTime();
                            daoControl.deleteTables();
                        }));

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_HOME));
    }

    @Override
    public void attach() {
        Calendar calendar = Calendar.getInstance();

        binding.dpTimer.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
        binding.dpTimer.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

        String date = String.format(Locale.US, "%04d-%02d-%02d", calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        dateString.set(date);

        binding.tpTimer.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        binding.tpTimer.setCurrentMinute(calendar.get(Calendar.MINUTE));
        binding.tpTimer.setIs24HourView(true);
        binding.tpTimer.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);
        binding.tpTimer.setOnTimeChangedListener(this);

        String time = String.format(Locale.US, "%02d:%02d", calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        timeString.set(time);
    }

    @Override
    public void detach() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_time;
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
        String date = String.format(Locale.US, "%04d-%02d-%02d", i, i1 + 1, i2);
        dateString.set(date);
        buttonControlViewModel.okSelected.set(false);
    }

    @Override
    public void onTimeChanged(TimePicker timePicker, int i, int i1) {
        String time = String.format(Locale.US, "%02d:%02d", i, i1);
        timeString.set(time);
        buttonControlViewModel.okSelected.set(false);
    }

    private void setTime() {
        try {
            String date = dateString.get().replace("-", "");
            String cTime = timeString.get().replace(":", "") + "00";
            Process process = Runtime.getRuntime().exec("su");
            String datetime = date + "." + cTime; //测试的设置的时间【时间格式 yyyyMMdd.HHmmss】
            DataOutputStream dataOutputStream = new DataOutputStream(process.getOutputStream());
            dataOutputStream.writeBytes("setprop persist.sys.timezone GMT\n");
            dataOutputStream.writeBytes("/system/bin/date -s " + datetime + "\n");
            dataOutputStream.writeBytes("clock -w\n");
            dataOutputStream.writeBytes("exit\n");
            dataOutputStream.flush();
            dataOutputStream.close();

            buttonControlViewModel.okSelected.set(true);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }
}