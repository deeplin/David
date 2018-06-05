package com.david;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component
public interface TestComponent {

    void inject(GreenDaoTest greenDaoTest);

}
