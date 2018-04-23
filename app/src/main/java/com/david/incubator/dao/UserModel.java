package com.david.incubator.dao;

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
public class UserModel {

    @Id(autoincrement = true)
    private Long id;

    private String name;
    private String userId;
    private boolean sex;

    private long startTimeStamp;
    private long endTimeStamp;

    private String bloodGroup;
    private String birthday;
    private int weight;
    private int gestationalAge;
    private String history;

    @Generated(hash = 1198489226)
    public UserModel(Long id, String name, String userId, boolean sex,
                     long startTimeStamp, long endTimeStamp, String bloodGroup,
                     String birthday, int weight, int gestationalAge, String history) {
        this.id = id;
        this.name = name;
        this.userId = userId;
        this.sex = sex;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
        this.bloodGroup = bloodGroup;
        this.birthday = birthday;
        this.weight = weight;
        this.gestationalAge = gestationalAge;
        this.history = history;
    }

    @Generated(hash = 782181818)
    public UserModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean getSex() {
        return this.sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public long getStartTimeStamp() {
        return this.startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimeStamp() {
        return this.endTimeStamp;
    }

    public void setEndTimeStamp(long endTimeStamp) {
        this.endTimeStamp = endTimeStamp;
    }

    public String getBloodGroup() {
        return this.bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getBirthday() {
        return this.birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getGestationalAge() {
        return this.gestationalAge;
    }

    public void setGestationalAge(int gestationalAge) {
        this.gestationalAge = gestationalAge;
    }

    public String getHistory() {
        return this.history;
    }

    public void setHistory(String history) {
        this.history = history;
    }
}
