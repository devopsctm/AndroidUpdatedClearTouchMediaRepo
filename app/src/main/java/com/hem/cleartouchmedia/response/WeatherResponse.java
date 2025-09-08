package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.bean.WeatherCity;
import com.hem.cleartouchmedia.bean.WeatherList;

import java.util.List;

public class WeatherResponse {

    @SerializedName("cod")
    String cod;

    @SerializedName("message")
    String message;

    @SerializedName("cnt")
    String cnt;

    @SerializedName("list")
    List<WeatherList> weatherLists;

    @SerializedName("city")
    WeatherCity weatherCity;

    public WeatherResponse(String cod, String message, String cnt, List<WeatherList> weatherLists, WeatherCity weatherCity) {
        this.cod = cod;
        this.message = message;
        this.cnt = cnt;
        this.weatherLists = weatherLists;
        this.weatherCity = weatherCity;
    }

    public String getCod() {
        return cod;
    }

    public String getMessage() {
        return message;
    }

    public String getCnt() {
        return cnt;
    }

    public List<WeatherList> getWeatherLists() {
        return weatherLists;
    }

    public WeatherCity getWeatherCity() {
        return weatherCity;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCnt(String cnt) {
        this.cnt = cnt;
    }

    public void setWeatherLists(List<WeatherList> weatherLists) {
        this.weatherLists = weatherLists;
    }

    public void setWeatherCity(WeatherCity weatherCity) {
        this.weatherCity = weatherCity;
    }
}
