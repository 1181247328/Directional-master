package com.cdqf.dire_class;

public class BlePosition {
    private String ble;

    private String bleName;

    public BlePosition(String ble, String bleName) {
        this.ble = ble;
        this.bleName = bleName;
    }

    public String getBle() {
        return ble;
    }

    public void setBle(String ble) {
        this.ble = ble;
    }

    public String getBleName() {
        return bleName;
    }

    public void setBleName(String bleName) {
        this.bleName = bleName;
    }
}
