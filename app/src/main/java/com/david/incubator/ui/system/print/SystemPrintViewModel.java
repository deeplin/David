package com.david.incubator.ui.system.print;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.CtrlGetCommand;
import com.david.common.dao.StatusCommand;
import com.david.common.dao.WeightModel;
import com.david.common.dao.gen.AnalogCommandDao;
import com.david.common.dao.gen.CtrlGetCommandDao;
import com.david.common.dao.gen.DaoSession;
import com.david.common.dao.gen.StatusCommandDao;
import com.david.common.dao.gen.WeightModelDao;
import com.david.common.data.ModuleHardware;
import com.david.common.serial.PrintSerialControl;
import com.david.common.serial.command.other.PrintCommand;
import com.david.common.util.LogUtil;
import com.david.common.util.ResourceUtil;
import com.david.common.util.TimeUtil;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.util.ViewUtil;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/19 9:27
 * email: 10525677@qq.com
 * description:
 */
public class SystemPrintViewModel {

    @Inject
    public PrintSerialControl printSerialControl;
    @Inject
    ModuleHardware moduleHardware;
    @Inject
    DaoControl daoControl;

    ButtonControlViewModel buttonControlViewModel;

    public ObservableBoolean chartSelected = new ObservableBoolean(true);
    public ObservableField<String> dataValue = new ObservableField<>();
    public ObservableInt dataCycle = new ObservableInt(1);
    public ObservableInt scaleCycle = new ObservableInt(1);

    private final static String DATA_TEMPLATE_LINE_1 = "Printing Date: %s\n********************************\n\n";
    private final static String DATA_TEMPLATE_LINE_2 = "Description\n%s Infant Incubator \n";
    private final static String DATA_TEMPLATE_LINE_3 = "A:Air S:Skin F:AirFlow\n";
    private final static String DATA_TEMPLATE_LINE_4 = "H:Humidity SC:Scale\n";

    private final static String DATA_TEMPLATE_ITEM_1 = "S1A:%s℃ S1B:%s℃ S2:%s℃\n\n";
    private final static String DATA_TEMPLATE_ITEM_2 = "S3:%s℃ A1:%s℃ A2:%s℃\n";
    private final static String DATA_TEMPLATE_ITEM_3 = "A3:%s℃ F1:%s℃ H1:%s%%\n";
    private final static String DATA_TEMPLATE_ITEM_4 = "O1:%s%% O2:%s%% O3:%s%%\n";
    private final static String DATA_TEMPLATE_ITEM_5 = "SP:%s%% PR:%sbpm PI:%s\n";

    private final static String CTRL_GET_TEMPLATE_ITEM_1 = "Date:%s Ctrl:%s\n";
    private final static String CTRL_GET_TEMPLATE_ITEM_2 = "c_air:%s c_hum:%s c_o2:%s\n";
    private final static String CTRL_GET_TEMPLATE_ITEM_3 = "c_skin:%s w_skin:%s\n";
    private final static String CTRL_GET_TEMPLATE_ITEM_4 = "w_man:%s w_inc:%s\n\n";

    private final static String STATUS_TEMPLATE_ITEM_2 = "Alarm:%s\n";
    private final static String STATUS_TEMPLATE_ITEM_1 = "SysMode:%s CtrlMode:%s\n";

    private final static String SCALE_TEMPLATE_ITEM = "SC:%sg\n\n";

    private final static String TIME_ITEM = "Date:%s\n";

    private final static int MAX_DATA_CYCLE = 30;
    private final static int MAX_SCALE_CYCLE = 49;

