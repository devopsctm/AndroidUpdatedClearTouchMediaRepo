package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class WeatherCity {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("country")
    String country;

    public WeatherCity(String id, String name, String country) {
        this.id = id;
        this.name = name;
        this.country = country;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
