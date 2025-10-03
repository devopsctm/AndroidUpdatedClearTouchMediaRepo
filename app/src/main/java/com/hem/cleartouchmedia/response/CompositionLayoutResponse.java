package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.CompositionLayoutDetail;

import java.util.List;

/**
 * Created by LENOVO on 09/09/2022.
 */

public class CompositionLayoutResponse
{
    @SerializedName("status")
    String Status;
    @SerializedName("msg")
    String Message;
    @SerializedName("data")
    List<CompositionLayoutDetail> compositionLayoutDetail;

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

    public void setCompositionLayoutDetail(List<CompositionLayoutDetail> compositionLayoutDetail) {
        this.compositionLayoutDetail = compositionLayoutDetail;
    }

   /* public List<CompositionLayoutDetail> getCompositionLayoutDetail() {
        return compositionLayoutDetail;
    }*/

    public List<CompositionLayoutDetail> getCompositionLayoutDetail() {
        return (compositionLayoutDetail == null) ? java.util.Collections.emptyList() : compositionLayoutDetail;
    }
}
