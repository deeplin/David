package com.david.common.control;

import android.content.Context;

import com.david.common.dao.SystemSetting;
import com.david.common.dao.gen.DaoMaster;
import com.david.common.dao.gen.DaoSession;
import com.david.common.dao.gen.SystemSettingDao;
import com.david.common.data.UserModelData;
import com.david.R;
import com.david.common.mode.LanguageMode;
import com.david.common.util.ResourceUtil;

import org.greenrobot.greendao.database.Database;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * author: Ling Lin
 * created on: 2017/7/20 11:09
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class DaoControl {

    @Inject
    UserModelData userModelData;

    private DaoMaster.DevOpenHelper devOpenHelper;
    private Database database;
    private DaoSession daoSession;

    @Inject
    public DaoControl() {
        MainApplication.getInstance().getApplicationComponent().inject(this);
    }

    public void start(Context applicationContext) throws Exception {
        devOpenHelper = new DaoMaster.DevOpenHelper(applicationContext, "DavidDatabase");
        database = devOpenHelper.getWritableDb();
        daoSession = new DaoMaster(database).newSession();

        initSystemSetting();
    }

    public void stop() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null) {
            database.close();
            database = null;
        }
        if (devOpenHelper != null) {
            devOpenHelper.close();
            devOpenHelper = null;
        }
    }

    private void initSystemSetting(){
        SystemSettingDao systemSettingDao = daoSession.getSystemSettingDao();
        SystemSetting systemSetting = systemSettingDao.queryBuilder()
                .where(SystemSettingDao.Properties.Id.eq(0L))
                .unique();
        if (systemSetting == null) {
            /*读取语言设置*/
            String languageIndex = ResourceUtil.getString(R.string.language);

            LanguageMode languageMode = LanguageMode.getMode(languageIndex);
            byte languageModeId = languageMode.getIndex();
            systemSetting = new SystemSetting();
            systemSetting.setLanguageIndex(languageModeId);
            systemSettingDao.insert(systemSetting);
        }
    }
}
