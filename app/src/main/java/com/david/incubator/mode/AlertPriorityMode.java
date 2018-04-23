package com.david.incubator.mode;

/**
 * author: Ling Lin
 * created on: 2017/8/16 14:46
 * email: 10525677@qq.com
 * description:
 */

public enum AlertPriorityMode {
    Sys_Con(2),
    Sys_New_Alert(3),
    Sys_Old_Alert(4),
    Sys_Response_Err(5),
    Sys_None(10);

    private int index;

    AlertPriorityMode(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }
}