package com.david.incubator.ui.menu.scale.chart;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.david.R;
import com.david.common.control.MainApplication;
import com.david.common.dao.WeightModel;
import com.david.common.ui.IViewModel;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;
import com.david.common.util.SensorRange;
import com.david.incubator.ui.menu.chart.IChartViewModel;
import com.david.incubator.ui.menu.chart.IRefreshableViewModel;
import com.david.incubator.ui.menu.chart.SensorChartView;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

import java.util.Locale;

import io.reactivex.schedulers.Schedulers;

public class ScaleChartViewModel implements IViewModel, IRefreshableViewModel, IChartViewModel<WeightModel> {

    protected ScaleChartWriter baseChartViewWriter;
    protected PageTurnTable pageTurnTable;

    public ObservableBoolean tableSelected = new ObservableBoolean(false);
    public ObservableField<String> lastWeight = new ObservableField<>();

    public ScaleChartViewModel(SensorChartView sensorChartView, PageTurnTable pageTurnTable) {
        this.baseChartViewWriter = new ScaleChartWriter(sensorChartView);
        this.pageTurnTable = pageTurnTable;

        tableSelected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean status = ((ObservableBoolean) observable).get();
                setVisiblePage(status);
                refresh();
            }
        });
    }

    public synchronized void goNext() {
        pageTurnTable.goNext();
    }

    public synchronized void goPrevious() {
        pageTurnTable.goPrevious();
    }

    @Override
    public void attach() {
        baseChartViewWriter.attach();
        initialize();
        initializePageTurnTable();
        this.tableSelected.set(false);
        setVisiblePage(false);
        refresh();
    }

    @Override
    public void detach() {
        pageTurnTable.stop();
        baseChartViewWriter.clearXAxis();
        baseChartViewWriter.detach();
    }

    @Override
    public void refresh() {
        io.reactivex.Observable.just(this)
                .observeOn(Schedulers.io())
                .subscribe(obj -> {
                    synchronized (this) {
                        if (!tableSelected.get()) {
                            baseChartViewWriter.removeAllLine();
                            baseChartViewWriter.draw(this, 0, 0, 0);
                            addLine();

                            if (baseChartViewWriter.getLastWeight() > Constant.SENSOR_NA_VALUE) {
                                this.setLastWeight(baseChartViewWriter.getLastWeight());
                            }

                            baseChartViewWriter.postInvalidate();
                        }
                    }
                });
    }

    public void setLastWeight(int weight) {
        lastWeight.set(String.format(Locale.US, "%s: %d",
                ResourceUtil.getString(R.string.current_weight), weight));
    }

    private void setVisiblePage(boolean checked) {
        baseChartViewWriter.setVisibility(!checked);
        if (checked) {
            pageTurnTable.setVisibility(View.VISIBLE);
            pageTurnTable.start();
        } else {
            pageTurnTable.stop();
            pageTurnTable.setVisibility(View.INVISIBLE);
        }
    }

    protected void addLine() {
        baseChartViewWriter.addLine(ContextCompat.getColor(MainApplication.getInstance(), R.color.scale));
    }

    private void initialize() {
        baseChartViewWriter.initializeYAxis(8000.0, 0.0, 500.0, 2.0);
    }

    private void initializePageTurnTable() {
        pageTurnTable.initialize(new ScaleDataRetriever());
    }

    @Override
    public double getChartData(WeightModel weightModel) {
        if (weightModel != null) {
            int value = weightModel.getSC();
            if (value < SensorRange.SCALE_DISPLAY_LOWER + 100) {
                value = SensorRange.SCALE_DISPLAY_LOWER + 100;
            } else if (value > SensorRange.SCALE_DISPLAY_UPPER) {
                value = SensorRange.SCALE_DISPLAY_UPPER;
            }
            return (value);
        }
        return 0.0;
    }

    @Override
    public double getObjective() {
        return Constant.SENSOR_NA_VALUE * 1.0;
    }
}
