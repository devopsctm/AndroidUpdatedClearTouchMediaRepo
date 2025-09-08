package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.CompositionDetail;

/**
 * Created by LENOVO on 09/09/2022.
 */

public class CompositionResponse
{

    @SerializedName("status")
    String Status;
    @SerializedName("msg")
    String Message;
    @SerializedName("data")
    CompositionDetail compositionDetail;

    public void setStatus(String status) {
        Status = status;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public void setCompositionDetail(CompositionDetail compositionDetail) {
        this.compositionDetail = compositionDetail;
    }

    public String getStatus() {
        return Status;
    }

    public CompositionDetail getCompositionDetail() {
        return compositionDetail;
    }

    public String getMessage() {
        return Message;
    }

}
