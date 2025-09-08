package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class TwitterData {

    @SerializedName("created_at")
    String created_at;

    @SerializedName("text")
    String text;

    @SerializedName("id")
    String id;

    public TwitterData(String created_at, String text, String id) {
        this.created_at = created_at;
        this.text = text;
        this.id = id;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getText() {
        return text;
    }

    public String getId() {
        return id;
    }
}
