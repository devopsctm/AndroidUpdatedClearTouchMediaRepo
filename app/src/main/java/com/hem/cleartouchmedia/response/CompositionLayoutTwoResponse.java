package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.CompositionLayoutTwoDetail;

/**
 * Created by LENOVO on 09/09/2022.
 */

public class CompositionLayoutTwoResponse
{

    @SerializedName("status")
    String Status;
    @SerializedName("msg")
    String Message;
    @SerializedName("data")
    CompositionLayoutTwoDetail compositionLayoutTwoDetail;

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

    public void setCompositionLayoutTwoDetail(CompositionLayoutTwoDetail compositionLayoutTwoDetail) {
        this.compositionLayoutTwoDetail = compositionLayoutTwoDetail;
    }

    public CompositionLayoutTwoDetail getCompositionLayoutTwoDetail() {
        return compositionLayoutTwoDetail;
    }
}
