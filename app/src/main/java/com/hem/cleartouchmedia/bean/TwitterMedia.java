package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TwitterMedia {


    @SerializedName("type")
    String type;

    @SerializedName("url")
    String url;

    @SerializedName("preview_image_url")
    String previewImageUrl;

    @SerializedName("variants")
    List<VideoVariants> variants;

    public TwitterMedia(String type, String url, String previewImageUrl, List<VideoVariants> variants) {
        this.type = type;
        this.url = url;
        this.previewImageUrl = previewImageUrl;
        this.variants = variants;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setPreviewImageUrl(String previewImageUrl) {
        this.previewImageUrl = previewImageUrl;
    }

    public void setVariants(List<VideoVariants> variants) {
        this.variants = variants;
    }

    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getPreviewImageUrl() {
        return previewImageUrl;
    }

    public List<VideoVariants> getVariants() {
        return variants;
    }
}
