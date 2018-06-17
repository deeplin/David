package com.david;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.david.common.control.DaoControl;
import com.david.common.control.MainApplication;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.SystemSetting;
import com.david.common.dao.WeightModel;
import com.david.common.dao.gen.DaoMaster;
import com.david.common.dao.gen.DaoSession;
import com.david.common.dao.gen.WeightModelDao;
import com.david.common.util.TimeUtil;
import com.david.incubator.control.ApplicationComponent;
import com.david.incubator.control.IncubatorApplication;
import com.david.incubator.ui.main.MainActivity;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import javax.inject.Singleton;

import dagger.Component;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class GreenDaoTest {

    @Test
    public void useSystemSetting() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        DaoControl daoControl = new DaoControl();
        daoControl.start(MainApplication.getInstance());

        SystemSetting systemSetting = daoControl.getSystemSetting();

        for(int index = 0; index < 100; index ++) {
            daoControl.increaseJaunediceTime();
        }

        Log.e("deeplin", "" + systemSetting.getId() + " " +
                systemSetting.getBlueTime() +" " + systemSetting.getLuminance() + " " + systemSetting.getVolume());

        daoControl.stop();

        delay();
        assertEquals("com.david", appContext.getPackageName());
    }

    @Test
    public void useAnalogCommand() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        DaoControl daoControl = new DaoControl();
        daoControl.start(MainApplication.getInstance());

//        daoControl.deleteTables();
//
//        AnalogCommand analogCommand = new AnalogCommand();
//        analogCommand.setS1A(1);
//        analogCommand.setS1B(2);
//        analogCommand.setS2(3);
//        analogCommand.setS3(4);
//
//        for(int index = 0; index < 1000; index ++){
//            analogCommand.setA1(index);
//            daoControl.saveCommand(analogCommand);
//        }

        List<AnalogCommand> analogCommandList = daoControl.getAnalogCommand(100);
        for(AnalogCommand command : analogCommandList){
            Log.e("deeplin",command.getId() + " " + command.getA1() + " " + command.getTimeStamp());
        }
        Log.e("deeplin", ""+ analogCommandList.size());

        daoControl.stop();

        delay();
        assertEquals("com.david", appContext.getPackageName());
    }

    private void delay(){
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
