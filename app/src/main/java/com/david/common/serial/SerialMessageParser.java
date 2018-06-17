package com.david.common.serial;

import com.apkfuns.logutils.LogUtils;
import com.david.common.dao.CtrlGetCommand;
import com.david.common.mode.MessageMode;
import com.david.common.serial.command.spo2.Spo2WaveCommand;
import com.david.common.util.CommandChar;
import com.david.common.util.Constant;
import com.david.common.util.ReflectionUtil;
import com.david.common.util.ResourceUtil;
import com.david.common.util.StringUtil;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;

/**
 * author: Ling Lin
 * created on: 2017/7/18 17:06
 * email: 10525677@qq.com
 * description:
 */

@Singleton
public class SerialMessageParser implements Consumer<BaseSerialMessage> {

    private static final int SERIAL_BUFFER_SIZE = 10240;
    private final byte[] filteredInputBuffer;

    @Inject
    public SerialMessageParser() {
        filteredInputBuffer = new byte[SERIAL_BUFFER_SIZE];
    }

    @Override
    public void accept(@NonNull BaseSerialMessage serialMessage) throws Exception {
        MessageMode messageMode = serialMessage.getMessageMode();

        BiConsumer<Boolean, BaseSerialMessage> onCompleted = serialMessage.getOnCompleted();

        try {
            if (messageMode.equals(MessageMode.Error)) {
                throw new Exception("No response.");
            }
            /*filter无用字符*/
            byte[] inputBuffer = serialMessage.getResponse();
            byte[] filteredInputBuffer = filterBuffer(inputBuffer);

            /*检查返回指令状态，返回指令数据段*/
            String commandString = checkCommandStatus(filteredInputBuffer);

            if (commandString != null) {
                if (serialMessage.getClass() == Spo2WaveCommand.class) {
                    Spo2WaveCommand.parseCommandString(commandString, serialMessage);
                } else {
                    parseCommandString(commandString, serialMessage);
                }
            }

            if (onCompleted != null) {
                onCompleted.accept(true, serialMessage);
            }
        } catch (Exception e) {

            String response = serialMessage.getResponse() == null ? "" : new String(serialMessage.getResponse());
            LogUtils.e(String.format(Locale.US, "Serial connection: %s %s %s", e.getMessage(),
                    new String(serialMessage.getRequest()), response));
            LogUtils.e(e);

            if (serialMessage.getRepeatTime() != BaseSerialMessage.CRITICAL_COMMAND) {
                serialMessage.decreaseRepeatTime();
                if (serialMessage.getRepeatTime() <= 0) {
                    serialMessage.setMessageMode(MessageMode.Fail);
                    //todo
//                alarmControl.setAlert(AlarmPriorityMode.Sys_Con, "SYS.CON");
                    if (onCompleted != null) {
                        onCompleted.accept(false, serialMessage);
                    }
                } else {
                    serialMessage.setMessageMode(MessageMode.Resend);
                }
            } else {
                serialMessage.setMessageMode(MessageMode.Resend);
            }
        }
    }

    /*
     * filter无用字符，双回车表示字符串结束
     * */
    private synchronized byte[] filterBuffer(byte[] inputBuffer) throws Exception {
        int filterIndex = 0;
        boolean completePacket = false;
        boolean enter = false;

        for (int serialIndex = 0; (serialIndex < inputBuffer.length) && (!completePacket); serialIndex++) {
            byte data = inputBuffer[serialIndex];
            // [0 - 9] || [A - Z]
            if ((data >= 0x30 && data <= 0x39) || (data >= 0x41 && data <= 0x5A)) {
                filteredInputBuffer[filterIndex++] = data;
                enter = false;
                // [a - z]
            } else if (data >= 0x61 && data <= 0x7A) {
                filteredInputBuffer[filterIndex++] = (data);
                enter = false;
            } else {
                switch (data) {
                    case (0x20):
                    case (0x2C): //","
                    case (0x2D): //"-"
                    case (0x2E): //"."
                    case (0x3D): //"="
                    case (0x3A): //":"
                    case (0x3B): //";"
                    case (0x5B): //"["
                    case (0x5D): //"]"
                    case (0x5F): //"_"
                    case (0x28): //"_"
                    case (0x29): //"_"
                        filteredInputBuffer[filterIndex++] = data;
                        enter = false;
                        break;
                    case (0x0A):
                        if (enter) {
                            completePacket = true;
                        } else {
                            enter = true;
                        }
                        break;
                    case (0x0D):/*无用字符*/
                        break;
                    default: {
                        throw new Exception(String.format(Locale.US, "Receive illegal char. %s", StringUtil.byteToHex(data)));
                    }
                }
            }
        }
        if (completePacket) {
            byte[] filteredInput = new byte[filterIndex];
            System.arraycopy(filteredInputBuffer, 0, filteredInput, 0, filterIndex);
            return filteredInput;
        }
        throw new Exception("No ending char.");
    }

    /*检查返回指令状态，返回指令数据段*/
    private String checkCommandStatus(byte[] filteredInputBuffer) throws Exception {
        int statusIndex = getStatusIndex(filteredInputBuffer);
        String status = new String(filteredInputBuffer, 0, statusIndex);

        if (status.equals(CommandChar.OK)) {
            if (filteredInputBuffer.length - statusIndex - 1 > 0) {
                return new String(filteredInputBuffer, statusIndex + 1, filteredInputBuffer.length - statusIndex - 2);
            } else {
                return null;
            }
        } else {
            throw new Exception("Error in checking status.");
        }
    }

    private int getStatusIndex(byte[] bytes) throws Exception {
        for (int index = 0; index < bytes.length; index++) {
            //3B ;
            if (bytes[index] == 0x3B) {
                return index;
            }
        }
        throw new Exception("Get status index error.");
    }

    private void parseCommandString(String commandString, BaseSerialMessage command) throws Exception {
        String[] items = commandString.split(CommandChar.SEMICOLON);
        if (items.length <= 0) {
            throw new Exception("Too short length.");
        }

        for (String item : items) {
            String[] pair = item.split(CommandChar.EQUAL);
            if (pair.length == 2) {
                String method = pair[0];
                if (command.getClass() == CtrlGetCommand.class) {
                    //Replace . with _
                    method = method.replace(".", "_");
                }

                int value;
                String pair1 = pair[1];
                if (pair1.equals(Constant.SENSOR_NA_STRING)) {
                    value = Constant.SENSOR_NA_VALUE;
                    ReflectionUtil.setMethod(command, method, value);
                } else {
                    if (ResourceUtil.tryParseInt(pair1)) {
                        value = Integer.parseInt(pair[1]);
                        ReflectionUtil.setMethod(command, method, value);
                    } else {
                        ReflectionUtil.setMethod(command, method, pair1);
                    }
                }
            } else {
                throw new Exception("Parse command error.");
            }
        }
    }
}