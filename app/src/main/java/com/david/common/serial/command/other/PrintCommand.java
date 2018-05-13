package com.david.common.serial.command.other;

import com.david.common.serial.BaseSerialMessage;

/**
 * author: Ling Lin
 * created on: 2018/1/19 10:09
 * email: 10525677@qq.com
 * description:
 */
public class PrintCommand extends BaseSerialMessage {

    private String buffer;

    public PrintCommand(String buffer) {
        this.buffer = buffer;
    }

    @Override
    public byte[] getRequest() {
        byte[] result;
        try {
            result = buffer.getBytes("GBK");
        } catch (Exception e) {
            result = new byte[0];
        }
        return result;
    }
}