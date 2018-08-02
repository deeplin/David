package com.david.incubator.ui.objective.cabin;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.david.common.control.MainApplication;
import com.david.common.data.ModuleHardware;
import com.david.incubator.ui.objective.cabin.humidity.ObjectiveHumidityLayout;
import com.david.incubator.ui.objective.cabin.humidity.ObjectiveHumidityViewModel;
import com.david.incubator.ui.objective.cabin.oxygen.ObjectiveOxygenViewModel;
import com.david.incubator.ui.objective.cabin.pr.ObjectivePrViewModel;
import com.david.incubator.ui.objective.cabin.spo2.ObjectiveSpo2Layout;
import com.david.incubator.ui.objective.cabin.spo2.ObjectiveSpo2ViewModel;
import com.david.incubator.ui.objective.cabin.temp.ObjectiveTempLayout;
import com.david.incubator.ui.objective.warmer.jaundice.WarmerObjectiveJaundiceLayout;
import com.david.incubator.ui.objective.warmer.jaundice.WarmerObjectiveJaundiceViewModel;

import java.util.Objects;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2017/12/29 16:18
 * email: 10525677@qq.com
 * description:
 */
public class ObjectivePagerAdapter extends PagerAdapter {

    @Inject
    ModuleHardware moduleHardware;

    private int count;
    private int[] tabArray;
    private int[] positionArray;

    public ObjectivePagerAdapter() {
        super();
        MainApplication.getInstance().getApplicationComponent().inject(this);

        count = 0;
        tabArray = new int[6];
        positionArray = new int[6];

        positionArray[0] = count;
        tabArray[count] = 0;
        count++;

        if (moduleHardware.isHUM()) {
            positionArray[1] = count;
            tabArray[count] = 1;
            count++;
        } else {
            positionArray[1] = -1;
        }

        if (moduleHardware.isO2()) {
            positionArray[2] = count;
            tabArray[count] = 2;
            count++;
        } else {
            positionArray[2] = -1;
        }

        if (moduleHardware.isSPO2()) {
            positionArray[3] = count;
            tabArray[count] = 3;
            count++;
            positionArray[4] = count;
            tabArray[count] = 4;
            count++;
        } else {
            positionArray[3] = -1;
            positionArray[4] = -1;
        }

        if (moduleHardware.isJaundiceInstalled()) {
            positionArray[5] = count;
            tabArray[count] = 5;
            count++;
        } else {
            positionArray[5] = -1;
        }
    }

    public int getPosition(int position) {
        return positionArray[position];
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return Objects.equals(view, object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view;
        int tabId = tabArray[position];

        switch (tabId) {
            case 0:
                view = new ObjectiveTempLayout(container.getContext());
                break;
            case 1:
                ObjectiveHumidityViewModel objectiveHumidityViewModel = new ObjectiveHumidityViewModel();
                ObjectiveHumidityLayout objectiveHumidityLayout = new ObjectiveHumidityLayout(container.getContext(), objectiveHumidityViewModel);
                objectiveHumidityLayout.setUnit("%RH");
                view = objectiveHumidityLayout;
                break;
            case 2:
                ObjectiveOxygenViewModel objectiveOxygenViewModel = new ObjectiveOxygenViewModel();
                ObjectiveHumidityLayout objectiveOxygenLayout = new ObjectiveHumidityLayout(container.getContext(), objectiveOxygenViewModel);
                objectiveOxygenLayout.setUnit("%");
                view = objectiveOxygenLayout;
                break;
            case 3:
                ObjectiveSpo2ViewModel objectiveSpo2ViewModel = new ObjectiveSpo2ViewModel();
                view = new ObjectiveSpo2Layout(container.getContext(), objectiveSpo2ViewModel);
                break;
            case 4:
                ObjectivePrViewModel objectivePrViewModel = new ObjectivePrViewModel();
                view = new ObjectiveSpo2Layout(container.getContext(), objectivePrViewModel);
                break;
            case 5:
                WarmerObjectiveJaundiceViewModel warmerObjectiveJaundiceViewModel =
                        new WarmerObjectiveJaundiceViewModel();
                view = new WarmerObjectiveJaundiceLayout(container.getContext(), warmerObjectiveJaundiceViewModel);
                break;
            default:
                view = new View(container.getContext());
                break;
        }
        container.addView(view);
        view.setTag(position);
        return view;
    }
}
