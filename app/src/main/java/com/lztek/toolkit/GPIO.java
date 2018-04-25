package com.lztek.toolkit;

/**
 * GPIO操作工具类.
 */

public final class GPIO {

    private static final byte[] BYTES_IN = new byte[]{'i', 'n'};           // "in".getBytes();
    private static final byte[] BYTES_OUT = new byte[]{'o', 'u', 't'};      // "out".getBytes();
    private static final byte[] BYTES_LOW = new byte[]{'l', 'o', 'w'};      // "low".getBytes();
    private static final byte[] BYTES_HIGH = new byte[]{'h', 'i', 'g', 'h'}; // "high".getBytes();
    private GPIO() {
    }

    //private static final byte[] BYTES_1       = new byte[] { '1'};                  // "1".getBytes();
    //private static final byte[] BYTES_0       = new byte[] { '0'};                  // "0".getBytes();

    private static String directionPath(int port) {
        return "/sys/class/gpio/gpio" + port + "/direction";
    }

    private static String valuePath(int port) {
        return "/sys/class/gpio/gpio" + port + "/value";
    }

    private static void close(java.io.Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (java.io.IOException e) {
            }
        }
    }

    private static boolean write(String path, byte[] values) {
        java.io.FileOutputStream stream = null;
        try {
            stream = new java.io.FileOutputStream(path);
            stream.write(values);
            return true;
        } catch (Exception e) {
            android.util.Log.e("#ERROR#", "[GPIO] write[" + path + "] exepion:" + e.getMessage(), e);
            return false;
        } finally {
            GPIO.close(stream);
        }
    }

    /**
     * 使能GPIO
     *
     * @param port 要进行使能操作的GPIO口
     * @return {@code true} 成功, {@code false} 失败.
     */
    public static boolean enable(int port) {
        long times = 0;
        java.io.File direction = new java.io.File(GPIO.directionPath(port));
        java.io.File value = new java.io.File(GPIO.valuePath(port));
        if (value.exists() && value.canRead() && value.canWrite()) {
            return true;
        }

        SU.exec("echo " + port + " > /sys/class/gpio/export");
        times = 0;
        while (!(value.exists() && direction.exists()) && times < 2500) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            times += 50;
        }
        if (!(value.exists() && direction.exists())) {
            android.util.Log.e("#ERROR#", "!!! gpio[" + port + "] export failed !!!!");
            return false;
        }

        SU.exec("chmod 666 " + GPIO.directionPath(port));
        times = 0;
        while (!(direction.canRead() && direction.canWrite()) && times < 2500) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            times += 50;
        }
        if (!(direction.canRead() && direction.canWrite())) {
            android.util.Log.e("#ERROR#", "!!! gpio[" + port + "] chmod[direction] failed !!!!");
            return false;
        }

        SU.exec("chmod 666 " + GPIO.valuePath(port));
        times = 0;
        while (!(value.canRead() && value.canWrite()) && times < 2500) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
            }
            times += 50;
        }
        if (!(value.canRead() && value.canWrite())) {
            android.util.Log.e("#ERROR#", "!!! gpio[" + port + "] chmod[value] failed !!!!");
            return false;
        }
        return true;
    }


    /**
     * 设置GPIO为输入模式
     *
     * @param port 要设置为输入模式的GPIO口
     * @return {@code true} 成功, {@code false} 失败.
     */
    public static boolean setInputMode(int port) {
        return GPIO.write(GPIO.directionPath(port), BYTES_IN);
    }

    /**
     * 设置GPIO为输出模式
     *
     * @param port 要设置为输出模式的GPIO口
     * @return {@code true} 成功, {@code false} 失败.
     */
    public static boolean setOutputMode(int port) {
        return GPIO.write(GPIO.directionPath(port), BYTES_OUT);
    }

    /**
     * 读取GPIO的值，需注意输入、输出模式
     *
     * @param port 要读取的GPIO口
     * @return 正常值0或1，其它值为读取失败.
     */
    public static int value(int port) {
        java.io.FileInputStream stream = null;
        try {
            stream = new java.io.FileInputStream(GPIO.valuePath(port));
            return '0' == stream.read() ? 0 : 1;
            //int value = stream.read();
            //return '0' == value? 0 : ('1' == value? 1 : -1);
        } catch (Exception e) {
            android.util.Log.e("#ERROR#", "[GPIO]read value failed: " + e.getMessage(), e);
            return -1;
        } finally {
            GPIO.close(stream);
        }
    }

    /**
     * 先将GPIO设置为输入模式，再读取GPIO的输入值
     *
     * @param port 要读取的GPIO口
     * @return 正常值0或1，其它值为读取失败.
     */
    public static int readValue(int port) {
        return GPIO.setInputMode(port) ? GPIO.value(port) : -1;
    }

    /**
     * 设置GPIO的输出值，此操作将GPIO设置为输出模式，并将输出相应的值[0｜1]
     *
     * @param port  要输出操作的GPIO口
     * @param value 要输出的值，只能是0或1
     * @return {@code true} 成功, {@code false} 失败.
     * @throws IllegalArgumentException {@code value}不是0或1.
     */
    public static boolean writeValue(int port, int value) {
        if (0 != value && 1 != value) {
            throw new IllegalArgumentException("[GPIO] invalid vaule");
        }
        return GPIO.write(GPIO.directionPath(port), 0 == value ? BYTES_LOW : BYTES_HIGH);
        //return GPIO.setOutputMode(port) && GPIO.write(GPIO.valuePath(port), 0 == value? BYTES_0 : BYTES_1);
    }
}
