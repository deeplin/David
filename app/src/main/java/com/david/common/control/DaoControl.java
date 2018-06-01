package com.david.common.control;

import android.content.Context;

import com.david.R;
import com.david.common.dao.AnalogCommand;
import com.david.common.dao.CtrlGetCommand;
import com.david.common.dao.Spo2GetCommand;
import com.david.common.dao.StatusCommand;
import com.david.common.dao.SystemSetting;
import com.david.common.dao.UserModel;
import com.david.common.dao.WeightModel;
import com.david.common.dao.gen.AnalogCommandDao;
import com.david.common.dao.gen.CtrlGetCommandDao;
import com.david.common.dao.gen.DaoMaster;
import com.david.common.dao.gen.DaoSession;
import com.david.common.dao.gen.Spo2GetCommandDao;
import com.david.common.dao.gen.StatusCommandDao;
import com.david.common.dao.gen.SystemSettingDao;
import com.david.common.dao.gen.UserModelDao;
import com.david.common.dao.gen.WeightModelDao;
import com.david.common.data.UserModelData;
import com.david.common.mode.LanguageMode;
import com.david.common.util.Constant;
import com.david.common.util.ResourceUtil;
import com.david.common.util.TimeUtil;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

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

    public void start(Context applicationContext) {
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

    private void initSystemSetting() {
        SystemSetting systemSetting = getSystemSetting();
        if (systemSetting == null) {
            /*读取语言设置*/
            String languageIndex = ResourceUtil.getString(R.string.language);
            LanguageMode languageMode = LanguageMode.getMode(languageIndex);
            byte languageModeId = languageMode.getIndex();
            systemSetting = new SystemSetting();
            systemSetting.setLanguageIndex(languageModeId);
            SystemSettingDao systemSettingDao = daoSession.getSystemSettingDao();
            systemSettingDao.insert(systemSetting);
        }
    }

    public void deleteTables(){
        database.execSQL("DELETE FROM ANALOG_COMMAND");
        database.execSQL("DELETE FROM STATUS_COMMAND");
        database.execSQL("DELETE FROM WEIGHT_MODEL");
        database.execSQL("DELETE FROM SPO2_GET_COMMAND");
        database.execSQL("DELETE FROM CTRL_GET_COMMAND");
        database.execSQL("DELETE FROM USER_MODEL");
    }

    public void increaseJaunediceTime() {
        SystemSetting systemSetting = getSystemSetting();
        systemSetting.setBlueTime(systemSetting.getBlueTime() + 10);
        SystemSettingDao systemSettingDao = daoSession.getSystemSettingDao();
        systemSettingDao.save(systemSetting);
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public SystemSetting getSystemSetting() {
        SystemSettingDao systemSettingDao = daoSession.getSystemSettingDao();
        return systemSettingDao.queryBuilder()
                .where(SystemSettingDao.Properties.Id.eq(0L))
                .unique();
    }

    public void saveSystemSetting(SystemSetting systemSetting) {
        SystemSettingDao sensorRangeDao = daoSession.getSystemSettingDao();
        sensorRangeDao.save(systemSetting);
    }

    public void saveCommand(AnalogCommand analogCommand) {
        long currentTime = TimeUtil.getCurrentTimeInSecond();
        /*每分钟记录一次*/
        if (currentTime % 60 == 0) {
            analogCommand.setTimeStamp(currentTime);

            AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();
            analogCommandDao.insert(analogCommand);
            /*去除ID*/
            analogCommand.setId(null);
        }
    }

    public void saveCommand(StatusCommand statusCommand) {
        long currentTime = TimeUtil.getCurrentTimeInSecond();
        /*每分钟记录一次*/
        if (currentTime % 60 == 0) {
            statusCommand.setTimeStamp(currentTime);

            StatusCommandDao statusCommandDao = daoSession.getStatusCommandDao();
            statusCommandDao.insert(statusCommand);
            /*去除ID*/
            statusCommand.setId(null);
        }
    }

    public void saveCommand(CtrlGetCommand ctrlGetCommand) {
        if (ctrlGetCommand.isChanged()) {
            long currentTime = TimeUtil.getCurrentTimeInSecond();
            ctrlGetCommand.setTimeStamp(currentTime);

            CtrlGetCommandDao ctrlGetCommandDao = daoSession.getCtrlGetCommandDao();
            ctrlGetCommandDao.insert(ctrlGetCommand);
            /*去除ID*/
            ctrlGetCommand.setId(null);
            ctrlGetCommand.setCtrlGetCommand(ctrlGetCommand);
        }
    }

    public void saveCommand(Spo2GetCommand spo2GetCommand) {
        if (spo2GetCommand.isChanged()) {
            long currentTime = TimeUtil.getCurrentTimeInSecond();
            spo2GetCommand.setTimeStamp(currentTime);

            Spo2GetCommandDao spo2GetCommandDao = daoSession.getSpo2GetCommandDao();
            spo2GetCommandDao.insert(spo2GetCommand);
            /*去除ID*/
            spo2GetCommand.setId(null);

            spo2GetCommand.setSpo2GetCommand(spo2GetCommand);
        }
    }

    public void deleteStale() {
        daoSession.clear();
        long staleTime = TimeUtil.getCurrentTimeInSecond() - Constant.SENSOR_SAVED_IN_DATABASE;
        //delete old analog records
        AnalogCommandDao analogCommandDao = daoSession.getAnalogCommandDao();
        List<AnalogCommand> analogModelsList = analogCommandDao.queryBuilder()
                .where(AnalogCommandDao.Properties.TimeStamp.le(staleTime))
                .build().list();
        for (AnalogCommand oldAnalogCommand : analogModelsList) {
            analogCommandDao.delete(oldAnalogCommand);
        }

        //delete old status records
        StatusCommandDao statusCommandDao = daoSession.getStatusCommandDao();
        List<StatusCommand> statusCommandList = statusCommandDao.queryBuilder()
                .where(StatusCommandDao.Properties.TimeStamp.le(staleTime))
                .build().list();
        for (StatusCommand statusCommand : statusCommandList) {
            statusCommandDao.delete(statusCommand);
        }

        //delete old ctrl get records
        CtrlGetCommandDao ctrlGetCommandDao = daoSession.getCtrlGetCommandDao();
        List<CtrlGetCommand> ctrlGetCommandList = ctrlGetCommandDao.queryBuilder()
                .where(CtrlGetCommandDao.Properties.TimeStamp.le(staleTime))
                .build().list();
        for (CtrlGetCommand ctrlGetCommand : ctrlGetCommandList) {
            ctrlGetCommandDao.delete(ctrlGetCommand);
        }

        //delete old spo2 records
        Spo2GetCommandDao spo2GetCommandDao = daoSession.getSpo2GetCommandDao();
        List<Spo2GetCommand> spo2GetCommandList = spo2GetCommandDao.queryBuilder()
                .where(Spo2GetCommandDao.Properties.TimeStamp.le(staleTime))
                .build().list();
        for (Spo2GetCommand spo2GetCommand : spo2GetCommandList) {
            spo2GetCommandDao.delete(spo2GetCommand);
        }

        //delete old weight records
        long weightStaleTime = TimeUtil.getCurrentTimeInSecond() - Constant.SCALE_SAVED_IN_DATABASE;
        WeightModelDao weightModelDao = daoSession.getWeightModelDao();
        List<WeightModel> weightModelList = weightModelDao.queryBuilder()
                .where(WeightModelDao.Properties.TimeStamp.le(weightStaleTime))
                .build().list();
        for (WeightModel oldScaleCommand : weightModelList) {
            weightModelDao.delete(oldScaleCommand);
        }
    }

    /*Chart*/
    public List<AnalogCommand> getAnalogCommand(int limit) {
        return getAnalogCommand(limit, -1);
    }

    public List<AnalogCommand> getAnalogCommand(int limit, long currentId) {
        UserModel userModel = userModelData.userModel;
        if (userModel == null) {
            userModel = getLastUserModel();
        }

        daoSession.clear();
        AnalogCommandDao analogModelDao = daoSession.getAnalogCommandDao();
        QueryBuilder<AnalogCommand> queryBuilder = analogModelDao.queryBuilder()
                .orderDesc(AnalogCommandDao.Properties.Id)
                .limit(limit);

        if (userModel != null) {
            queryBuilder.where(AnalogCommandDao.Properties.TimeStamp.ge(userModel.getStartTimeStamp()));
            if (userModel.getEndTimeStamp() > 0) {
                queryBuilder.where(AnalogCommandDao.Properties.TimeStamp.le(userModel.getEndTimeStamp()));
            }
        }

        if (currentId > 0) {
            queryBuilder.where(AnalogCommandDao.Properties.Id.le(currentId));
        }

        return queryBuilder.build().list();
    }

    public List<StatusCommand> getStatusCommand(int limit) {
        return getStatusCommand(limit, -1);
    }

    public List<StatusCommand> getStatusCommand(int limit, long currentId) {
        UserModel userModel = userModelData.userModel;
        if (userModel == null) {
            userModel = getLastUserModel();
        }

        daoSession.clear();
        StatusCommandDao statusCommandDao = daoSession.getStatusCommandDao();
        QueryBuilder<StatusCommand> queryBuilder = statusCommandDao.queryBuilder()
                .orderDesc(StatusCommandDao.Properties.Id)
                .limit(limit);

        if (userModel != null) {
            queryBuilder.where(StatusCommandDao.Properties.TimeStamp.ge(userModel.getStartTimeStamp()));
            if (userModel.getEndTimeStamp() > 0) {
                queryBuilder.where(StatusCommandDao.Properties.TimeStamp.le(userModel.getEndTimeStamp()));
            }
        }

        if (currentId > 0) {
            queryBuilder.where(StatusCommandDao.Properties.Id.le(currentId));
        }
        return queryBuilder.build().list();
    }

    public void saveWeight(int sc) {
        WeightModel weightModel = new WeightModel();
        weightModel.setSC(sc);
        weightModel.setTimeStamp(TimeUtil.getCurrentTimeInSecond());
        WeightModelDao weightModelDao = daoSession.getWeightModelDao();
        weightModelDao.insert(weightModel);
    }

    /*读取当前用户的最近12天的 称重数据*/
    public List<WeightModel> getWeightModel() {
        daoSession.clear();

        UserModel userModel = userModelData.userModel;
        if (userModel == null) {
            userModel = getLastUserModel();
        }

        WeightModelDao weightModelDao = daoSession.getWeightModelDao();
        QueryBuilder<WeightModel> queryBuilder = weightModelDao.queryBuilder()
                .orderDesc(WeightModelDao.Properties.Id);

        if (userModel != null) {
            long currentTime = TimeUtil.getCurrentTimeInSecond();
            long startTime = currentTime - 12 * 24 * 3600;
            if (startTime < userModel.getStartTimeStamp()) {
                startTime = userModel.getStartTimeStamp();
            }
            queryBuilder.where(WeightModelDao.Properties.TimeStamp.ge(startTime));
            if (userModel.getEndTimeStamp() > 0) {
                queryBuilder.where(WeightModelDao.Properties.TimeStamp.le(userModel.getEndTimeStamp()));
            }
        }
        /*后发生的在前*/
        return queryBuilder.build().list();
    }

    public List<WeightModel> getWeightModel(int limit, long currentId) {
        UserModel userModel = userModelData.userModel;
        if (userModel == null) {
            userModel = getLastUserModel();
        }

        daoSession.clear();
        WeightModelDao weightModelDao = daoSession.getWeightModelDao();
        QueryBuilder<WeightModel> queryBuilder = weightModelDao.queryBuilder()
                .orderDesc(WeightModelDao.Properties.Id)
                .limit(limit);

        if (userModel != null) {
            queryBuilder.where(WeightModelDao.Properties.TimeStamp.ge(userModel.getStartTimeStamp()));
            if (userModel.getEndTimeStamp() > 0) {
                queryBuilder.where(WeightModelDao.Properties.TimeStamp.le(userModel.getEndTimeStamp()));
            }
        }

        if (currentId > 0) {
            queryBuilder.where(WeightModelDao.Properties.Id.le(currentId));
        }

        return queryBuilder.build().list();
    }

    /*User*/

    public UserModel getLastUserModel() {
        UserModelDao userModelDao = daoSession.getUserModelDao();
        return userModelDao.queryBuilder()
                .orderDesc(UserModelDao.Properties.Id).limit(1).unique();
    }

    public void addUserModel(UserModel userModel) {
        UserModel lastUserModel = getLastUserModel();
        UserModelDao userModelDao = daoSession.getUserModelDao();
        if (lastUserModel != null) {
            lastUserModel.setEndTimeStamp(TimeUtil.getCurrentTimeInSecond());
            userModelDao.save(lastUserModel);
        }
        userModel.setStartTimeStamp(TimeUtil.getCurrentTimeInSecond());
        userModelDao.save(userModel);
    }

    public List<UserModel> getUserModel(int limit, long currentId) {
        daoSession.clear();

        UserModelDao userModelDao = daoSession.getUserModelDao();
        if (currentId > 0) {
            return userModelDao.queryBuilder()
                    .orderDesc(UserModelDao.Properties.Id)
                    .limit(limit)
                    .where(UserModelDao.Properties.Id.le(currentId))
                    .build().list();
        } else {
            return userModelDao.queryBuilder()
                    .orderDesc(UserModelDao.Properties.Id)
                    .limit(limit)
                    .build().list();
        }
    }

    public void deleteUserModel(UserModel userModel) {
        UserModelDao userModelDao = daoSession.getUserModelDao();
        userModelDao.delete(userModel);
    }
}