package com.david.incubator.ui.user.usermodel;

import android.databinding.ObservableBoolean;

import com.david.common.dao.UserModel;
import com.david.common.ui.IViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:10
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class UserModelDetailViewModel implements IViewModel {

    public UserModel userModel;
    public ObservableBoolean showSignsOfData = new ObservableBoolean(false);

    @Inject
    UserModelDetailViewModel() {
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
        showSignsOfData.set(false);
        userModel = null;
    }
}
