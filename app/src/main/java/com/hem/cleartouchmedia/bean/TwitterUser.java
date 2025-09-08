package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class TwitterUser {

    @SerializedName("id")
    String id;

    @SerializedName("description")
    String description;

    @SerializedName("profile_image_url")
    String profile_image_url;

    @SerializedName("created_at")
    String created_at;

    @SerializedName("name")
    String name;

    @SerializedName("url")
    String url;

    @SerializedName("verified")
    String verified;

    @SerializedName("username")
    String username;

    @SerializedName("location")
    String location;

    public TwitterUser(String id, String description, String profile_image_url, String created_at,
                       String name, String url, String verified, String username, String location) {
        this.id = id;
        this.description = description;
        this.profile_image_url = profile_image_url;
        this.created_at = created_at;
        this.name = name;
        this.url = url;
        this.verified = verified;
        this.username = username;
        this.location = location;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getVerified() {
        return verified;
    }

    public String getUsername() {
        return username;
    }

    public String getLocation() {
        return location;
    }
}
