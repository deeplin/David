package com.david.common.dao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.david.common.dao.WeightModel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "WEIGHT_MODEL".
*/
public class WeightModelDao extends AbstractDao<WeightModel, Long> {

    public static final String TABLENAME = "WEIGHT_MODEL";

    /**
     * Properties of entity WeightModel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TimeStamp = new Property(1, long.class, "timeStamp", false, "TIME_STAMP");
        public final static Property SC = new Property(2, int.class, "SC", false, "SC");
    }


    public WeightModelDao(DaoConfig config) {
        super(config);
    }
    
    public WeightModelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"WEIGHT_MODEL\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TIME_STAMP\" INTEGER NOT NULL ," + // 1: timeStamp
                "\"SC\" INTEGER NOT NULL );"); // 2: SC
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"WEIGHT_MODEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, WeightModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTimeStamp());
        stmt.bindLong(3, entity.getSC());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, WeightModel entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTimeStamp());
        stmt.bindLong(3, entity.getSC());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public WeightModel readEntity(Cursor cursor, int offset) {
        WeightModel entity = new WeightModel( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // timeStamp
            cursor.getInt(offset + 2) // SC
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, WeightModel entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTimeStamp(cursor.getLong(offset + 1));
        entity.setSC(cursor.getInt(offset + 2));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(WeightModel entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(WeightModel entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(WeightModel entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
