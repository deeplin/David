package com.david.common.dao.gen;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.david.common.dao.CtrlGetCommand;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CTRL_GET_COMMAND".
*/
public class CtrlGetCommandDao extends AbstractDao<CtrlGetCommand, Long> {

    public static final String TABLENAME = "CTRL_GET_COMMAND";

    /**
     * Properties of entity CtrlGetCommand.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TimeStamp = new Property(1, long.class, "timeStamp", false, "TIME_STAMP");
        public final static Property Ctrl = new Property(2, String.class, "ctrl", false, "CTRL");
        public final static Property C_air = new Property(3, int.class, "c_air", false, "C_AIR");
        public final static Property C_hum = new Property(4, int.class, "c_hum", false, "C_HUM");
        public final static Property C_o2 = new Property(5, int.class, "c_o2", false, "C_O2");
        public final static Property C_skin = new Property(6, int.class, "c_skin", false, "C_SKIN");
        public final static Property W_skin = new Property(7, int.class, "w_skin", false, "W_SKIN");
        public final static Property W_man = new Property(8, int.class, "w_man", false, "W_MAN");
        public final static Property W_inc = new Property(9, int.class, "w_inc", false, "W_INC");
        public final static Property S_set = new Property(10, int.class, "s_set", false, "S_SET");
        public final static Property A_set = new Property(11, int.class, "a_set", false, "A_SET");
        public final static Property W_set = new Property(12, int.class, "w_set", false, "W_SET");
        public final static Property W_mat = new Property(13, int.class, "w_mat", false, "W_MAT");
    }


    public CtrlGetCommandDao(DaoConfig config) {
        super(config);
    }
    
    public CtrlGetCommandDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CTRL_GET_COMMAND\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"TIME_STAMP\" INTEGER NOT NULL ," + // 1: timeStamp
                "\"CTRL\" TEXT," + // 2: ctrl
                "\"C_AIR\" INTEGER NOT NULL ," + // 3: c_air
                "\"C_HUM\" INTEGER NOT NULL ," + // 4: c_hum
                "\"C_O2\" INTEGER NOT NULL ," + // 5: c_o2
                "\"C_SKIN\" INTEGER NOT NULL ," + // 6: c_skin
                "\"W_SKIN\" INTEGER NOT NULL ," + // 7: w_skin
                "\"W_MAN\" INTEGER NOT NULL ," + // 8: w_man
                "\"W_INC\" INTEGER NOT NULL ," + // 9: w_inc
                "\"S_SET\" INTEGER NOT NULL ," + // 10: s_set
                "\"A_SET\" INTEGER NOT NULL ," + // 11: a_set
                "\"W_SET\" INTEGER NOT NULL ," + // 12: w_set
                "\"W_MAT\" INTEGER NOT NULL );"); // 13: w_mat
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CTRL_GET_COMMAND\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CtrlGetCommand entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTimeStamp());
 
        String ctrl = entity.getCtrl();
        if (ctrl != null) {
            stmt.bindString(3, ctrl);
        }
        stmt.bindLong(4, entity.getC_air());
        stmt.bindLong(5, entity.getC_hum());
        stmt.bindLong(6, entity.getC_o2());
        stmt.bindLong(7, entity.getC_skin());
        stmt.bindLong(8, entity.getW_skin());
        stmt.bindLong(9, entity.getW_man());
        stmt.bindLong(10, entity.getW_inc());
        stmt.bindLong(11, entity.getS_set());
        stmt.bindLong(12, entity.getA_set());
        stmt.bindLong(13, entity.getW_set());
        stmt.bindLong(14, entity.getW_mat());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CtrlGetCommand entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getTimeStamp());
 
        String ctrl = entity.getCtrl();
        if (ctrl != null) {
            stmt.bindString(3, ctrl);
        }
        stmt.bindLong(4, entity.getC_air());
        stmt.bindLong(5, entity.getC_hum());
        stmt.bindLong(6, entity.getC_o2());
        stmt.bindLong(7, entity.getC_skin());
        stmt.bindLong(8, entity.getW_skin());
        stmt.bindLong(9, entity.getW_man());
        stmt.bindLong(10, entity.getW_inc());
        stmt.bindLong(11, entity.getS_set());
        stmt.bindLong(12, entity.getA_set());
        stmt.bindLong(13, entity.getW_set());
        stmt.bindLong(14, entity.getW_mat());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CtrlGetCommand readEntity(Cursor cursor, int offset) {
        CtrlGetCommand entity = new CtrlGetCommand( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // timeStamp
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ctrl
            cursor.getInt(offset + 3), // c_air
            cursor.getInt(offset + 4), // c_hum
            cursor.getInt(offset + 5), // c_o2
            cursor.getInt(offset + 6), // c_skin
            cursor.getInt(offset + 7), // w_skin
            cursor.getInt(offset + 8), // w_man
            cursor.getInt(offset + 9), // w_inc
            cursor.getInt(offset + 10), // s_set
            cursor.getInt(offset + 11), // a_set
            cursor.getInt(offset + 12), // w_set
            cursor.getInt(offset + 13) // w_mat
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CtrlGetCommand entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTimeStamp(cursor.getLong(offset + 1));
        entity.setCtrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setC_air(cursor.getInt(offset + 3));
        entity.setC_hum(cursor.getInt(offset + 4));
        entity.setC_o2(cursor.getInt(offset + 5));
        entity.setC_skin(cursor.getInt(offset + 6));
        entity.setW_skin(cursor.getInt(offset + 7));
        entity.setW_man(cursor.getInt(offset + 8));
        entity.setW_inc(cursor.getInt(offset + 9));
        entity.setS_set(cursor.getInt(offset + 10));
        entity.setA_set(cursor.getInt(offset + 11));
        entity.setW_set(cursor.getInt(offset + 12));
        entity.setW_mat(cursor.getInt(offset + 13));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CtrlGetCommand entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CtrlGetCommand entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CtrlGetCommand entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
