package com.david.incubator.ui.system.otherparameter;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.StatusCommand;
import com.david.common.serial.SerialControl;
import com.david.common.serial.command.other.DigitalCommand;
import com.david.common.ui.ITabConstraintLayout;
import com.david.common.ui.TabConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.david.databinding.LayoutSystemOtherParameterBinding;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/19 20:59
 * email: 10525677@qq.com
 * description:
 */
public class SystemOtherParameterLayout extends TabConstraintLayout<LayoutSystemOtherParameterBinding> implements ITabConstraintLayout {

    @Inject
    SerialControl serialControl;

    ObservableInt navigationView;

    public ObservableField<String> analogAllValue = new ObservableField<>();
    public ObservableField<String> statusValue = new ObservableField<>();
    public ObservableField<String> digitalValue = new ObservableField<>();

    private final String analogFormat = "S1A=%s;S1B=%s;S2=%s;S3=%s;A1=%s;A2=%s;A3=%s;F1=%s;H1=%s;O1=%s;O2=%s;O3=%s;SP=%s;PR=%s;PI=%s;VB=%s;VR=%s;VU=%s;T1=%s;T2=%s;T3=%s;SC=%s";
    private final String statusFormat = "Mode=%s;Ctrl=%s;Time=%s;CTime=%s;Warm=%s;Inc=%s;Hum=%s;O2=%s;Alert=%s";
    private final String digitalFormat = "Do=%s;Dc=%s;Ho=%s;Hc=%s;Mo=%s;Ms=%s;Ds=%s;Ps=%s;Ts=%s;WTs=%s;Hs=%s;Is1=%s;Is2=%s;WMs=%s;Fan=%s";

    public SystemOtherParameterLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(this);

        binding.title.setTitle(R.string.other_parameter);
        ButtonControlViewModel buttonControlViewModel = new ButtonControlViewModel();
        buttonControlViewModel.showUpDown.set(false);
        buttonControlViewModel.showOK.set(false);
        binding.buttonControl.setViewModel(buttonControlViewModel);

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.SYSTEM_HOME));
    }

    private void init() {
        AnalogCommand analogCommand = new AnalogCommand();
        analogCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                if (baseSerialMessage instanceof AnalogCommand) {
                    AnalogCommand command = (AnalogCommand) baseSerialMessage;
                    analogAllValue.set(String.format(analogFormat,
                            command.getS1A(), command.getS1B(), command.getS2(), command.getS3(),
                            command.getA1(), command.getA2(), command.getA3(), command.getF1(), command.getH1(),
                            command.getO1(), command.getO2(), command.getO3(), command.getSP(), command.getPR(),
                            command.getPI(), command.getVB(), command.getVR(), command.getVU(),
                            command.getT1(), command.getT2(), command.getT3(), command.getSC()));
                }
            }
        });
        serialControl.addRepeatSession(getKey(analogCommand.getClass()), analogCommand);

        StatusCommand statusCommand = new StatusCommand();
        statusCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                if (baseSerialMessage instanceof StatusCommand) {
                    StatusCommand command = (StatusCommand) baseSerialMessage;
                    statusValue.set(String.format(statusFormat,
                            command.getMode(), command.getCtrl(), command.getTime(), command.getCTime(),
                            command.getWarm(), command.getInc(), command.getHum(), command.getO2(),
                            command.getAlert()));
                }
            }
        });
        serialControl.addRepeatSession(getKey(statusCommand.getClass()), statusCommand);

        DigitalCommand digitalCommand = new DigitalCommand();
        digitalCommand.setOnCompleted((aBoolean, baseSerialMessage) -> {
            if (aBoolean) {
                if (baseSerialMessage instanceof DigitalCommand) {
                    DigitalCommand command = (DigitalCommand) baseSerialMessage;
                    digitalValue.set(String.format(digitalFormat,
                            command.getDo(), command.getDc(), command.getHo(), command.getHc(),
                            command.getMo(), command.getMs(), command.getDs(), command.getPs(),
                            command.getTs(), command.getWTs(), command.getHs(), command.getIs1(),
                            command.getIs2(), command.getWMs(), command.getFan()));
                }
            }
        });
        serialControl.addRepeatSession(getKey(digitalCommand.getClass()), digitalCommand);
    }

    private String getKey(Class clazz) {
        return clazz.getSimpleName() + "E";
    }

    @Override
    public void attach() {
        init();
    }

    @Override
    public void detach() {
        serialControl.removeRepeatSession(getKey(AnalogCommand.class));
        serialControl.removeRepeatSession(getKey(StatusCommand.class));
        serialControl.removeRepeatSession(getKey(DigitalCommand.class));
    }

    @Override
    public void stopDisposable() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_system_other_parameter;
    }
}
