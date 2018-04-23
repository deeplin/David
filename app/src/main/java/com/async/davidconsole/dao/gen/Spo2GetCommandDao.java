package com.async.davidconsole.dao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.david.incubator.dao.Spo2GetCommand;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SPO2_GET_COMMAND".
*/
public class Spo2GetCommandDao extends AbstractDao<Spo2GetCommand, Long> {

    public static final String TABLENAME = "SPO2_GET_COMMAND";

    /**
     * Properties of entity Spo2GetCommand.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TimeStamp = new Property(1, long.class, "timeStamp", false, "TIME_STAMP");
        public final static Property Status = new Property(2, String.class, "status", false, "STATUS");
        public final static Property Avg = new Property(3, int.class, "avg", false, "AVG");
        public final static Property Sens = new Property(4, String.class, "sens", false, "SENS");
        public final static Property Fastsat = new Property(5, String.class, "fastsat", false, "FASTSAT");
    }


    public Spo2GetCommandDao(DaoConfig config) {
        super(config);
    }
    
    public Spo2GetCommandDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SPO2_GET_COMMAND\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TIME_STAMP\" INTEGER NOT NULL ," + // 1: timeStamp
                "\"STATUS\" TEXT," + // 2: status
                "\"AVG\" INTEGER NOT NULL ," + // 3: avg
                "\"SENS\" TEXT," + // 4: sens
                "\"FASTSAT\" TEXT);"); // 5: fastsat
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SPO2_GET_COMMAND\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Spo2GetCommand entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTimeStamp());
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(3, status);
        }
        stmt.bindLong(4, entity.getAvg());
 
        String sens = entity.getSens();
        if (sens != null) {
            stmt.bindString(5, sens);
        }
 
        String fastsat = entity.getFastsat();
        if (fastsat != null) {
            stmt.bindString(6, fastsat);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Spo2GetCommand entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTimeStamp());
 
        String status = entity.getStatus();
        if (status != null) {
            stmt.bindString(3, status);
        }
        stmt.bindLong(4, entity.getAvg());
 
        String sens = entity.getSens();
        if (sens != null) {
            stmt.bindString(5, sens);
        }
 
        String fastsat = entity.getFastsat();
        if (fastsat != null) {
            stmt.bindString(6, fastsat);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Spo2GetCommand readEntity(Cursor cursor, int offset) {
        Spo2GetCommand entity = new Spo2GetCommand( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // timeStamp
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // status
            cursor.getInt(offset + 3), // avg
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // sens
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5) // fastsat
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Spo2GetCommand entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTimeStamp(cursor.getLong(offset + 1));
        entity.setStatus(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAvg(cursor.getInt(offset + 3));
        entity.setSens(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setFastsat(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Spo2GetCommand entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Spo2GetCommand entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Spo2GetCommand entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
