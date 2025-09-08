package com.hem.cleartouchmedia.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "weatherDetail",indices = @Index(value = {"id"},unique = true))
public class WeatherDetail {

    @PrimaryKey@NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    String id;

    @SerializedName("media_id")
    @ColumnInfo(name = "media_id")
    String mediaId;

    @SerializedName("weather_city")
    @ColumnInfo(name = "weather_city")
    String weatherCity;

    @SerializedName("weather_country")
    @ColumnInfo(name = "weather_country")
    String weatherCountry;

    @SerializedName("main_temp")
    @ColumnInfo(name = "main_temp")
    String mainTemp;

    @SerializedName("main_feels_like")
    @ColumnInfo(name = "main_feels_like")
    String mainFeelsLike;

    @SerializedName("main_temp_min")
    @ColumnInfo(name = "main_temp_min")
    String mainTempMin;

    @SerializedName("main_temp_max")
    @ColumnInfo(name = "main_temp_max")
    String mainTempMax;

    @SerializedName("main_humidity")
    @ColumnInfo(name = "main_humidity")
    String mainHumidity;

    @SerializedName("main_weather_type")
    @ColumnInfo(name = "main_weather_type")
    String mainWeatherType;

    @SerializedName("main_weather_date")
    @ColumnInfo(name = "main_weather_date")
    String mainWeatherDate;

    @SerializedName("day_one_temp")
    @ColumnInfo(name = "day_one_temp")
    String dayOneTemp;

    @SerializedName("day_one_feels_like")
    @ColumnInfo(name = "day_one_feels_like")
    String dayOneFeelsLike;

    @SerializedName("day_one_temp_min")
    @ColumnInfo(name = "day_one_temp_min")
    String dayOneTempMin;

    @SerializedName("day_one_temp_max")
    @ColumnInfo(name = "day_one_temp_max")
    String dayOneTempMax;

    @SerializedName("day_one_humidity")
    @ColumnInfo(name = "day_one_humidity")
    String dayOneHumidity;

    @SerializedName("day_one_weather_type")
    @ColumnInfo(name = "day_one_weather_type")
    String dayOneWeatherType;

    @SerializedName("day_one_weather_date")
    @ColumnInfo(name = "day_one_weather_date")
    String dayOneWeatherDate;

    @SerializedName("day_two_temp")
    @ColumnInfo(name = "day_two_temp")
    String dayTwoTemp;

    @SerializedName("day_two_feels_like")
    @ColumnInfo(name = "day_two_feels_like")
    String dayTwoFeelsLike;

    @SerializedName("day_two_temp_min")
    @ColumnInfo(name = "day_two_temp_min")
    String dayTwoTempMin;

    @SerializedName("day_two_temp_max")
    @ColumnInfo(name = "day_two_temp_max")
    String dayTwoTempMax;

    @SerializedName("day_two_humidity")
    @ColumnInfo(name = "day_two_humidity")
    String dayTwoHumidity;

    @SerializedName("day_two_weather_type")
    @ColumnInfo(name = "day_two_weather_type")
    String dayTwoWeatherType;

    @SerializedName("day_two_weather_date")
    @ColumnInfo(name = "day_two_weather_date")
    String dayTwoWeatherDate;

    @SerializedName("day_three_temp")
    @ColumnInfo(name = "day_three_temp")
    String dayThreeTemp;

    @SerializedName("day_three_feels_like")
    @ColumnInfo(name = "day_three_feels_like")
    String dayThreeFeelsLike;

    @SerializedName("day_three_temp_min")
    @ColumnInfo(name = "day_three_temp_min")
    String dayThreeTempMin;

    @SerializedName("day_three_temp_max")
    @ColumnInfo(name = "day_three_temp_max")
    String dayThreeTempMax;

    @SerializedName("day_three_humidity")
    @ColumnInfo(name = "day_three_humidity")
    String dayThreeHumidity;

    @SerializedName("day_three_weather_type")
    @ColumnInfo(name = "day_three_weather_type")
    String dayThreeWeatherType;

