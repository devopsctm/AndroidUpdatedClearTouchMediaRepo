package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.model.Screen;

/**
 * Created by LENOVO on 08/10/2022.
 */

public class MediaData
{
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

    @SerializedName("authenticated_user")
    String authenticatedUser;

    @SerializedName("twitter_id")
    String twitterId;

    public MediaData(String appsId, String type, String searchType, String term, String tweetCountDisplay, String slideDuration, String authenticatedUser, String twitterId) {
        this.appsId = appsId;
        this.type = type;
        this.searchType = searchType;
        this.term = term;
        this.tweetCountDisplay = tweetCountDisplay;
        this.slideDuration = slideDuration;
        this.authenticatedUser = authenticatedUser;
        this.twitterId = twitterId;
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

    public void setAuthenticatedUser(String authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
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

    public String getAuthenticatedUser() {
        return authenticatedUser;
    }

    public String getTwitterId() {
        return twitterId;
    }
}
