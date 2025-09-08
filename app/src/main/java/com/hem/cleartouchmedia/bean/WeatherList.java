package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class WeatherList {

    @SerializedName("main")
    WeatherItem weatherItem;

    @SerializedName("weather")
    WeatherDetailList weatherDetailList;

    @SerializedName("dt_txt")
    String weatherDate;

    public WeatherList(WeatherItem weatherItem, WeatherDetailList weatherDetailList, String weatherDate) {
        this.weatherItem = weatherItem;
        this.weatherDetailList = weatherDetailList;
        this.weatherDate = weatherDate;
    }

    public void setWeatherItem(WeatherItem weatherItem) {
        this.weatherItem = weatherItem;
    }

    public void setWeatherDetailList(WeatherDetailList weatherDetailList) {
        this.weatherDetailList = weatherDetailList;
    }

    public void setWeatherDate(String weatherDate) {
        this.weatherDate = weatherDate;
    }

    public WeatherItem getWeatherItem() {
        return weatherItem;
    }

    public WeatherDetailList getWeatherDetailList() {
        return weatherDetailList;
    }

    public String getWeatherDate() {
        return weatherDate;
    }
}
