package com.david.incubator.ui.setting;

import android.app.AlertDialog;
import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.apkfuns.logutils.LogUtils;
import com.david.R;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.UserModel;
import com.david.common.mode.BloodTypeMode;
import com.david.common.ui.BindingConstraintLayout;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;
import com.david.databinding.LayoutSettingAddUserBinding;
import com.david.incubator.control.IncubatorAutomationControl;
import com.david.incubator.ui.common.KeyEditTextViewModel;
import com.david.incubator.ui.common.KeyValueViewModel;
import com.david.incubator.ui.main.top.TopViewModel;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

/**
 * author: Ling Lin
 * created on: 2018/1/3 19:32
 * email: 10525677@qq.com
 * description:
 */

public class SettingAddUserLayout extends BindingConstraintLayout<LayoutSettingAddUserBinding> implements TextWatcher {

    @Inject
    DaoControl daoControl;
    @Inject
    TopViewModel topViewModel;
    @Inject
    IncubatorAutomationControl automationControl;

    public ObservableBoolean addUser = new ObservableBoolean(false);
    public ObservableBoolean okSelected = new ObservableBoolean(false);

    KeyEditTextViewModel nameKeyEditTextViewModel;
    KeyEditTextViewModel idKeyEditTextViewModel;
    KeyValueViewModel sexKeyValueViewModel;
    KeyValueViewModel bloodTypeKeyValueViewModel;
    KeyValueViewModel birthdayKeyValueViewModel;
    KeyValueViewModel birthWeightKeyValueViewModel;
    KeyValueViewModel gestationKeyValueViewModel;
    KeyEditTextViewModel medicalHistoryKeyEditTextViewModel;

    public ObservableBoolean sex = new ObservableBoolean(false);
    public ObservableField<BloodTypeMode> bloodType = new ObservableField<>(BloodTypeMode.A);
    public ObservableInt gestation = new ObservableInt(40);
    public ObservableInt birthWeight = new ObservableInt(0);

    private AlertDialog alertDialog = null;

