package com.david.common.control;

import android.databinding.ObservableField;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2018/1/25 15:51
 * email: 10525677@qq.com
 * description:
 */
@Singleton
public class AlertControl {

    private String alertID = null;
    public ObservableField<String> alertStringField = new ObservableField<>("");

    @Inject
    public AlertControl() {
//        MainApplication.getInstance().getApplicationComponent().inject(this);
//
//        shareMemory.alertID.addOnPropertyChangedCallback(new android.databinding.Observable.OnPropertyChangedCallback() {
//            @Override
//            public void onPropertyChanged(android.databinding.Observable observable, int i) {
//                String alertID = ((ObservableField<String>) observable).get();
//                if (alertID.equals(Constant.SENSOR_NA_STRING)) {
//                    clearRemoteAlert();
//                } else {
//                    setAlert(AlertPriorityMode.Sys_New_Alert, alertID);
//                }
//            }
//        });
    }

//    /*设置报警*/
//    public synchronized void setAlert(AlertPriorityMode alertPriorityMode, String alertID) {
//        if (alertPriorityMode.getIndex() < this.alertPriorityMode.getIndex()) {
//            if (alertPriorityMode.equals(AlertPriorityMode.Sys_New_Alert)) {
//                this.alertPriorityMode = AlertPriorityMode.Sys_Old_Alert;
//            } else {
//                this.alertPriorityMode = alertPriorityMode;
//            }
//            this.alertID = alertID;
//            alertStringField.set(getAlertDetail(alertID));
//        }
//    }
//
//    private String getAlertDetail(String alertID) {
//        String alertDetail;
//        try {
//            int resourceID = ReflectionUtil.getResourceId(MainApplication.getInstance().getApplicationContext(),
//                    alertID, ReflectionUtil.ResourcesType.string);
//            alertDetail = ResourceUtil.getString(resourceID);
//        } catch (Exception e) {
//            alertDetail = alertID;
//        }
//        return alertDetail;
//    }
//
//    /*是否报警*/
//    public boolean isAlert() {
//        return !alertPriorityMode.equals(AlertPriorityMode.Sys_None);
//    }
//
//    /*是否下位机报警*/
//    public String getAlertID() {
//        return alertID;
//    }
//
//    /*清除所有报警*/
//    public synchronized void clearAlert() {
//        alertPriorityMode = AlertPriorityMode.Sys_None;
//        alertID = null;
//        alertStringField.set("");
//    }
//
//    /*清除下位机产生报警*/
//    void clearRemoteAlert() {
//        if (alertID != null) {
//            clearAlert();
//        }
//    }
}
