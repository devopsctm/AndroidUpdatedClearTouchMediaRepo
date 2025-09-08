package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LENOVO on 08/10/2022.
 */

public class DevicePushData
{
    @SerializedName("device_id")
    String deviceID;

    @SerializedName("composition_id")
    String compositionID;

    @SerializedName("push_status")
    String pushStatus;

    @SerializedName("device_status")
    String deviceStatus;

    public DevicePushData(String deviceID, String compositionID, String pushStatus, String deviceStatus) {
        this.deviceID = deviceID;
        this.compositionID = compositionID;
        this.pushStatus = pushStatus;
        this.deviceStatus = deviceStatus;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public void setCompositionID(String compositionID) {
        this.compositionID = compositionID;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    public void setDeviceStatus(String deviceStatus) {
        this.deviceStatus = deviceStatus;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public String getCompositionID() {
        return compositionID;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public String getDeviceStatus() {
        return deviceStatus;
    }
}
