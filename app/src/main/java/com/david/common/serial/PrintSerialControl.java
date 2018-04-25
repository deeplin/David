package com.david.common.serial;

import com.apkfuns.logutils.LogUtils;
import com.david.common.utils.Constant;
import com.lztek.toolkit.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * author: Ling Lin
 * created on: 2018/1/19 9:30
 * email: 10525677@qq.com
 * description:
 */

public class PrintSerialControl extends BaseSerialControl {
    private SerialPort serialPort = null;

    @Inject
    public PrintSerialControl() {
        super();
    }

    public synchronized void start() throws Exception {
        if (serialPort == null) {
            int speed = 9600;
            int dataBit = 8;
            int parity = 0;
            int stopBits = 1;
            int dataFlow = 0;
            serialPort = SerialPort.open(Constant.COM4, speed, dataBit, parity, stopBits, dataFlow);
            if (serialPort == null) {
                throw new Exception("Error occur in opening serial port.");
            }
            super.open(0);
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
    }

    @Override
    protected OutputStream getOutputStream() {
        return serialPort.getOutputStream();
    }

    @Override
    protected InputStream getInputStream() {
        return serialPort.getInputStream();
    }

    public void sendAsync(BaseSerialMessage serialMessage) {
        Observable.just(serialMessage)
                .observeOn(Schedulers.io())
                .subscribe((message) -> super.send(message)
                        , (error) -> LogUtils.e(error));
    }
}
