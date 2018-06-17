package com.david.common.serial.command.spo2;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;
import com.david.common.util.Pair;
import com.david.common.util.ReflectionUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * filename: com.eternal.davidconsole.serial.command.Spo2WaveCommand.java
 * author: Ling Lin
 * created on: 2017/8/2 20:46
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class Spo2WaveCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~SPO2 WAVE" + CommandChar.ENTER).getBytes();
    private List<Pair<String, String>> points;

    @Inject
    public Spo2WaveCommand() {
        points = new ArrayList<>();
    }

    public static void parseCommandString(String commandString, BaseSerialMessage message) throws Exception {
        String[] items = commandString.split(CommandChar.SEMICOLON);
        if (items.length <= 0) {
            throw new Exception("Malformed serial response: " + commandString);
        }

        /*解析Spo2字符串 去掉第一个位置*/
        for (int index = 1; index < items.length; index++) {
            String[] pair = items[index].split(CommandChar.COLON);

            if (pair.length == 2) {
                ReflectionUtil.addMethod(message, "Point", pair[0], pair[1]);
            } else if (pair.length == 1) {
                ReflectionUtil.addMethod(message, "Point", pair[0], "");
            } else if (pair.length == 0) {
                ReflectionUtil.addMethod(message, "Point", "", "");
            }
        }
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }

    public synchronized void addPoint(String ppg, String siq) {
        Pair<String, String> pair = new Pair<>(ppg, siq);
        points.add(pair);
    }

    public synchronized void copyToList(List<Pair<String, String>> bufferList) {
        bufferList.addAll(points);
        points.clear();
    }
}