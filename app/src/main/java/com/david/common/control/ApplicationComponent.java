package com.david.common.control;

import com.david.common.ui.alarm.AlarmView;
import com.david.incubator.control.IncubatorApplication;
import com.david.incubator.ui.home.cabin.HomeFragment;
import com.david.incubator.ui.home.cabin.HomeViewModel;
import com.david.incubator.ui.main.MainActivity;
import com.david.incubator.ui.main.MainViewModel;
import com.david.incubator.ui.main.side.SideLayout;
import com.david.incubator.ui.main.side.SideViewModel;
import com.david.incubator.ui.main.top.TopLayout;
import com.david.incubator.ui.main.top.TopViewModel;
import com.david.incubator.ui.menu.MenuLayout;
import com.david.incubator.ui.menu.MenuViewModel;
import com.david.incubator.ui.objective.cabin.ObjectiveFragment;
import com.david.incubator.ui.objective.cabin.ObjectivePagerAdapter;
import com.david.incubator.ui.objective.cabin.humidity.ObjectiveHumidityViewModel;
import com.david.incubator.ui.objective.cabin.spo2.ObjectiveSpo2ViewModel;
import com.david.incubator.ui.objective.cabin.temp.ObjectiveTempLayout;
import com.david.incubator.ui.objective.cabin.temp.ObjectiveTempViewModel;

import javax.inject.Singleton;

import dagger.Component;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:33
 * email: 10525677@qq.com
 * description:
 */
@Singleton
@Component
public interface ApplicationComponent {

    void inject(MainApplication mainApplication);

    void inject(IncubatorApplication incubatorApplication);

    void inject(TopLayout topLayout);

    void inject(MessageSender messageSender);

    void inject(DaoControl daoControl);

    void inject(AutomationControl automationControl);

    void inject(MainActivity mainActivity);

    void inject(MainViewModel mainViewModel);

    void inject(MenuLayout menuLayout);

    void inject(SideViewModel sideViewModel);

    void inject(SideLayout sideLayout);

    void inject(TopViewModel topViewModel);

    void inject(AlarmView alarmView);

    void inject(MenuViewModel menuViewModel);

    void inject(HomeFragment homeFragment);

    void inject(HomeViewModel homeViewModel);

    void inject(ObjectiveFragment objectiveFragment);

    void inject(ObjectivePagerAdapter objectivePagerAdapter);

    void inject(ObjectiveTempViewModel objectiveTempViewModel);

    void inject(ObjectiveTempLayout objectiveTempLayout);

    void inject(ObjectiveHumidityViewModel objectiveHumidityViewModel);

    void inject(ObjectiveSpo2ViewModel objectiveSpo2ViewModel);
}
