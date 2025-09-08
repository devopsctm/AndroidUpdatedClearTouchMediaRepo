package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class VideoVariants {

    @SerializedName("content_type")
    String contentType;

    @SerializedName("url")
    String url;

    public VideoVariants(String contentType, String url) {
        this.contentType = contentType;
        this.url = url;
    }

    public String getContentType() {
        return contentType;
    }

    public String getUrl() {
        return url;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
