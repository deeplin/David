package com.david.common.serial;

import com.david.common.mode.MessageMode;

import io.reactivex.functions.BiConsumer;

/**
 * author: Ling Lin
 * created on: 2017/7/17 21:49
 * email: 10525677@qq.com
 * description:
 */

public abstract class BaseSerialMessage {

    public static final int CRITICAL_COMMAND = Integer.MAX_VALUE;

    private MessageMode messageMode;
    private byte[] response;
    private BiConsumer<Boolean, BaseSerialMessage> onCompleted;
    private int repeatTime;

    public BaseSerialMessage() {
        messageMode = MessageMode.Start;
        response = null;
        onCompleted = null;
        repeatTime = 3;
    }

    public MessageMode getMessageMode() {
        return messageMode;
    }

    public void setMessageMode(MessageMode messageMode) {
        this.messageMode = messageMode;
    }

    public abstract byte[] getRequest();

    public byte[] getResponse() {
        return response;
    }

    public void setResponse(byte[] response) {
        this.response = response;
    }

    public void setOnCompleted(BiConsumer<Boolean, BaseSerialMessage> onCompleted) {
        this.onCompleted = onCompleted;
    }

    public BiConsumer<Boolean, BaseSerialMessage> getOnCompleted() {
        return onCompleted;
    }

    public synchronized void decreaseRepeatTime() {
        repeatTime--;
    }

    public synchronized int getRepeatTime() {
        return repeatTime;
    }

    public void setCritical() {
        this.repeatTime = CRITICAL_COMMAND;
    }
}