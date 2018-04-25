package com.david.common.data;

import android.databinding.ObservableBoolean;

import com.david.common.dao.UserModel;
import com.david.common.ui.IViewModel;

import javax.inject.Inject;

public class UserModelData implements IViewModel {

    public UserModel userModel;
    public ObservableBoolean showSignsOfData = new ObservableBoolean(false);

    @Inject
    UserModelData() {
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
