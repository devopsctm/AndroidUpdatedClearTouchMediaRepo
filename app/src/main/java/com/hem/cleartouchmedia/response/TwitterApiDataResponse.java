package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.Screen;

/**
 * Created by LENOVO on 16/09/2018.
 */

public class TwitterApiDataResponse
{

    @SerializedName("id")
    String id;

    @SerializedName("apps_id")
    String appsId;

    @SerializedName("type")
    String type;

    @SerializedName("search_type")
    String searchType;

    @SerializedName("term")
    String term;

    @SerializedName("tweet_count_display")
    String tweetCountDisplay;

    @SerializedName("slide_duration")
    String slideDuration;

    @SerializedName("provider_id")
    String providerId;

    @SerializedName("nickname")
    String nickname;

    @SerializedName("name")
    String name;

    @SerializedName("image")
    String image;

    public TwitterApiDataResponse(String id, String appsId, String type, String searchType, String term, String tweetCountDisplay, String slideDuration, String providerId, String nickname, String name, String image) {
        this.id = id;
        this.appsId = appsId;
        this.type = type;
        this.searchType = searchType;
        this.term = term;
        this.tweetCountDisplay = tweetCountDisplay;
        this.slideDuration = slideDuration;
        this.providerId = providerId;
        this.nickname = nickname;
        this.name = name;
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAppsId(String appsId) {
        this.appsId = appsId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setTweetCountDisplay(String tweetCountDisplay) {
        this.tweetCountDisplay = tweetCountDisplay;
    }

    public void setSlideDuration(String slideDuration) {
        this.slideDuration = slideDuration;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public String getAppsId() {
        return appsId;
    }

    public String getType() {
        return type;
    }

    public String getSearchType() {
        return searchType;
    }

    public String getTerm() {
        return term;
    }

    public String getTweetCountDisplay() {
        return tweetCountDisplay;
    }

    public String getSlideDuration() {
        return slideDuration;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
