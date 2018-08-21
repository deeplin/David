package com.david.common.serial;

import com.david.common.util.LogUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Locale;

/**
 * author: Ling Lin
 * created on: 2017/7/17 21:48
 * email: 10525677@qq.com
 * description:
 */

public abstract class BaseSerialControl {

    private boolean running;

    private byte[] inputBuffer;

    protected abstract OutputStream getOutputStream();

    protected abstract InputStream getInputStream();

    public BaseSerialControl() {
        this.running = false;
    }

    protected synchronized void open(int bufferSize) {
        this.inputBuffer = new byte[bufferSize];
        this.running = true;
    }

    protected synchronized void close() {
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
        LogUtil.i(this, String.format(Locale.US, "Serial send: %s", new String(serialMessage.getRequest())));
    }

    protected void receive(BaseSerialMessage serialMessage) throws Exception {
        InputStream inputStream = getInputStream();
        int index = 0;
        Thread.sleep(20);
        for (int inputCount = 8; inputCount > 0; inputCount--) {
            int inputLength = inputStream.read(this.inputBuffer, index, this.inputBuffer.length - index);
            if (inputLength > 0) {
                LogUtil.i(this, String.format(Locale.US, "%d: %d: %d", inputCount, inputLength, index));
                index += inputLength;
            } else {
                break;
            }
            Thread.sleep(10);
        }
        if (index > 0) {
            byte[] tempBuffer = new byte[index];
            System.arraycopy(this.inputBuffer, 0, tempBuffer, 0, index);
            serialMessage.setResponse(tempBuffer);
            LogUtil.i(this, String.format(Locale.US, "Serial receive: %d %s", index, new String(tempBuffer)));
        } else {
            throw new Exception("No response.");
        }
    }

    protected void receive(byte[] buffer) throws Exception {
        InputStream inputStream = getInputStream();
        inputStream.read(buffer, 0, buffer.length);
    }
}