package com.hem.cleartouchmedia.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CompositionCustomLayoutModel {
    @SerializedName("count_zones")
    String countZones;
    @SerializedName("layout_data")
    List<CompositionCustomLayoutDetail> compositionCustomLayoutDetail;

    public CompositionCustomLayoutModel(String countZones, List<CompositionCustomLayoutDetail> compositionCustomLayoutDetail) {
        this.countZones = countZones;
        this.compositionCustomLayoutDetail = compositionCustomLayoutDetail;
    }

    public void setCountZones(String countZones) {
        this.countZones = countZones;
    }

    public void setCompositionCustomLayoutDetail(List<CompositionCustomLayoutDetail> compositionCustomLayoutDetail) {
        this.compositionCustomLayoutDetail = compositionCustomLayoutDetail;
    }

    public String getCountZones() {
        return countZones;
    }

    public List<CompositionCustomLayoutDetail> getCompositionCustomLayoutDetail() {
        return compositionCustomLayoutDetail;
    }
}
