package com.david.incubator.ui.user.usermodel;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.View;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.UserModel;
import com.david.common.data.ShareMemory;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.incubator.ui.common.ButtonControlViewModel;
import com.david.incubator.ui.common.KeyValueView;
import com.david.incubator.ui.common.KeyValueViewModel;
import com.jakewharton.rxbinding2.view.RxView;
import com.david.databinding.LayoutUserModelBinding;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:10
 * email: 10525677@qq.com
 * description:
 */

public class UserModelLayout extends BindingConstraintLayout<LayoutUserModelBinding> {

    @Inject
    DaoControl daoControl;
    @Inject
    ShareMemory shareMemory;
    @Inject
    UserModelDetailViewModel userModelDetailViewModel;

    ButtonControlViewModel buttonControlViewModel;

    ObservableInt navigationView;

    private long currentUserModelId = -1;

    public UserModelLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(this);

        init();

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibUp))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> increaseValue());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibDown))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> decreaseValue());

        RxView.clicks(binding.buttonControl.findViewById(R.id.ibReturn))
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_HOME));
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_model;
    }

    @Override
    public void attach() {
    }

    @Override
    public void detach() {
    }

    private void increaseValue() {
        if (currentUserModelId > 0)
            currentUserModelId += 8;
        reload();
    }

    private void decreaseValue() {
        reload();
    }

    private void init() {
        binding.title.setTitle(R.string.patient_information);

        buttonControlViewModel = new ButtonControlViewModel();
        buttonControlViewModel.showOK.set(false);
        binding.buttonControl.setViewModel(buttonControlViewModel);

        setListener(binding.userModel1);
        setListener(binding.userModel2);
        setListener(binding.userModel3);
        setListener(binding.userModel4);
        setListener(binding.userModel5);
        setListener(binding.userModel6);
        setListener(binding.userModel7);
        setListener(binding.userModel8);
        reload();
    }

    private void reload() {
        List<UserModel> userModelList = daoControl.getUserModel(8, currentUserModelId);
        int index = 0;
        addViewModel(binding.userModel1, userModelList, index++);
        addViewModel(binding.userModel2, userModelList, index++);
        addViewModel(binding.userModel3, userModelList, index++);
        addViewModel(binding.userModel4, userModelList, index++);
        addViewModel(binding.userModel5, userModelList, index++);
        addViewModel(binding.userModel6, userModelList, index++);
        addViewModel(binding.userModel7, userModelList, index++);
        addViewModel(binding.userModel8, userModelList, index);
    }

    private void addViewModel(KeyValueView keyValueView, List<UserModel> userModelList, int index) {
        if (userModelList != null && userModelList.size() > index) {
            keyValueView.setVisibility(View.VISIBLE);
            UserModel userModel = userModelList.get(index);
            KeyValueViewModel keyValueViewModel = new KeyValueViewModel(R.string.id);
            keyValueViewModel.setValueField(userModel.getUserId());
            keyValueView.setViewModel(keyValueViewModel);
            keyValueView.setTag(userModel);
            currentUserModelId = userModel.getId() - 1;
        } else {
            keyValueView.setVisibility(View.INVISIBLE);
        }
    }

    private void setListener(KeyValueView keyValueView) {
        keyValueView.setListener(o -> {
            userModelDetailViewModel.userModel = (UserModel) keyValueView.getTag();
            navigationView.set(FragmentPage.USER_MODEL_DETAIL);
        });
    }
}
