package com.example.lalal.Tools.BLTH;

/**
 * Created by lalal on 2018/3/7.
 */

public class BlueToothTools {
    private String deviceName;
    private String deviceAddr;

    public BlueToothTools() {
    }

    public BlueToothTools(String deviceName, String deviceAddr) {
        this.deviceName = deviceName;
        this.deviceAddr = deviceAddr;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceAddr() {
        return deviceAddr;
    }

    public void setDeviceAddr(String deviceAddr) {
        this.deviceAddr = deviceAddr;
    }

    @Override
    public String toString() {
        return "BlueToothTools{" +
                "deviceName='" + deviceName + '\'' +
                ", deviceAddr='" + deviceAddr + '\'' +
                '}';
    }
}
