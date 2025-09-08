package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.CompositionCustomLayoutModel;

/**
 * Created by LENOVO on 09/09/2022.
 */

public class CompositionCustomLayoutResponse
{
    @SerializedName("status")
    String Status;
    @SerializedName("msg")
    String Message;
    @SerializedName("data")
    CompositionCustomLayoutModel compositionLayoutModel;

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

    public void setCompositionCustomLayoutModel(CompositionCustomLayoutModel compositionLayoutModel) {
        this.compositionLayoutModel = compositionLayoutModel;
    }

    public CompositionCustomLayoutModel getCompositionCustomLayoutModel() {
        return compositionLayoutModel;
    }
}