    @SerializedName("day_three_weather_date")
    @ColumnInfo(name = "day_three_weather_date")
    String dayThreeWeatherDate;

    @SerializedName("day_four_temp")
    @ColumnInfo(name = "day_four_temp")
    String dayFourTemp;

    @SerializedName("day_four_feels_like")
    @ColumnInfo(name = "day_four_feels_like")
    String dayFourFeelsLike;

    @SerializedName("day_four_temp_min")
    @ColumnInfo(name = "day_four_temp_min")
    String dayFourTempMin;

    @SerializedName("day_four_temp_max")
    @ColumnInfo(name = "day_four_temp_max")
    String dayFourTempMax;

    @SerializedName("day_four_humidity")
    @ColumnInfo(name = "day_four_humidity")
    String dayFourHumidity;

    @SerializedName("day_four_weather_type")
    @ColumnInfo(name = "day_four_weather_type")
    String dayFourWeatherType;

    @SerializedName("day_four_weather_date")
    @ColumnInfo(name = "day_four_weather_date")
    String dayFourWeatherDate;

    public WeatherDetail(@NonNull String id, String mediaId, String weatherCity, String weatherCountry, String mainTemp, String mainFeelsLike, String mainTempMin, String mainTempMax, String mainHumidity, String mainWeatherType, String mainWeatherDate, String dayOneTemp, String dayOneFeelsLike, String dayOneTempMin, String dayOneTempMax, String dayOneHumidity, String dayOneWeatherType, String dayOneWeatherDate, String dayTwoTemp, String dayTwoFeelsLike, String dayTwoTempMin, String dayTwoTempMax, String dayTwoHumidity, String dayTwoWeatherType, String dayTwoWeatherDate, String dayThreeTemp, String dayThreeFeelsLike, String dayThreeTempMin, String dayThreeTempMax, String dayThreeHumidity, String dayThreeWeatherType, String dayThreeWeatherDate, String dayFourTemp, String dayFourFeelsLike, String dayFourTempMin, String dayFourTempMax, String dayFourHumidity, String dayFourWeatherType, String dayFourWeatherDate) {
        this.id = id;
        this.mediaId = mediaId;
        this.weatherCity = weatherCity;
        this.weatherCountry = weatherCountry;
        this.mainTemp = mainTemp;
        this.mainFeelsLike = mainFeelsLike;
        this.mainTempMin = mainTempMin;
        this.mainTempMax = mainTempMax;
        this.mainHumidity = mainHumidity;
        this.mainWeatherType = mainWeatherType;
        this.mainWeatherDate = mainWeatherDate;
        this.dayOneTemp = dayOneTemp;
        this.dayOneFeelsLike = dayOneFeelsLike;
        this.dayOneTempMin = dayOneTempMin;
        this.dayOneTempMax = dayOneTempMax;
        this.dayOneHumidity = dayOneHumidity;
        this.dayOneWeatherType = dayOneWeatherType;
        this.dayOneWeatherDate = dayOneWeatherDate;
        this.dayTwoTemp = dayTwoTemp;
        this.dayTwoFeelsLike = dayTwoFeelsLike;
        this.dayTwoTempMin = dayTwoTempMin;
        this.dayTwoTempMax = dayTwoTempMax;
        this.dayTwoHumidity = dayTwoHumidity;
        this.dayTwoWeatherType = dayTwoWeatherType;
        this.dayTwoWeatherDate = dayTwoWeatherDate;
        this.dayThreeTemp = dayThreeTemp;
        this.dayThreeFeelsLike = dayThreeFeelsLike;
        this.dayThreeTempMin = dayThreeTempMin;
        this.dayThreeTempMax = dayThreeTempMax;
        this.dayThreeHumidity = dayThreeHumidity;
        this.dayThreeWeatherType = dayThreeWeatherType;
        this.dayThreeWeatherDate = dayThreeWeatherDate;
        this.dayFourTemp = dayFourTemp;
        this.dayFourFeelsLike = dayFourFeelsLike;
        this.dayFourTempMin = dayFourTempMin;
        this.dayFourTempMax = dayFourTempMax;
        this.dayFourHumidity = dayFourHumidity;
        this.dayFourWeatherType = dayFourWeatherType;
        this.dayFourWeatherDate = dayFourWeatherDate;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getWeatherCity() {
        return weatherCity;
    }

    public String getWeatherCountry() {
        return weatherCountry;
    }

    public String getMainTemp() {
        return mainTemp;
    }

    public String getMainFeelsLike() {
        return mainFeelsLike;
    }

    public String getMainTempMin() {
        return mainTempMin;
    }

    public String getMainTempMax() {
        return mainTempMax;
    }

    public String getMainHumidity() {
        return mainHumidity;
    }

    public String getMainWeatherType() {
        return mainWeatherType;
    }

    public String getMainWeatherDate() {
        return mainWeatherDate;
    }

    public String getDayOneTemp() {
        return dayOneTemp;
    }

    public String getDayOneFeelsLike() {
        return dayOneFeelsLike;
    }

    public String getDayOneTempMin() {
        return dayOneTempMin;
    }

    public String getDayOneTempMax() {
        return dayOneTempMax;
    }

    public String getDayOneHumidity() {
        return dayOneHumidity;
    }

    public String getDayOneWeatherType() {
        return dayOneWeatherType;
    }

    public String getDayOneWeatherDate() {
        return dayOneWeatherDate;
    }

    public String getDayTwoTemp() {
        return dayTwoTemp;
    }

    public String getDayTwoFeelsLike() {
        return dayTwoFeelsLike;
    }

    public String getDayTwoTempMin() {
        return dayTwoTempMin;
    }

    public String getDayTwoTempMax() {
        return dayTwoTempMax;
    }

    public String getDayTwoHumidity() {
        return dayTwoHumidity;
    }

    public String getDayTwoWeatherType() {
        return dayTwoWeatherType;
    }

    public String getDayTwoWeatherDate() {
        return dayTwoWeatherDate;
    }

    public String getDayThreeTemp() {
        return dayThreeTemp;
    }

    public String getDayThreeFeelsLike() {
        return dayThreeFeelsLike;
    }

    public String getDayThreeTempMin() {
        return dayThreeTempMin;
    }

    public String getDayThreeTempMax() {
        return dayThreeTempMax;
    }

    public String getDayThreeHumidity() {
        return dayThreeHumidity;
    }

    public String getDayThreeWeatherType() {
        return dayThreeWeatherType;
    }

    public String getDayThreeWeatherDate() {
        return dayThreeWeatherDate;
    }

    public String getDayFourTemp() {
        return dayFourTemp;
    }

    public String getDayFourFeelsLike() {
        return dayFourFeelsLike;
    }

    public String getDayFourTempMin() {
        return dayFourTempMin;
    }

    public String getDayFourTempMax() {
        return dayFourTempMax;
    }

    public String getDayFourHumidity() {
        return dayFourHumidity;
    }

    public String getDayFourWeatherType() {
        return dayFourWeatherType;
    }

    public String getDayFourWeatherDate() {
        return dayFourWeatherDate;
    }


    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setWeatherCity(String weatherCity) {
        this.weatherCity = weatherCity;
    }

    public void setWeatherCountry(String weatherCountry) {
        this.weatherCountry = weatherCountry;
    }

    public void setMainTemp(String mainTemp) {
        this.mainTemp = mainTemp;
    }

    public void setMainFeelsLike(String mainFeelsLike) {
        this.mainFeelsLike = mainFeelsLike;
    }

    public void setMainTempMin(String mainTempMin) {
        this.mainTempMin = mainTempMin;
    }

    public void setMainTempMax(String mainTempMax) {
        this.mainTempMax = mainTempMax;
    }

    public void setMainHumidity(String mainHumidity) {
        this.mainHumidity = mainHumidity;
    }

    public void setMainWeatherType(String mainWeatherType) {
        this.mainWeatherType = mainWeatherType;
    }

    public void setMainWeatherDate(String mainWeatherDate) {
        this.mainWeatherDate = mainWeatherDate;
    }

    public void setDayOneTemp(String dayOneTemp) {
        this.dayOneTemp = dayOneTemp;
    }

    public void setDayOneFeelsLike(String dayOneFeelsLike) {
        this.dayOneFeelsLike = dayOneFeelsLike;
    }

    public void setDayOneTempMin(String dayOneTempMin) {
        this.dayOneTempMin = dayOneTempMin;
    }

    public void setDayOneTempMax(String dayOneTempMax) {
        this.dayOneTempMax = dayOneTempMax;
    }

    public void setDayOneHumidity(String dayOneHumidity) {
        this.dayOneHumidity = dayOneHumidity;
    }

    public void setDayOneWeatherType(String dayOneWeatherType) {
        this.dayOneWeatherType = dayOneWeatherType;
    }

    public void setDayOneWeatherDate(String dayOneWeatherDate) {
        this.dayOneWeatherDate = dayOneWeatherDate;
    }

    public void setDayTwoTemp(String dayTwoTemp) {
        this.dayTwoTemp = dayTwoTemp;
    }

    public void setDayTwoFeelsLike(String dayTwoFeelsLike) {
        this.dayTwoFeelsLike = dayTwoFeelsLike;
    }

    public void setDayTwoTempMin(String dayTwoTempMin) {
        this.dayTwoTempMin = dayTwoTempMin;
    }

    public void setDayTwoTempMax(String dayTwoTempMax) {
        this.dayTwoTempMax = dayTwoTempMax;
    }

    public void setDayTwoHumidity(String dayTwoHumidity) {
        this.dayTwoHumidity = dayTwoHumidity;
    }

    public void setDayTwoWeatherType(String dayTwoWeatherType) {
        this.dayTwoWeatherType = dayTwoWeatherType;
    }

    public void setDayTwoWeatherDate(String dayTwoWeatherDate) {
        this.dayTwoWeatherDate = dayTwoWeatherDate;
    }

    public void setDayThreeTemp(String dayThreeTemp) {
        this.dayThreeTemp = dayThreeTemp;
    }

    public void setDayThreeFeelsLike(String dayThreeFeelsLike) {
        this.dayThreeFeelsLike = dayThreeFeelsLike;
    }

    public void setDayThreeTempMin(String dayThreeTempMin) {
        this.dayThreeTempMin = dayThreeTempMin;
    }

    public void setDayThreeTempMax(String dayThreeTempMax) {
        this.dayThreeTempMax = dayThreeTempMax;
    }

    public void setDayThreeHumidity(String dayThreeHumidity) {
        this.dayThreeHumidity = dayThreeHumidity;
    }

    public void setDayThreeWeatherType(String dayThreeWeatherType) {
        this.dayThreeWeatherType = dayThreeWeatherType;
    }

    public void setDayThreeWeatherDate(String dayThreeWeatherDate) {
        this.dayThreeWeatherDate = dayThreeWeatherDate;
    }

    public void setDayFourTemp(String dayFourTemp) {
        this.dayFourTemp = dayFourTemp;
    }

    public void setDayFourFeelsLike(String dayFourFeelsLike) {
        this.dayFourFeelsLike = dayFourFeelsLike;
    }

    public void setDayFourTempMin(String dayFourTempMin) {
        this.dayFourTempMin = dayFourTempMin;
    }

    public void setDayFourTempMax(String dayFourTempMax) {
        this.dayFourTempMax = dayFourTempMax;
    }

    public void setDayFourHumidity(String dayFourHumidity) {
        this.dayFourHumidity = dayFourHumidity;
    }

    public void setDayFourWeatherType(String dayFourWeatherType) {
        this.dayFourWeatherType = dayFourWeatherType;
    }

    public void setDayFourWeatherDate(String dayFourWeatherDate) {
        this.dayFourWeatherDate = dayFourWeatherDate;
    }
}
