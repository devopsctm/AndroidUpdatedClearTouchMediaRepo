package com.hem.cleartouchmedia.model;


import com.google.gson.annotations.SerializedName;

public class CompositionCustomLayoutDetail {
    @SerializedName("zone")
    String zone;
    @SerializedName("width")
    String width;
    @SerializedName("height")
    String height;
    @SerializedName("top")
    String top;
    @SerializedName("left")
    String left;

    public CompositionCustomLayoutDetail(String zone, String width, String height, String top, String left) {
        this.zone = zone;
        this.width = width;
        this.height = height;
        this.top = top;
        this.left = left;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setTop(String top) {
        this.top = top;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getZone() {
        return zone;
    }

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getTop() {
        return top;
    }

    public String getLeft() {
        return left;
    }
}
