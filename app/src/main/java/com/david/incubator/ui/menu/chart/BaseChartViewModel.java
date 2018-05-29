package com.david.incubator.ui.menu.chart;

import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.view.View;

import com.david.common.ui.IViewModel;
import com.david.incubator.ui.menu.chart.chartview.BaseChartViewWriter;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2017/12/31 15:36
 * email: 10525677@qq.com
 * description:
 */
public abstract class BaseChartViewModel<T> implements IViewModel, IRefreshableViewModel, IChartViewModel<T> {

    protected final int[] CYCLE_VALUE_ARRAY = {2, 4, 8, 12, 24, 48};
    protected final int[] CYCLE_VALUE_TIME = {1, 2, 4, 6, 12, 24};

    protected BaseChartViewWriter baseChartViewWriter;
    protected PageTurnTable pageTurnTable;

    public ObservableInt cycleValue = new ObservableInt();
    public ObservableInt cycleIndex = new ObservableInt();
    public ObservableInt yAxisTitle = new ObservableInt();

    public ObservableBoolean tableSelected = new ObservableBoolean(false);

    public BaseChartViewModel(BaseChartViewWriter baseChartViewWriter,
                              PageTurnTable pageTurnTable) {
        this.baseChartViewWriter = baseChartViewWriter;
        this.pageTurnTable = pageTurnTable;
        cycleIndex.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                cycleValue.set(CYCLE_VALUE_ARRAY[cycleIndex.get()]);
                refresh();
            }
        });

        tableSelected.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                boolean status = ((ObservableBoolean) observable).get();
                setVisiblePage(status);
                refresh();
            }
        });
    }

    public abstract void initialize();

    public abstract int getColor();

    public abstract void initializePageTurnTable();

    public abstract int getYAxisTitle();

    public synchronized void increase() {
        if (cycleIndex.get() < CYCLE_VALUE_ARRAY.length - 1) {
            cycleIndex.set(cycleIndex.get() + 1);
        }
    }

    public synchronized void decrease() {
        if (cycleIndex.get() > 0) {
            cycleIndex.set(cycleIndex.get() - 1);
        }
    }

    public synchronized void goNext() {
        pageTurnTable.goNext();
    }

    public synchronized void goPrevious() {
        pageTurnTable.goPrevious();
    }

    //x轴时间间隔
    protected int getStep() {
        //每点间隔分钟数
        int step = CYCLE_VALUE_TIME[cycleIndex.get()] * 60;
        //间隔20个点
        return step * 20;
    }

    @Override
    public void attach() {
        cycleIndex.set(0);
        cycleValue.set(CYCLE_VALUE_ARRAY[cycleIndex.get()]);
        initialize();
        initializePageTurnTable();
        tableSelected.set(false);
        setVisiblePage(false);
        refresh();
    }

    @Override
    public void refresh() {
        io.reactivex.Observable.just(this)
                .observeOn(Schedulers.io())
                .subscribe(obj -> {
                    synchronized (this) {
                        if (!tableSelected.get()) {
                            baseChartViewWriter.removeAllLine();
                            int limit = 120 * CYCLE_VALUE_TIME[cycleIndex.get()];
                            baseChartViewWriter.draw(this, limit, getStep(), CYCLE_VALUE_TIME[cycleIndex.get()]);
                            addLine();
                            baseChartViewWriter.postInvalidate();
                        }
                    }
                });
    }

    private void setVisiblePage(boolean checked) {
        baseChartViewWriter.setVisibility(!checked);
        if (checked) {
            yAxisTitle.set(0);
            pageTurnTable.setVisibility(View.VISIBLE);
            pageTurnTable.start();
        } else {
            yAxisTitle.set(getYAxisTitle());
            pageTurnTable.stop();
            pageTurnTable.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public synchronized void detach() {
        pageTurnTable.stop();
    }

    protected void addLine() {
        baseChartViewWriter.addLine(getColor());
    }

    public boolean isCabin() {
        return true;
    }
}
