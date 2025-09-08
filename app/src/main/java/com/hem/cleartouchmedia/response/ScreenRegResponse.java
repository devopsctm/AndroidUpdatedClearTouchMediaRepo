package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.Screen;

/**
 * Created by LENOVO on 08/10/2022.
 */

public class ScreenRegResponse
{
    @SerializedName("status")
    String Status;
    @SerializedName("random_number")
    String randomNumber;

    public ScreenRegResponse(String status, String randomNumber) {
        Status = status;
        this.randomNumber = randomNumber;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public void setRandomNumber(String randomNumber) {
        this.randomNumber = randomNumber;
    }

    public String getStatus() {
        return Status;
    }

    public String getRandomNumber() {
        return randomNumber;
    }
}
