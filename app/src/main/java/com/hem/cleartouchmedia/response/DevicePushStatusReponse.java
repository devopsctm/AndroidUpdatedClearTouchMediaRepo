package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.bean.DevicePushData;

public class DevicePushStatusReponse {

    @SerializedName("status")
    String Status;

    @SerializedName("msg")
    String Message;

    @SerializedName("device_push_data")
    DevicePushData devicePushData;

    public DevicePushStatusReponse(String status, String message, DevicePushData devicePushData) {
        Status = status;
        Message = message;
        this.devicePushData = devicePushData;
    }


    public void setStatus(String status) {
        Status = status;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setDevicePushData(DevicePushData devicePushData) {
        this.devicePushData = devicePushData;
    }

    public String getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public DevicePushData getDevicePushData() {
        return devicePushData;
    }
}
