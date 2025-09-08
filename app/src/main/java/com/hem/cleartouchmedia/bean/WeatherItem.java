package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class WeatherItem {

    @SerializedName("temp")
    String temp;

    @SerializedName("feels_like")
    String feelsLike;

    @SerializedName("temp_min")
    String tempMin;

    @SerializedName("temp_max")
    String tempMax;

    @SerializedName("pressure")
    String pressure;

    @SerializedName("sea_level")
    String seaLevel;

    @SerializedName("grnd_level")
    String grndLevel;

    @SerializedName("humidity")
    String humidity;

    @SerializedName("temp_kf")
    String tempKF;

    public WeatherItem(String temp, String feelsLike, String tempMin, String tempMax, String pressure, String seaLevel, String grndLevel, String humidity, String tempKF) {
        this.temp = temp;
        this.feelsLike = feelsLike;
        this.tempMin = tempMin;
        this.tempMax = tempMax;
        this.pressure = pressure;
        this.seaLevel = seaLevel;
        this.grndLevel = grndLevel;
        this.humidity = humidity;
        this.tempKF = tempKF;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public void setTempMin(String tempMin) {
        this.tempMin = tempMin;
    }

    public void setTempMax(String tempMax) {
        this.tempMax = tempMax;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public void setSeaLevel(String seaLevel) {
        this.seaLevel = seaLevel;
    }

    public void setGrndLevel(String grndLevel) {
        this.grndLevel = grndLevel;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setTempKF(String tempKF) {
        this.tempKF = tempKF;
    }

    public String getTemp() {
        return temp;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public String getTempMin() {
        return tempMin;
    }

    public String getTempMax() {
        return tempMax;
    }

    public String getPressure() {
        return pressure;
    }

    public String getSeaLevel() {
        return seaLevel;
    }

    public String getGrndLevel() {
        return grndLevel;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getTempKF() {
        return tempKF;
    }
}
