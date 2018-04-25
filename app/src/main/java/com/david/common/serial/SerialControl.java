package com.david.common.serial;

import com.apkfuns.logutils.LogUtils;
import com.david.common.mode.MessageMode;
import com.david.common.util.Constant;
import com.lztek.toolkit.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2017/7/18 15:16
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class SerialControl extends BaseSerialControl {

    private SerialPort serialPort = null;
    private Consumer<BaseSerialMessage> messageParser;

    private Map<Class, BaseSerialMessage> sessionMap;
    private Map<String, BaseSerialMessage> repeatSessionMap;

    @Inject
    public SerialControl() {
        super();
        sessionMap = new HashMap<>();
        repeatSessionMap = new HashMap<>();
    }

    public synchronized void start(int bufferSize, Consumer<BaseSerialMessage> messageParser) throws Exception {
        if (serialPort == null) {
            this.messageParser = messageParser;
            int speed = 115200;
            SerialPort.loadLibrary();
            serialPort = SerialPort.open(Constant.COM3, speed);
            if (serialPort == null) {
                throw new Exception("Error occur in opening serial port.");
            }
            super.open(bufferSize);
        }
    }

    public synchronized void stop() {
        super.close();
        if (serialPort != null) {
            try {
                Thread.sleep(60);
                SerialPort.close(serialPort);
                serialPort = null;
            } catch (Exception e) {
                LogUtils.e(e);
            }
        }
        repeatSessionMap.clear();
        sessionMap.clear();
    }

    @Override
    protected OutputStream getOutputStream() {
        return serialPort.getOutputStream();
    }

    @Override
    protected InputStream getInputStream() {
        return serialPort.getInputStream();
    }

    public synchronized void addSession(BaseSerialMessage serialMessage) {
        sessionMap.put(serialMessage.getClass(), serialMessage);
        MessageMode messageMode = serialMessage.getMessageMode();
        if (messageMode.equals(MessageMode.Start)) {
            serialMessage.setMessageMode(MessageMode.InProcess);
            sendAsync(serialMessage);
        }
    }

    public synchronized void addRepeatSession(BaseSerialMessage serialMessage) {
        addRepeatSession(serialMessage.getClass().getSimpleName(), serialMessage);
    }

    private void addRepeatSession(String key, BaseSerialMessage serialMessage) {
        repeatSessionMap.put(key, serialMessage);
        MessageMode messageMode = serialMessage.getMessageMode();
        if (messageMode.equals(MessageMode.Start)) {
            serialMessage.setMessageMode(MessageMode.InProcess);
            sendAsync(serialMessage);
        }
    }

    public synchronized void refresh() {
        startSession();
        startRepeatSession();
    }

    private void startSession() {
        Iterator<Map.Entry<Class, BaseSerialMessage>> iterator = sessionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Class, BaseSerialMessage> entry = iterator.next();
            BaseSerialMessage serialMessage = entry.getValue();
            MessageMode messageMode = serialMessage.getMessageMode();

            if (messageMode.equals(MessageMode.Error)) {
                if (serialMessage.getRepeatTime() == BaseSerialMessage.CRITICAL_COMMAND) {
                    serialMessage.setMessageMode(MessageMode.InProcess);
                    sendAsync(serialMessage);
                } else {
                    serialMessage.setOnCompleted(null);
                    iterator.remove();
                }
            } else if (messageMode.equals(MessageMode.Completed)) {
                serialMessage.setOnCompleted(null);
                iterator.remove();
            }
        }
    }

    private void startRepeatSession() {
        for (BaseSerialMessage serialMessage : repeatSessionMap.values()) {
            serialMessage.setMessageMode(MessageMode.InProcess);
            sendAsync(serialMessage);
        }
    }

    public void sendAsync(BaseSerialMessage serialMessage) {
        Observable.just(serialMessage)
                .observeOn(Schedulers.io())
                .subscribe((message) -> super.sendReceive(message),
                        (error) -> {
                            serialMessage.setResponse(null);
                            serialMessage.setMessageMode(MessageMode.Error);
                            messageParser.accept(serialMessage);
                        }, () -> {
                            serialMessage.setMessageMode(MessageMode.Completed);
                            messageParser.accept(serialMessage);
                        });
    }
}
