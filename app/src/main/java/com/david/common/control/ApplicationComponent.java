package com.david.common.control;

import com.david.incubator.control.IncubatorApplication;
import com.david.incubator.ui.main.MainActivity;
import com.david.incubator.ui.main.MainViewModel;
import com.david.incubator.ui.main.side.SideLayout;
import com.david.incubator.ui.main.side.SideViewModel;
import com.david.incubator.ui.menu.MenuLayout;
import com.david.incubator.ui.main.top.TopLayout;

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

}
