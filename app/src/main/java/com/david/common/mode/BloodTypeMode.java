package com.david.common.mode;

/**
 * author: Ling Lin
 * created on: 2017/12/26 15:28
 * email: 10525677@qq.com
 * description:
 */

public enum BloodTypeMode {
    A("A", 1), B("B", 2), AB("AB", 3), O("O", 4), Other("Other", 5);

    private String name;
    private int index;

    BloodTypeMode(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
