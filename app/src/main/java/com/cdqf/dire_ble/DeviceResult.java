package com.cdqf.dire_ble;

import android.bluetooth.le.ScanResult;

public class DeviceResult {
    public ScanResult result;

    public DeviceResult(ScanResult result) {
        this.result = result;
    }
}
