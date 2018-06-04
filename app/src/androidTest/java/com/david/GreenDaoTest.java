package com.david;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.david.common.control.MainApplication;
import com.david.common.dao.gen.DaoMaster;
import com.david.common.dao.gen.DaoSession;

import org.greenrobot.greendao.database.Database;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class GreenDaoTest {

    private DaoMaster.DevOpenHelper devOpenHelper;
    private Database database;
    private DaoSession daoSession;

    public GreenDaoTest(){
//        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.david", appContext.getPackageName());
    }
}