    public SystemPrintViewModel() {
        MainApplication.getInstance().getApplicationComponent().inject(this);

        buttonControlViewModel = new ButtonControlViewModel();

        chartSelected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (chartSelected.get()) {
                    dataCycle.notifyChange();
                } else {
                    scaleCycle.notifyChange();
                }
            }
        });

        dataCycle.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int value = dataCycle.get();
                dataValue.set(String.format(Locale.US, "%s %2d %s", ResourceUtil.getString(R.string.print_cycle), value, ResourceUtil.getString(R.string.hours)));
            }
        });

        dataCycle.notifyChange();

        scaleCycle.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                int value = scaleCycle.get();
                dataValue.set(String.format(Locale.US, "%s %2d %s", ResourceUtil.getString(R.string.print_cycle), value, ResourceUtil.getString(R.string.days)));
            }
        });
    }

    public void startPrint() {
        if (chartSelected.get()) {
            startPrintData();
        } else {
            startPrintScale();
        }
    }

    private void startPrintData() {
        StringBuilder commandBuffer = new StringBuilder();

        DaoSession daoSession = daoControl.getDaoSession();
        daoSession.clear();
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();
        List<AnalogCommand> analogCommandList = analogCommandDao.queryBuilder().orderDesc(AnalogCommandDao.Properties.Id)
                .limit(dataCycle.get() * 60).build().list();
        StatusCommandDao statusCommandDao = daoSession.getStatusCommandDao();
        List<StatusCommand> statusCommandList = statusCommandDao.queryBuilder()
                .orderDesc(StatusCommandDao.Properties.Id)
                .limit(dataCycle.get() * 60).build().list();

        commandBuffer.append("\n\n\n\n");
        commandBuffer.append(DATA_TEMPLATE_LINE_4);
        commandBuffer.append(DATA_TEMPLATE_LINE_3);
        commandBuffer.append(String.format(DATA_TEMPLATE_LINE_2, moduleHardware.getDeviceModel()));
        commandBuffer.append(String.format(DATA_TEMPLATE_LINE_1, TimeUtil.getCurrentDate(TimeUtil.FullTime)));

        print(commandBuffer);

        for (int index = 0, statusIndex = 0; index < analogCommandList.size(); index++) {
            AnalogCommand analogCommand = analogCommandList.get(index);

            long timeStamp = analogCommand.getTimeStamp();
            String time = TimeUtil.getTime(timeStamp * 1000, TimeUtil.DateTimeWithoutSecond);

            while (statusIndex < statusCommandList.size()) {
                StatusCommand statusCommand = statusCommandList.get(statusIndex);
                if (timeStamp == statusCommand.getTimeStamp()) {
                    commandBuffer.append(String.format(TIME_ITEM, time));
                    printData(statusCommand, commandBuffer);
                    printData(analogCommand, commandBuffer);
                    print(commandBuffer);
                    statusIndex++;
                    break;
                } else if (timeStamp < statusCommand.getTimeStamp()) {
                    statusIndex++;
                } else {
                    break;
                }
            }
        }

        long timeStamp = TimeUtil.getCurrentTimeInSecond();
        CtrlGetCommandDao ctrlGetCommandDao = daoSession.getCtrlGetCommandDao();
        List<CtrlGetCommand> ctrlGetCommandList = ctrlGetCommandDao.queryBuilder().orderDesc(CtrlGetCommandDao.Properties.Id)
                .where(CtrlGetCommandDao.Properties.TimeStamp.ge(timeStamp - dataCycle.get() * 3600)).build().list();

        for (int index = 0; index < ctrlGetCommandList.size(); index++) {
            CtrlGetCommand ctrlGetCommand = ctrlGetCommandList.get(index);
            printData(ctrlGetCommand, commandBuffer);
        }

        print(commandBuffer);
    }

    private void print(StringBuilder stringBuilder){
        try {
            printSerialControl.start();
            PrintCommand printCommand = new PrintCommand(stringBuilder.toString());
            printSerialControl.sendAsync(printCommand);
            stringBuilder.delete(0, stringBuilder.length());
            Thread.sleep(200);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }

    private void printData(AnalogCommand analogCommand, StringBuilder commandBuffer) {
        String S1A = ViewUtil.formatTempValue(analogCommand.getS1A());
        String S1B = ViewUtil.formatTempValue(analogCommand.getS1B());
        String S2 = ViewUtil.formatTempValue(analogCommand.getS2());
        String S3 = ViewUtil.formatTempValue(analogCommand.getS3());
        String A1 = ViewUtil.formatTempValue(analogCommand.getA1());
        String A2 = ViewUtil.formatTempValue(analogCommand.getA2());
        String A3 = ViewUtil.formatTempValue(analogCommand.getA3());
        String F1 = ViewUtil.formatTempValue(analogCommand.getF1());
        String H1 = ViewUtil.formatHumidityValue(analogCommand.getH1());
        String O1 = ViewUtil.formatOxygenValue(analogCommand.getO1());
        String O2 = ViewUtil.formatOxygenValue(analogCommand.getO2());
        String O3 = ViewUtil.formatOxygenValue(analogCommand.getO3());
        String SP = ViewUtil.formatSpo2Value(analogCommand.getSP());
        String PR = ViewUtil.formatPrValue(analogCommand.getPR());
        String PI = ViewUtil.formatPiValue(analogCommand.getPI());

        commandBuffer.append(String.format(DATA_TEMPLATE_ITEM_5, SP, PR, PI));
        commandBuffer.append(String.format(DATA_TEMPLATE_ITEM_4, O1, O2, O3));
        commandBuffer.append(String.format(DATA_TEMPLATE_ITEM_3, A3, F1, H1));
        commandBuffer.append(String.format(DATA_TEMPLATE_ITEM_2, S3, A1, A2));
        commandBuffer.append(String.format(DATA_TEMPLATE_ITEM_1, S1A, S1B, S2));
    }

    private void printData(CtrlGetCommand ctrlGetCommand, StringBuilder commandBuffer) {
        String date = TimeUtil.getTime(ctrlGetCommand.getTimeStamp() * 1000, TimeUtil.DateTimeWithoutSecond);
        String ctrl = ctrlGetCommand.getCtrl();
        String c_air = ViewUtil.formatTempValue(ctrlGetCommand.getC_air());
        String c_hum = ViewUtil.formatHumidityValue(ctrlGetCommand.getC_hum());
        String c_o2 = ViewUtil.formatOxygenValue(ctrlGetCommand.getC_o2());
        String c_skin = ViewUtil.formatTempValue(ctrlGetCommand.getC_skin());
        String w_skin = ViewUtil.formatTempValue(ctrlGetCommand.getW_skin());
        String w_man = String.valueOf(ctrlGetCommand.getW_man());
        String w_inc = String.valueOf(ctrlGetCommand.getW_inc());

        commandBuffer.append(String.format(CTRL_GET_TEMPLATE_ITEM_1, date, ctrl));
        commandBuffer.append(String.format(CTRL_GET_TEMPLATE_ITEM_2, c_air, c_hum, c_o2));
        commandBuffer.append(String.format(CTRL_GET_TEMPLATE_ITEM_3, c_skin, w_skin));
        commandBuffer.append(String.format(CTRL_GET_TEMPLATE_ITEM_4, w_man, w_inc));
    }

    private void printData(StatusCommand statusCommand, StringBuilder commandBuffer) {
        String systemMode = statusCommand.getMode();
        String ctrlMode = statusCommand.getCtrl();
        String alert = statusCommand.getAlert();

        commandBuffer.append(String.format(Locale.US, STATUS_TEMPLATE_ITEM_2, alert));
        commandBuffer.append(String.format(Locale.US, STATUS_TEMPLATE_ITEM_1, systemMode, ctrlMode));
    }

    private void startPrintScale() {
        StringBuilder commandBuffer = new StringBuilder();

        DaoSession daoSession = daoControl.getDaoSession();
        daoSession.clear();
        WeightModelDao weightModelDao = daoSession.getWeightModelDao();
        List<WeightModel> commandList = weightModelDao.queryBuilder()
                .orderDesc(WeightModelDao.Properties.Id)
                .limit(scaleCycle.get() * 24).build().list();

        commandBuffer.append("\n\n\n\n");
        commandBuffer.append(DATA_TEMPLATE_LINE_4);
        commandBuffer.append(DATA_TEMPLATE_LINE_3);
        commandBuffer.append(String.format(DATA_TEMPLATE_LINE_2, moduleHardware.getDeviceModel()));
        commandBuffer.append(String.format(DATA_TEMPLATE_LINE_1, TimeUtil.getCurrentDate(TimeUtil.FullTime)));

        print(commandBuffer);

        for (int index = 0; index < commandList.size(); index++) {
            WeightModel weightModel = commandList.get(index);

            String SC = ViewUtil.formatScaleValue(weightModel.getSC());
            String time = TimeUtil.getTime(weightModel.getTimeStamp() * 1000, TimeUtil.DateTimeWithoutSecond);

            commandBuffer.append(String.format(TIME_ITEM, time));
            commandBuffer.append(String.format(SCALE_TEMPLATE_ITEM, SC));
            print(commandBuffer);
        }
        print(commandBuffer);
    }

    public void increase() {
        if (chartSelected.get()) {
            int value = dataCycle.get() + 1;
            if (value <= MAX_DATA_CYCLE) {
                dataCycle.set(value);
            }
        } else {
            int value = scaleCycle.get() + 1;
            if (value <= MAX_SCALE_CYCLE) {
                scaleCycle.set(value);
            }
        }
    }

    public void decrease() {
        if (chartSelected.get()) {
            int value = dataCycle.get() - 1;
            if (value >= 1) {
                dataCycle.set(value);
            }
        } else {
            int value = scaleCycle.get() - 1;
            if (value >= 1) {
                scaleCycle.set(value);
            }
        }
    }
}