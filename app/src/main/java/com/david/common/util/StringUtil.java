package com.david.common.util;

/**
 * author: Ling Lin
 * created on: 2017/8/2 20:49
 * email: 10525677@qq.com
 * description:
 */

public class StringUtil {

    public static String byteToHex(byte data){
        StringBuilder stringBuilder = new StringBuilder();
        String hv = Integer.toHexString(data);
        if (hv.length() < 2) {
            stringBuilder.append(0);
        }
        return stringBuilder.append(hv).toString().toUpperCase();
    }

    /*0x80: -127
    * 0xFF: -1
    * 0x7F: 127
    * */
    public static int hexToInteger(String hexString) {
        int result = Integer.parseInt(hexString, 16);
        if (result > 127) {
            result -= 256;
        }
        return result;
    }
}