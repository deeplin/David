package com.david.common.serial.command.alert;

import com.david.common.serial.BaseSerialMessage;
import com.david.common.util.CommandChar;

public class AlertListCommand extends BaseSerialMessage {

    public static final byte[] COMMAND = ("~ALERT" + CommandChar.ENTER).getBytes();

    public String getAlert0() {
        return alert0;
    }

    public void setAlert0(String alert0) {
        this.alert0 = alert0;
        setAlertCount(1);
    }

    public String getAlert1() {
        return alert1;
    }

    public void setAlert1(String alert1) {
        this.alert1 = alert1;
        setAlertCount(2);
    }

    public String getAlert2() {
        return alert2;
    }

    public void setAlert2(String alert2) {
        this.alert2 = alert2;
        setAlertCount(3);
    }

    public String getAlert3() {
        return alert3;
    }

    public void setAlert3(String alert3) {
        this.alert3 = alert3;
        setAlertCount(4);
    }

    public String getAlert4() {
        return alert4;
    }

    public void setAlert4(String alert4) {
        this.alert4 = alert4;
        setAlertCount(5);
    }

    public String getAlert5() {
        return alert5;
    }

    public void setAlert5(String alert5) {
        this.alert5 = alert5;
        setAlertCount(6);
    }

    public String getAlert6() {
        return alert6;
    }

    public void setAlert6(String alert6) {
        this.alert6 = alert6;
        setAlertCount(7);
    }

    public String getAlert7() {
        return alert7;
    }

    public void setAlert7(String alert7) {
        this.alert7 = alert7;
        setAlertCount(8);
    }

    public String getAlert8() {
        return alert8;
    }

    public void setAlert8(String alert8) {
        this.alert8 = alert8;
        setAlertCount(9);
    }

    public String getAlert9() {
        return alert9;
    }

    public void setAlert9(String alert9) {
        this.alert9 = alert9;
        setAlertCount(10);
    }

    public int getAlertCount() {
        return alertCount;
    }

    private void setAlertCount(int count) {
        if (alertCount < count) {
            alertCount = count;
        }
    }

    private String alert0;
    private String alert1;
    private String alert2;
    private String alert3;
    private String alert4;
    private String alert5;
    private String alert6;
    private String alert7;
    private String alert8;
    private String alert9;
    private int alertCount;

    public AlertListCommand() {
        alertCount = 0;
    }

    @Override
    public byte[] getRequest() {
        return COMMAND;
    }
}
