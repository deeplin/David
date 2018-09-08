package com.david.common.data;

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
public class SelectedUser{

    public UserModel userModel;
    public ObservableBoolean showDetail = new ObservableBoolean(false);

    @Inject
    SelectedUser() {
    }
}