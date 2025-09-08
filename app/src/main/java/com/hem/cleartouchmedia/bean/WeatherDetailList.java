package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class WeatherDetailList {

    @SerializedName("id")
    String id;

    @SerializedName("main")
    String main;

    @SerializedName("description")
    String description;

    @SerializedName("icon")
    String icon;

    public WeatherDetailList(String id, String main, String description, String icon) {
        this.id = id;
        this.main = main;
        this.description = description;
        this.icon = icon;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public String getMain() {
        return main;
    }

    public String getDescription() {
        return description;
    }

    public String getIcon() {
        return icon;
    }
}
