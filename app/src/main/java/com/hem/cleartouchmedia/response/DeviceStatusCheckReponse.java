package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;

public class DeviceStatusCheckReponse {

    @SerializedName("status")
    String Status;
    @SerializedName("msg")
    String Message;

    public DeviceStatusCheckReponse(String status, String message) {
        Status = status;
        Message = message;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }
}
