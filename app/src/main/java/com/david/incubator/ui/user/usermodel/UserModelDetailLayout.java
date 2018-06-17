package com.david.incubator.ui.user.usermodel;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.ObservableInt;

import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.UserModel;
import com.david.common.data.SelectedUser;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.FragmentPage;
import com.david.common.util.ResourceUtil;
import com.david.databinding.LayoutUserModelDetailBinding;
import com.david.incubator.ui.common.KeyValueViewModel;
import com.david.incubator.ui.main.top.TopViewModel;
import com.david.incubator.util.ViewUtil;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 20:10
 * email: 10525677@qq.com
 * description:
 */

public class UserModelDetailLayout extends BindingConstraintLayout<LayoutUserModelDetailBinding> {

    @Inject
    SelectedUser selectedUser;
    @Inject
    DaoControl daoControl;
    @Inject
    TopViewModel topViewModel;

    KeyValueViewModel nameKeyValueViewModel;
    KeyValueViewModel idKeyValueViewModel;
    KeyValueViewModel sexKeyValueViewModel;
    KeyValueViewModel bloodTypeKeyValueViewModel;
    KeyValueViewModel birthdayKeyValueViewModel;
    KeyValueViewModel birthWeightKeyValueViewModel;
    KeyValueViewModel gestationKeyValueViewModel;
    KeyValueViewModel medicalHistoryKeyValueViewModel;

    ObservableInt navigationView;

    private AlertDialog alertDialog;

    public UserModelDetailLayout(Context context, ObservableInt navigationView) {
        super(context);
        this.navigationView = navigationView;
        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(this);

        init();

        RxView.clicks(binding.btDeletePatient)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> alertDialog = ViewUtil.buildConfirmDialog(context, R.string.delete,
                        ResourceUtil.getString(R.string.delete_confirm),
                        (dialog, which) -> {
                            alertDialog = null;
                            daoControl.deleteUserModel(selectedUser.userModel);
                            topViewModel.loadUserId();
                            navigationView.set(FragmentPage.USER_MODEL);
                        }));

        RxView.clicks(binding.btSignsOfData)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> selectedUser.showDetail.set(true));

        RxView.clicks(binding.btReturn)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> navigationView.set(FragmentPage.USER_MODEL));
    }

    @Override
    public void attach() {
        selectedUser.attach();
    }

    @Override
    public void detach() {
        selectedUser.detach();
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    private void init() {
        binding.title.setTitle(R.string.patient_information);

        UserModel userModel = selectedUser.userModel;

        nameKeyValueViewModel = new KeyValueViewModel(R.string.name);
        nameKeyValueViewModel.setValueField(userModel.getName());
        binding.addPatientName.setViewModel(nameKeyValueViewModel);

        idKeyValueViewModel = new KeyValueViewModel(R.string.id);
        idKeyValueViewModel.setValueField(userModel.getUserId());
        binding.addPatientId.setViewModel(idKeyValueViewModel);

        sexKeyValueViewModel = new KeyValueViewModel(R.string.sex);
        if (selectedUser.userModel.getSex()) {
            sexKeyValueViewModel.setValueField(ResourceUtil.getString(R.string.male));
        } else {
            sexKeyValueViewModel.setValueField(ResourceUtil.getString(R.string.female));
        }
        binding.addPatientSex.setViewModel(sexKeyValueViewModel);

        bloodTypeKeyValueViewModel = new KeyValueViewModel(R.string.blood_type);
        bloodTypeKeyValueViewModel.setValueField(userModel.getBloodGroup());
        binding.addPatientBloodType.setViewModel(bloodTypeKeyValueViewModel);

        birthdayKeyValueViewModel = new KeyValueViewModel(R.string.birthday);
        birthdayKeyValueViewModel.setValueField(userModel.getBirthday());
        binding.addPatientBirthday.setViewModel(birthdayKeyValueViewModel);

        birthWeightKeyValueViewModel = new KeyValueViewModel(R.string.birth_weight);
        birthWeightKeyValueViewModel.setValueField(String.valueOf(userModel.getWeight()));
        birthWeightKeyValueViewModel.setUnitText("g");
        binding.addPatientBirthWeight.setViewModel(birthWeightKeyValueViewModel);

        gestationKeyValueViewModel = new KeyValueViewModel(R.string.gestation);
        gestationKeyValueViewModel.setValueField(String.valueOf(userModel.getGestationalAge()));
        gestationKeyValueViewModel.setUnitText(R.string.week);
        binding.addPatientGestation.setViewModel(gestationKeyValueViewModel);

        medicalHistoryKeyValueViewModel = new KeyValueViewModel(R.string.medical_history);
        medicalHistoryKeyValueViewModel.setValueField(userModel.getHistory());
        binding.addPatientBirthMedicalHistory.setViewModel(medicalHistoryKeyValueViewModel);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_user_model_detail;
    }
}