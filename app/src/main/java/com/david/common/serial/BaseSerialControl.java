package com.david.common.serial;

import com.david.common.utils.LogUtil;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * author: Ling Lin
 * created on: 2017/7/17 21:48
 * email: 10525677@qq.com
 * description:
 */

public abstract class BaseSerialControl {

    private volatile boolean running;

    private byte[] inputBuffer;

    protected abstract OutputStream getOutputStream();

    protected abstract InputStream getInputStream();

    public BaseSerialControl() {
        this.running = false;
    }

    protected void open(int bufferSize) {
        this.inputBuffer = new byte[bufferSize];
        this.running = true;
    }

    protected void close() {
        this.running = false;
    }

    protected synchronized void sendReceive(BaseSerialMessage serialMessage) throws Exception {
        if (this.running) {
            send(serialMessage);
            receive(serialMessage);
        }
    }

    protected void send(BaseSerialMessage serialMessage) throws Exception {
        OutputStream outputStream = getOutputStream();
        outputStream.write(serialMessage.getRequest());
        LogUtil.i(this, "Serial send: " + new String(serialMessage.getRequest()));
    }

    private void receive(BaseSerialMessage serialMessage) throws Exception {
        InputStream inputStream = getInputStream();
        int index = 0;
        Thread.sleep(10);
        for (int inputCount = 8; inputCount > 0; inputCount--) {
            int inputLength = inputStream.read(this.inputBuffer, index, this.inputBuffer.length - index);
            if (inputLength > 0) {
//                LogUtil.i(this, inputCount + ": " + inputLength + " index: " + index);
                index += inputLength;
                if (index >= this.inputBuffer.length) {
                    throw new Exception("Serial buffer overflows.");
                }
            } else {
                break;
            }
            Thread.sleep(10);
        }
        if (index > 0) {
            byte[] tempBuffer = new byte[index];
            System.arraycopy(this.inputBuffer, 0, tempBuffer, 0, index);
            serialMessage.setResponse(tempBuffer);
            LogUtil.i(this, "Serial receive: " + index + " " + new String(tempBuffer));
        } else {
            throw new Exception("No response.");
        }
    }
}