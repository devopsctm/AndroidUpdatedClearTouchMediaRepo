package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.Screen;

import java.io.Serializable;
import java.util.List;

/**
 * Created by LENOVO on 08/10/2022.
 */

public class ScreenResponse
{
    @SerializedName("status")
    String Status;
    @SerializedName("msg")
    String Message;

    @SerializedName("data")
    Screen screen;

    public void setStatus(String status) {
        Status = status;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public String getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public Screen getScreen() {
        return screen;
    }
}
