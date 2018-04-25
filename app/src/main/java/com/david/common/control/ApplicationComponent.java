package com.david.common.control;

import com.david.incubator.control.IncubatorApplication;
import com.david.incubator.ui.top.TopLayout;

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
}