    public SettingAddUserLayout(Context context) {
        super(context);
        MainApplication.getInstance().getApplicationComponent().inject(this);

        binding.setViewModel(this);

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        addUser.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (addUser.get()) {
                    binding.addPatientName.setEnabled(true);
                    binding.addPatientId.setEnabled(true);
                    binding.addPatientBirthMedicalHistory.setEnabled(true);
                    binding.addPatientSex.setEnabled(true);
                    binding.addPatientBloodType.setEnabled(true);
                    binding.addPatientBirthday.setEnabled(true);
                    binding.addPatientGestation.setEnabled(true);
                    binding.addPatientBirthWeight.setEnabled(true);

                    nameKeyEditTextViewModel.valueField.set("");
                    idKeyEditTextViewModel.valueField.set("");
                    medicalHistoryKeyEditTextViewModel.valueField.set("");
                    sex.notifyChange();
                    bloodType.notifyChange();
                    birthWeight.notifyChange();
                    gestation.notifyChange();
                    Calendar calendar = Calendar.getInstance();
                    String date = String.format(Locale.US, "%02d-%02d-%02d", calendar.get(Calendar.YEAR) % 100,
                            calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    birthdayKeyValueViewModel.setValueField(date);

                } else {
                    binding.addPatientName.setEnabled(false);
                    binding.addPatientId.setEnabled(false);
                    binding.addPatientBirthMedicalHistory.setEnabled(false);
                    binding.addPatientSex.setEnabled(false);
                    binding.addPatientBloodType.setEnabled(false);
                    binding.addPatientBirthday.setEnabled(false);
                    binding.addPatientGestation.setEnabled(false);
                    binding.addPatientBirthWeight.setEnabled(false);

                    UserModel userModel = daoControl.getLastUserModel();
                    if (userModel != null) {
                        nameKeyEditTextViewModel.valueField.set(userModel.getName());
                        idKeyEditTextViewModel.valueField.set(userModel.getUserId());
                        medicalHistoryKeyEditTextViewModel.valueField.set(userModel.getHistory());
                        sexKeyValueViewModel.valueField.set(getSex(sex.get()));
                        bloodTypeKeyValueViewModel.valueField.set(bloodType.get().getName());
                        gestationKeyValueViewModel.valueField.set(String.valueOf(gestation.get()));
                        birthdayKeyValueViewModel.valueField.set(userModel.getBirthday());
                        birthWeightKeyValueViewModel.valueField.set(String.valueOf(birthWeight.get()));
                    }
                }
            }
        });

        sex.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                sexKeyValueViewModel.valueField.set(getSex(sex.get()));
            }
        });

        birthWeight.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                birthWeightKeyValueViewModel.valueField.set(String.valueOf(birthWeight.get()));
            }
        });

        bloodType.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                bloodTypeKeyValueViewModel.valueField.set(bloodType.get().getName());
            }
        });

        gestation.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                gestationKeyValueViewModel.valueField.set(String.valueOf(gestation.get()));
            }
        });

        binding.addPatientSex.setListener(o -> {
            okSelected.set(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.dialog);
            builder.setTitle(ResourceUtil.getString(R.string.sex));
            String[] items = new String[]{ResourceUtil.getString(R.string.male), ResourceUtil.getString(R.string.female)};
            builder.setItems(items, (dialog, which) -> sex.set(which == 0));

            alertDialog = builder.create();
            alertDialog.show();
            setWidth(alertDialog.getWindow());
        });

        binding.addPatientBloodType.setListener(o -> {
            okSelected.set(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.dialog);
            builder.setTitle(ResourceUtil.getString(R.string.blood_type));
            String[] items = new String[]{BloodTypeMode.A.getName(), BloodTypeMode.B.getName(),
                    BloodTypeMode.AB.getName(), BloodTypeMode.O.getName(), BloodTypeMode.Other.getName()};
            builder.setItems(items, (dialog, which) -> bloodType.set(BloodTypeMode.values()[which]));
            alertDialog = builder.create();
            alertDialog.show();
            setWidth(alertDialog.getWindow());
        });

        binding.addPatientBirthday.setListener(o -> {
            okSelected.set(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.dialog);
            builder.setTitle(ResourceUtil.getString(R.string.birthday));

            View view = inflater.inflate(R.layout.dialog_birth, null);
            builder.setView(view);

            final DatePicker datePicker = view.findViewById(R.id.dpDate);

            Calendar calendar = Calendar.getInstance();

            datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH), null);
            datePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);

            builder.setPositiveButton(R.string.ok, (dialog, which) -> {
                String date = String.format(Locale.US, "%02d-%02d-%02d", datePicker.getYear() % 100,
                        datePicker.getMonth() + 1, datePicker.getDayOfMonth());
                birthdayKeyValueViewModel.setValueField(date);
            });
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            });

            alertDialog = builder.create();
            alertDialog.show();
        });

        binding.addPatientBirthWeight.setListener(o -> {
            okSelected.set(false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.dialog);
            builder.setTitle(ResourceUtil.getString(R.string.birth_weight));

            View view = inflater.inflate(R.layout.dialog_weight, null);
            builder.setView(view);

            NumberPicker numberPicker1 = view.findViewById(R.id.numberPicker1);
            numberPicker1.setMinValue(0);
            numberPicker1.setMaxValue(9);
            numberPicker1.setValue(birthWeight.get() / 1000);
            numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            NumberPicker numberPicker2 = view.findViewById(R.id.numberPicker2);
            numberPicker2.setMinValue(0);
            numberPicker2.setMaxValue(9);
            numberPicker2.setValue(birthWeight.get() / 100 % 10);
            numberPicker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            NumberPicker numberPicker3 = view.findViewById(R.id.numberPicker3);
            numberPicker3.setMinValue(0);
            numberPicker3.setMaxValue(9);
            numberPicker3.setValue(birthWeight.get() / 10 % 10);
            numberPicker3.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            NumberPicker numberPicker4 = view.findViewById(R.id.numberPicker4);
            numberPicker4.setMinValue(0);
            numberPicker4.setMaxValue(9);
            numberPicker4.setValue(birthWeight.get() % 10);
            numberPicker4.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            builder.setPositiveButton(R.string.ok, (dialog, which) ->
                    birthWeight.set(numberPicker1.getValue() * 1000 + numberPicker2.getValue() * 100 +
                            numberPicker3.getValue() * 10 + numberPicker4.getValue()));
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            });

            alertDialog = builder.create();
            alertDialog.show();
        });

        binding.addPatientGestation.setListener(o -> {
            okSelected.set(false);

            AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.dialog);
            builder.setTitle(ResourceUtil.getString(R.string.gestation));

            View view = inflater.inflate(R.layout.dialog_gestation, null);
            builder.setView(view);

            NumberPicker numberPicker1 = view.findViewById(R.id.numberPicker1);
            numberPicker1.setMinValue(0);
            numberPicker1.setMaxValue(9);
            numberPicker1.setValue(gestation.get() / 10 % 10);
            numberPicker1.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            NumberPicker numberPicker2 = view.findViewById(R.id.numberPicker2);
            numberPicker2.setMinValue(0);
            numberPicker2.setMaxValue(9);
            numberPicker2.setValue(gestation.get() % 10);
            numberPicker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            builder.setPositiveButton(R.string.ok, (dialog, which) ->
                    gestation.set(numberPicker1.getValue() * 10 + numberPicker2.getValue()));
            builder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            });

            alertDialog = builder.create();
            alertDialog.show();
        });

        RxView.clicks(binding.btAddUser)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> addUser.set(true));

        RxView.clicks(binding.ibOK)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    try {
                        UserModel userModel = new UserModel();

                        String nameValue = binding.addPatientName.getValueField();
                        if (nameValue == null || nameValue.length() == 0) {
                            binding.addPatientName.requestFocus();
                            return;
                        }
                        userModel.setName(nameValue);

                        String idString = binding.addPatientId.getValueField();
                        if (idString == null || idString.length() == 0) {
                            binding.addPatientId.requestFocus();
                            return;
                        }
                        userModel.setUserId(idString);

                        userModel.setSex(sexKeyValueViewModel.valueField.get().equals(ResourceUtil.getString(R.string.male)));
                        userModel.setBloodGroup(bloodTypeKeyValueViewModel.valueField.get());
                        userModel.setBirthday(birthdayKeyValueViewModel.valueField.get());
                        userModel.setWeight(Integer.parseInt(birthWeightKeyValueViewModel.valueField.get()));
                        userModel.setGestationalAge(Integer.parseInt(gestationKeyValueViewModel.valueField.get()));
                        userModel.setHistory(binding.addPatientBirthMedicalHistory.getValueField());

                        if (daoControl.addUserModel(userModel)) {
                            topViewModel.loadUserId();
                            okSelected.set(true);
                            addUser.set(false);
                        } else {
                            okSelected.set(false);
                        }
                    } catch (Exception e) {
                        okSelected.set(false);
                        LogUtils.e(e);
                    }
                });

        RxView.clicks(binding.ibReturn)
                .throttleFirst(Constant.BUTTON_CLICK_TIMEOUT, TimeUnit.MILLISECONDS)
                .subscribe((aVoid) -> {
                    okSelected.set(false);
                    addUser.set(false);
                });
        init();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_setting_add_user;
    }

    private void init() {
        binding.title.setTitle(R.string.patient_information);

        nameKeyEditTextViewModel = new KeyEditTextViewModel(R.string.name);
        binding.addPatientName.setViewModel(nameKeyEditTextViewModel);
        binding.addPatientName.setValueTextChangedListener(this);

        idKeyEditTextViewModel = new KeyEditTextViewModel(R.string.id);
        binding.addPatientId.setViewModel(idKeyEditTextViewModel);
        binding.addPatientId.setValueTextChangedListener(this);

        medicalHistoryKeyEditTextViewModel = new KeyEditTextViewModel(R.string.medical_history);
        binding.addPatientBirthMedicalHistory.setViewModel(medicalHistoryKeyEditTextViewModel);
        binding.addPatientBirthMedicalHistory.setValueTextChangedListener(this);

        sexKeyValueViewModel = new KeyValueViewModel(R.string.sex);
        sexKeyValueViewModel.unitTextVisibility.set(false);
        sexKeyValueViewModel.unitFigureVisibility.set(false);
        binding.addPatientSex.setViewModel(sexKeyValueViewModel);

        bloodTypeKeyValueViewModel = new KeyValueViewModel(R.string.blood_type);
        bloodTypeKeyValueViewModel.unitTextVisibility.set(false);
        bloodTypeKeyValueViewModel.unitFigureVisibility.set(false);
        binding.addPatientBloodType.setViewModel(bloodTypeKeyValueViewModel);

        birthdayKeyValueViewModel = new KeyValueViewModel(R.string.birthday);
        birthdayKeyValueViewModel.unitTextVisibility.set(false);
        birthdayKeyValueViewModel.unitFigureVisibility.set(false);
        binding.addPatientBirthday.setViewModel(birthdayKeyValueViewModel);

        birthWeightKeyValueViewModel = new KeyValueViewModel(R.string.birth_weight);
        birthWeightKeyValueViewModel.setUnitText("g");
        binding.addPatientBirthWeight.setViewModel(birthWeightKeyValueViewModel);

        gestationKeyValueViewModel = new KeyValueViewModel(R.string.gestation);
        gestationKeyValueViewModel.setUnitText(R.string.week);
        binding.addPatientGestation.setViewModel(gestationKeyValueViewModel);


    }

    @Override
    public void attach() {
        addUser.notifyChange();
    }

    @Override
    public void detach() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
        closeKeyboard();
    }

    private void setWidth(Window window) {
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics d = getResources().getDisplayMetrics();
        lp.width = (int) (d.widthPixels * 0.3);
        window.setAttributes(lp);
    }

    private void closeKeyboard() {
        View view = this.getRootView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) MainApplication.getInstance()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        automationControl.initializeTimeOut();
        okSelected.set(false);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

//    private void showKeyboard(){
//        InputMethodManager imm = (InputMethodManager) getContext( ).getSystemService(Context.INPUT_METHOD_SERVICE);
//           imm.showSoftInput(editText,InputMethodManager.SHOW_FORCED);
//    }

    private String getSex(boolean status) {
        if (status) {
            return ResourceUtil.getString(R.string.male);
        } else {
            return ResourceUtil.getString(R.string.female);
        }
    }
}