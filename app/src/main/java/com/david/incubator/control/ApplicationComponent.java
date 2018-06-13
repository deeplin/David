package com.david.incubator.control;

import com.david.common.control.AutomationControl;
import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.control.MessageSender;
import com.david.common.data.ShareMemory;
import com.david.common.ui.alarm.AlarmAdapter;
import com.david.common.ui.alarm.AlarmView;
import com.david.incubator.ui.home.cabin.HomeFragment;
import com.david.incubator.ui.home.cabin.HomeViewModel;
import com.david.incubator.ui.home.warmer.HeatingTimingView;
import com.david.incubator.ui.home.warmer.JaunediceData;
import com.david.incubator.ui.home.warmer.JaunediceView;
import com.david.incubator.ui.home.warmer.WarmerHomeFragment;
import com.david.incubator.ui.home.warmer.WarmerViewModel;
import com.david.incubator.ui.main.MainActivity;
import com.david.incubator.ui.main.MainViewModel;
import com.david.incubator.ui.menu.scale.ScaleFragment;
import com.david.incubator.ui.menu.scale.ScaleViewModel;
import com.david.incubator.ui.main.side.SideLayout;
import com.david.incubator.ui.main.side.SideViewModel;
import com.david.incubator.ui.main.top.TopLayout;
import com.david.incubator.ui.main.top.TopViewModel;
import com.david.incubator.ui.menu.MenuLayout;
import com.david.incubator.ui.menu.MenuViewModel;
import com.david.incubator.ui.menu.chart.ChartFragment;
import com.david.incubator.ui.menu.chart.chartview.AnalogChartWriter;
import com.david.incubator.ui.menu.chart.chartview.StatusChartWriter;
import com.david.incubator.ui.menu.chart.table.AnalogDataRetriever;
import com.david.incubator.ui.menu.chart.table.StatusDataRetriever;
import com.david.incubator.ui.menu.scale.chart.ScaleChartLayout;
import com.david.incubator.ui.menu.scale.chart.ScaleChartWriter;
import com.david.incubator.ui.menu.scale.chart.ScaleDataRetriever;
import com.david.incubator.ui.menu.sensorlist.SensorListLayout;
import com.david.incubator.ui.menu.sensorlist.SensorListViewModel;
import com.david.incubator.ui.menu.spo2.Spo2Fragment;
import com.david.incubator.ui.menu.spo2.chart.Spo2ChartSurfaceView;
import com.david.incubator.ui.menu.spo2.setting.Spo2SettingAverageTimeLayout;
import com.david.incubator.ui.menu.spo2.setting.Spo2SettingFastSATLayout;
import com.david.incubator.ui.menu.spo2.setting.Spo2SettingLayout;
import com.david.incubator.ui.menu.spo2.setting.Spo2SettingSensLayout;
import com.david.incubator.ui.menu.spo2.view.Spo2ViewLayout;
import com.david.incubator.ui.objective.cabin.ObjectiveHomeFragment;
import com.david.incubator.ui.objective.cabin.ObjectivePagerAdapter;
import com.david.incubator.ui.objective.cabin.humidity.ObjectiveHumidityViewModel;
import com.david.incubator.ui.objective.cabin.spo2.ObjectiveSpo2ViewModel;
import com.david.incubator.ui.objective.cabin.temp.ObjectiveTempLayout;
import com.david.incubator.ui.objective.cabin.temp.ObjectiveTempViewModel;
import com.david.incubator.ui.objective.warmer.WarmerObjectiveHomeFragment;
import com.david.incubator.ui.objective.warmer.WarmerObjectivePagerAdapter;
import com.david.incubator.ui.objective.warmer.jaundice.WarmerObjectiveJaundiceViewModel;
import com.david.incubator.ui.objective.warmer.temp.WarmerObjectiveTempLayout;
import com.david.incubator.ui.objective.warmer.temp.WarmerObjectiveTempViewModel;
import com.david.incubator.ui.objective.warmer.timing.WarmerObjectiveTimingLayout;
import com.david.incubator.ui.setting.SettingAddPatientLayout;
import com.david.incubator.ui.setting.SettingCalibrationLayout;
import com.david.incubator.ui.setting.SettingDeviceLayout;
import com.david.incubator.ui.setting.SettingHomeFragment;
import com.david.incubator.ui.setting.SettingIntroductionLayout;
import com.david.incubator.ui.system.calibration.SystemCalibrationViewModel;
import com.david.incubator.ui.system.deviation.SystemDeviationViewModel;
import com.david.incubator.ui.system.factory.SystemFactoryLayout;
import com.david.incubator.ui.system.otherparameter.SystemOtherParameterLayout;
import com.david.incubator.ui.system.overheat.SystemOverheatViewModel;
import com.david.incubator.ui.user.UserOverheatExperimentLayout;
import com.david.incubator.ui.system.print.SystemPrintViewModel;
import com.david.incubator.ui.system.range.SystemRangeViewModel;
import com.david.incubator.ui.user.UserLanguageLayout;
import com.david.incubator.ui.user.UserScreenLuminanceLayout;
import com.david.incubator.ui.user.UserTimeLayout;
import com.david.incubator.ui.user.UserWarningVolumeLayout;
import com.david.incubator.ui.user.usermodel.ChartLayout;
import com.david.incubator.ui.user.usermodel.UserModelDetailLayout;
import com.david.incubator.ui.user.usermodel.UserModelLayout;

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

    void inject(TopViewModel topViewModel);

    void inject(AlarmView alarmView);

    void inject(MenuViewModel menuViewModel);

    void inject(HomeFragment homeFragment);

    void inject(HomeViewModel homeViewModel);

    void inject(ObjectiveHomeFragment objectiveFragment);

    void inject(ObjectivePagerAdapter objectivePagerAdapter);

    void inject(ObjectiveTempViewModel objectiveTempViewModel);

    void inject(ObjectiveTempLayout objectiveTempLayout);

    void inject(ObjectiveHumidityViewModel objectiveHumidityViewModel);

    void inject(ObjectiveSpo2ViewModel objectiveSpo2ViewModel);

    void inject(HeatingTimingView heatingTimingView);

    void inject(JaunediceData jaunediceData);

    void inject(WarmerViewModel warmerViewModel);

    void inject(WarmerHomeFragment warmerHomeFragment);

    void inject(JaunediceView jaunediceView);

    void inject(WarmerObjectivePagerAdapter warmerObjectivePagerAdapter);

    void inject(WarmerObjectiveHomeFragment warmerObjectiveFragment);

    void inject(WarmerObjectiveTempViewModel warmerObjectiveTempViewModel);

    void inject(WarmerObjectiveTempLayout warmerObjectiveTempLayout);

    void inject(WarmerObjectiveJaundiceViewModel warmerObjectiveJaundiceViewModel);

    void inject(WarmerObjectiveTimingLayout warmerObjectiveTimingLayout);

    void inject(SettingHomeFragment settingFragment);

    void inject(SensorListViewModel sensorListViewModel);

    void inject(SensorListLayout sensorListLayout);

    void inject(ShareMemory shareMemory);

    void inject(SettingAddPatientLayout settingAddPatientLayout);

    void inject(SettingCalibrationLayout settingCalibrationLayout);

    void inject(SettingIntroductionLayout settingIntroductionLayout);

    void inject(SettingDeviceLayout settingDeviceLayout);

    void inject(UserLanguageLayout userLanguageLayout);

    void inject(UserWarningVolumeLayout userWarningVolumeLayout);

    void inject(UserScreenLuminanceLayout userScreenLuminanceLayout);

    void inject(UserModelLayout userModelLayout);

    void inject(UserModelDetailLayout userModelDetailLayout);

    void inject(SystemCalibrationViewModel systemCalibrationViewModel);

    void inject(SystemDeviationViewModel systemDeviationViewModel);

    void inject(SystemOtherParameterLayout systemOtherParameterLayout);

    void inject(SystemOverheatViewModel systemOverheatViewModel);

    void inject(UserOverheatExperimentLayout userOverheatExperimentLayout);

    void inject(SystemPrintViewModel systemPrintViewModel);

    void inject(SystemRangeViewModel systemRangeViewModel);

    void inject(SystemFactoryLayout systemFactoryLayout);

    void inject(ChartFragment chartFragment);

    void inject(AnalogChartWriter analogChartWriter);

    void inject(StatusChartWriter statusChartWriter);

    void inject(AnalogDataRetriever analogDataRetriever);

    void inject(StatusDataRetriever statusDataRetriever);

    void inject(ChartLayout chartLayout);

    void inject(ScaleViewModel scaleViewModel);

    void inject(ScaleFragment scaleFragment);

    void inject(ScaleDataRetriever scaleDataRetriever);

    void inject(ScaleChartWriter scaleChartWriter);

    void inject(ScaleChartLayout scaleChartLayout);

    void inject(Spo2Fragment spo2Fragment);

    void inject(Spo2ViewLayout spo2ViewLayout);

    void inject(Spo2SettingLayout spo2SettingLayout);

    void inject(Spo2SettingAverageTimeLayout spo2SettingAverageTimeLayout);

    void inject(Spo2SettingFastSATLayout spo2SettingFastSATLayout);

    void inject(Spo2SettingSensLayout spo2SettingSensLayout);

    void inject(Spo2ChartSurfaceView spo2ChartSurfaceView);

    void inject(AlarmAdapter alarmAdapter);

    void inject(UserTimeLayout userTimeLayout);


    /*Test*/
//    void inject(GreenDaoTest  greenDaoTest);
}
