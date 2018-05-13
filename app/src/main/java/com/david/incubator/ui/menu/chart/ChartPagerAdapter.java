package com.david.incubator.ui.menu.chart;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.david.common.control.DaoControl;
import com.david.common.data.ModuleHardware;
import com.david.common.data.ModuleSoftware;
import com.david.common.data.ShareMemory;
import com.david.incubator.ui.menu.chart.table.PageTurnTable;

/**
 * author: Ling Lin
 * created on: 2017/12/31 16:39
 * email: 10525677@qq.com
 * description:
 */

public class ChartPagerAdapter extends PagerAdapter {

    private SensorChartView sensorChartView;
    private PageTurnTable pageTurnTable;
    private ShareMemory shareMemory;
    private ModuleSoftware moduleSoftware;
    private DaoControl daoControl;

    private int count;
    private final int[] tabArray;

    public ChartPagerAdapter(boolean isCabin, SensorChartView sensorChartView,
                      PageTurnTable pageTurnTable, ModuleHardware moduleHardware,
                      ShareMemory shareMemory, ModuleSoftware moduleSoftware,
                      DaoControl daoControl) {
        this.sensorChartView = sensorChartView;
        this.pageTurnTable = pageTurnTable;
        this.shareMemory = shareMemory;
        this.moduleSoftware = moduleSoftware;
        this.daoControl = daoControl;

        count = 0;
        tabArray = new int[6];

        tabArray[count] = 0;
        count++;

        tabArray[count] = 1;
        count++;

        if (isCabin) {
            if (moduleHardware.isHUM()) {
                tabArray[count] = 2;
                count++;
            }

            if (moduleHardware.isO2()) {
                tabArray[count] = 3;
                count++;
            }
        }

        if (moduleHardware.isSPO2()) {
            tabArray[count] = 4;
            count++;
            tabArray[count] = 5;
            count++;
        }
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return view.equals(object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view;
        int tabId = tabArray[position];
        switch (tabId) {
//            case 0:
//                TempChartViewModel chartTempViewModel = new TempChartViewModel(
//                        sensorChartView, pageTurnTable, shareMemory, daoControl);
//                view = new ChartTempLayout(container.getContext(), chartTempViewModel);
//                break;
//            case 1:
//                BaseChartViewModel heatingViewModel = new HeatingChartViewModel(
//                        sensorChartView, pageTurnTable, shareMemory);
//                view = new ChartBaseLayout(container.getContext(), heatingViewModel);
//                break;
//            case 2:
//                BaseChartViewModel humidityViewModel = new HumidityChartViewModel(
//                        sensorChartView, pageTurnTable, shareMemory, moduleSoftware);
//                view = new ChartBaseLayout(container.getContext(), humidityViewModel);
//                break;
//            case 3:
//                BaseChartViewModel oxygenViewModel = new OxygenChartViewModel(
//                        sensorChartView, pageTurnTable, shareMemory, moduleSoftware);
//                view = new ChartBaseLayout(container.getContext(), oxygenViewModel);
//                break;
//            case 4:
//                BaseChartViewModel spo2ViewModel = new Spo2ChartViewModel(sensorChartView, pageTurnTable);
//                view = new ChartBaseLayout(container.getContext(), spo2ViewModel);
//                break;
//            case 5:
//                BaseChartViewModel prViewModel = new PrChartViewModel(sensorChartView, pageTurnTable);
//                view = new ChartBaseLayout(container.getContext(), prViewModel);
//                break;
            default:
                view = new View(container.getContext());
                break;
        }

        container.addView(view);
        view.setTag(position);
        return view;
    }
}
