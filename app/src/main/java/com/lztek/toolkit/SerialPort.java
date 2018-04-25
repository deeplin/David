package com.lztek.toolkit;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口读写操作类.
 */
public final class SerialPort {
    public static void loadLibrary() throws Exception {
        try {
            System.loadLibrary("serialport");
        } catch (Throwable t) {
            throw new Exception("load library libserialport.so failed");
        }
    }

    private FileDescriptor mFileDescriptor;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private SerialPort() {
    }

    private native static FileDescriptor _open(String path, int baudrate,
                                               int dataBit, int parity, int stopBits, int dataFlow);

    private native static int _close(FileDescriptor fd);


    /**
     * 打开串口
     *
     * @param path     要打开的串口路径
     * @param baudrate 波特率
     * @param dataBit  数据位
     * @param parity   奇偶校验
     * @param stopBits 停止位
     * @param dataFlow 流控
     * @return 打开成功返回一个SerialPort对象，出错返回null
     */
    public static SerialPort open(String path, int baudrate,
                                  int dataBit, int parity, int stopBits, int dataFlow) {
        FileDescriptor fd = SerialPort._open(path, baudrate, dataBit, parity, stopBits, dataFlow);
        if (null != fd) {
            SerialPort object = new SerialPort();
            object.mFileDescriptor = fd;
            object.mFileInputStream = new FileInputStream(fd);
            object.mFileOutputStream = new FileOutputStream(fd);


            return object;
        } else {
            return null;
        }
    }

    /**
     * 使用默认参数打开串口
     *
     * @param path     要打开的串口路径
     * @param baudrate 波特率
     * @return 打开成功返回一个SerialPort对象，出错返回null
     */
    public static SerialPort open(String path, int baudrate) {
        return SerialPort.open(path, baudrate, 8, 0, 1, 0);
    }

    /**
     * 关闭串口
     *
     * @param path 要关闭的串口 SerialPort对象
     * @return {@code true} 成功, {@code false} 失败.
     */
    public static boolean close(SerialPort object) {
        return null != object ? object.close() : false;
    }

    /**
     * 关闭串口
     *
     * @param
     * @return {@code true} 成功, {@code false} 失败.
     */
    public boolean close() {
        if (null != mFileDescriptor) {
            return SerialPort._close(mFileDescriptor) > 0;
        } else {
            return false;
        }
    }

    /**
     * 获取串口输入流接口
     *
     * @param
     * @return 成功返回一个InputStream接口，出错返回null
     */
    public InputStream getInputStream() {
        return mFileInputStream;
    }

    /**
     * 获取串口输出流接口
     *
     * @param
     * @return 成功返回一个OutputStream接口，出错返回null
     */
    public OutputStream getOutputStream() {
        return mFileOutputStream;
    }
}
