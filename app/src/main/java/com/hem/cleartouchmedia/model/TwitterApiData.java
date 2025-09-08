package com.hem.cleartouchmedia.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "TwitterApiData",indices = @Index(value = {"id"},unique = true))
public class TwitterApiData {

    @PrimaryKey@NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    String id;

    @SerializedName("media_id")
    @ColumnInfo(name = "media_id")
    String mediaId;

    @SerializedName("apps_id")
    @ColumnInfo(name = "apps_id")
    String appsId;

    @SerializedName("type")
    @ColumnInfo(name = "type")
    String type;

    @SerializedName("tweet_count_display")
    @ColumnInfo(name = "tweet_count_display")
    String tweetCountDisplay;

    @SerializedName("provider_id")
    @ColumnInfo(name = "provider_id")
    String providerId;

    @SerializedName("slide_duration")
    @ColumnInfo(name = "slide_duration")
    String slideDuration;

    @SerializedName("term")
    @ColumnInfo(name = "term")
    String term;

    @SerializedName("search_type")
    @ColumnInfo(name = "search_type")
    String searchType;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    String name;

    @SerializedName("nickname")
    @ColumnInfo(name = "nickname")
    String nickName;

    @SerializedName("image")
    @ColumnInfo(name = "image")
    String image;


    public TwitterApiData(@NonNull String id, String mediaId, String appsId, String type, String tweetCountDisplay, String providerId, String slideDuration, String term, String searchType, String name, String nickName, String image) {
        this.id = id;
        this.mediaId = mediaId;
        this.appsId = appsId;
        this.type = type;
        this.tweetCountDisplay = tweetCountDisplay;
        this.providerId = providerId;
        this.slideDuration = slideDuration;
        this.term = term;
        this.searchType = searchType;
        this.name = name;
        this.nickName = nickName;
        this.image = image;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setAppsId(String appsId) {
        this.appsId = appsId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setTweetCountDisplay(String tweetCountDisplay) {
        this.tweetCountDisplay = tweetCountDisplay;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setSlideDuration(String slideDuration) {
        this.slideDuration = slideDuration;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getAppsId() {
        return appsId;
    }

    public String getType() {
        return type;
    }

    public String getTweetCountDisplay() {
        return tweetCountDisplay;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getSlideDuration() {
        return slideDuration;
    }

    public String getTerm() {
        return term;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public String getImage() {
        return image;
    }
}
