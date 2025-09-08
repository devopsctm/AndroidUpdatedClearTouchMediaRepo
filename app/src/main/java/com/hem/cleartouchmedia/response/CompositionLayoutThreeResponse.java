package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.CompositionLayoutThreeDetail;

/**
 * Created by LENOVO on 09/09/2022.
 */

public class CompositionLayoutThreeResponse
{

    @SerializedName("status")
    String Status;
    @SerializedName("msg")
    String Message;
    @SerializedName("data")
    CompositionLayoutThreeDetail compositionLayoutThreeDetail;

    public CompositionLayoutThreeDetail getCompositionLayoutThreeDetail() {
        return compositionLayoutThreeDetail;
    }

    public void setCompositionLayoutThreeDetail(CompositionLayoutThreeDetail compositionLayoutThreeDetail) {
        this.compositionLayoutThreeDetail = compositionLayoutThreeDetail;
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
