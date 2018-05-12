package com.hapramp.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ankit on 5/12/2018.
 */

public class DeviceId {
    @SerializedName("deviceID")
    public String deviceId;

    public DeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
