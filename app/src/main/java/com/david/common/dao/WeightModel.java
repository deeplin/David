package com.david.common.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * author: Ling Lin
 * created on: 2017/7/20 11:36
 * email: 10525677@qq.com
 * description:
 */

@Entity
public class WeightModel {

    @Id(autoincrement = true)
    private Long id;
    private long timeStamp;
    private int SC;

    @Generated(hash = 1814652962)
    public WeightModel(Long id, long timeStamp, int SC) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.SC = SC;
    }

    @Generated(hash = 448621343)
    public WeightModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getSC() {
        return SC;
    }

    public void setSC(int SC) {
        this.SC = SC;
    }
}
