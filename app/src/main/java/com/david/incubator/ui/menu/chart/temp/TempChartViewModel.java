package com.david.incubator.ui.menu.chart.temp;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.support.v4.content.ContextCompat;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.gen.AnalogCommandDao;
import com.david.common.dao.gen.DaoSession;
import com.david.common.dao.gen.StatusCommandDao;
import com.david.common.dao.gen.WeightModelDao;
import com.david.common.data.ShareMemory;
import com.david.common.mode.CtrlMode;
import com.david.common.util.Constant;
import com.david.incubator.ui.menu.chart.BaseChartViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;
import com.david.incubator.ui.menu.chart.chartview.AnalogChartWriter;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * author: Ling Lin
 * created on: 2018/1/1 20:08
 * email: 10525677@qq.com
 * description:
 */
public class TempChartViewModel extends BaseChartViewModel<AnalogCommand> {

    public ShareMemory shareMemory;
    private DaoControl daoControl;

    public ObservableBoolean skin1Selected = new ObservableBoolean(true);
    public ObservableBoolean skin2Selected = new ObservableBoolean(true);
    public ObservableBoolean airSelected = new ObservableBoolean(true);
    public ObservableBoolean airVisible = new ObservableBoolean(true);

    private List<Double> airSeries;
    private List<Double> skin2Series;

    private final int airColor;
    private final int skin2Color;

    public TempChartViewModel(SensorChartView sensorChartView, PageTurnTable pageTurnTable,
                              ShareMemory shareMemory, DaoControl daoControl) {
        super(new AnalogChartWriter(sensorChartView), pageTurnTable);
        this.shareMemory = shareMemory;
        this.daoControl = daoControl;

        airSeries = new ArrayList<>();
        airColor = ContextCompat.getColor(MainApplication.getInstance(), R.color.c_air);
        skin2Series = new ArrayList<>();
        skin2Color = ContextCompat.getColor(MainApplication.getInstance(), R.color.skin2);

        if (shareMemory.isCabin()) {
            airVisible.set(true);
            airSelected.set(true);
        } else {
            airVisible.set(false);
            airSelected.set(false);
        }

        airSelected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                attach();
            }
        });

        skin1Selected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                attach();
            }
        });

        skin2Selected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                attach();
            }
        });
    }

    @Override
    public void initialize() {
        baseChartViewWriter.initializeYAxis(45.0, 20.0, 2.5, 2.0);
    }

    @Override
    public void initializePageTurnTable() {
        pageTurnTable.initialize(new TempDataRetriever());
    }

    @Override
    public String getYAxisTitle() {
        return "℃";
    }

    @Override
    public int getColor() {
        return ContextCompat.getColor(MainApplication.getInstance(), R.color.skin1);
    }

    @Override
    public void refresh() {
        airSeries.clear();
        skin2Series.clear();
        super.refresh();
    }

    @Override
    public synchronized void detach() {
        super.detach();
    }

    private int filterTemp(int value, int low, int high) {
        if (value < low) {
            value = low;
        } else if (value > high) {
            value = high;
        }
        return value;
    }

    @Override
    protected void addLine() {
        if (skin1Selected.get()) {
            super.addLine();
        }

        if (airSelected.get()) {
            baseChartViewWriter.addLine(airColor, airSeries);
        }
        if (skin2Selected.get()) {
            baseChartViewWriter.addLine(skin2Color, skin2Series);
        }
    }

    @Override
    public double getChartData(AnalogCommand analogCommand) {
        if (analogCommand == null) {
            airSeries.add(20.0);
            skin2Series.add(20.0);
            return 20.0;
        } else {
            int value = analogCommand.getS1B();
            value = filterTemp(value, 201, 450);

            int air = analogCommand.getA2();
            air = filterTemp(air, 203, 452);
            airSeries.add(air / 10.0);

            int skin2 = analogCommand.getS2();
            skin2 = filterTemp(skin2, 205, 454);
            skin2Series.add(skin2 / 10.0);
            return (value / 10.0);
        }
    }

    @Override
    public double getObjective() {
        if (Objects.equals(shareMemory.ctrlMode.get(), CtrlMode.Skin)) {
            return shareMemory.skinObjective.get() / 10.0;
        } else if (Objects.equals(shareMemory.ctrlMode.get(), CtrlMode.Air)) {
            return shareMemory.airObjective.get() / 10.0;
        } else {
            return Constant.SENSOR_NA_VALUE * 1.0;
        }
    }

    @Override
    public boolean isCabin() {
        return shareMemory.isCabin();
    }

    public void delete() {
        DaoSession daoSession = daoControl.getDaoSession();
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();
        analogCommandDao.deleteAll();

        StatusCommandDao statusCommandDao = daoSession.getStatusCommandDao();
        statusCommandDao.deleteAll();

        WeightModelDao weightModelDao = daoSession.getWeightModelDao();
        weightModelDao.deleteAll();

        airSeries.clear();
        skin2Series.clear();

        attach();
    }
}
