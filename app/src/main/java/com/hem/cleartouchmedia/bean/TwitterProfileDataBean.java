package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class TwitterProfileDataBean {

    @SerializedName("location")
    String location;

    @SerializedName("description")
    String description;

    @SerializedName("name")
    String name;

    @SerializedName("created_at")
    String createdAt;

    @SerializedName("url")
    String url;

    @SerializedName("profile_image_url")
    String profileImageUrl;

    @SerializedName("username")
    String username;

    public TwitterProfileDataBean(String location, String description, String name, String createdAt, String url, String profileImageUrl, String username) {
        this.location = location;
        this.description = description;
        this.name = name;
        this.createdAt = createdAt;
        this.url = url;
        this.profileImageUrl = profileImageUrl;
        this.username = username;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUrl() {
        return url;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getUsername() {
        return username;
    }
}
