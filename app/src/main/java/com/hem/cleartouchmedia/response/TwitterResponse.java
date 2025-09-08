package com.hem.cleartouchmedia.response;

import com.google.gson.annotations.SerializedName;
import com.hem.cleartouchmedia.bean.MediaData;
import com.hem.cleartouchmedia.bean.TwitterDetailByID;
import com.hem.cleartouchmedia.bean.TwitterProfileData;

import java.util.List;

public class TwitterResponse {

    @SerializedName("status")
    String status;

    @SerializedName("twitter_profile_data")
    TwitterProfileData twitterProfileData;

    @SerializedName("twitter_details_by_id")
    List<TwitterDetailByID> twitterDetailsById;

    @SerializedName("media_data")
    MediaData mediaData;

    public TwitterResponse(String status, TwitterProfileData twitterProfileData, List<TwitterDetailByID> twitterDetailsById, MediaData mediaData) {
        this.status = status;
        this.twitterProfileData = twitterProfileData;
        this.twitterDetailsById = twitterDetailsById;
        this.mediaData = mediaData;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTwitterProfileData(TwitterProfileData twitterProfileData) {
        this.twitterProfileData = twitterProfileData;
    }

    public void setTwitterDetailsById(List<TwitterDetailByID> twitterDetailsById) {
        this.twitterDetailsById = twitterDetailsById;
    }

    public void setMediaData(MediaData mediaData) {
        this.mediaData = mediaData;
    }

    public String getStatus() {
        return status;
    }

    public TwitterProfileData getTwitterProfileData() {
        return twitterProfileData;
    }

    public List<TwitterDetailByID> getTwitterDetailsById() {
        return twitterDetailsById;
    }

    public MediaData getMediaData() {
        return mediaData;
    }

    /*@SerializedName("includes")
    TwitterIncludes twitterIncludes;

    @SerializedName("data")
    TwitterData twitterData;

    public TwitterResponse(TwitterIncludes twitterIncludes, TwitterData twitterData) {
        this.twitterIncludes = twitterIncludes;
        this.twitterData = twitterData;
    }

    public void setTwitterIncludes(TwitterIncludes twitterIncludes) {
        this.twitterIncludes = twitterIncludes;
    }

    public void setTwitterData(TwitterData twitterData) {
        this.twitterData = twitterData;
    }

    public TwitterIncludes getTwitterIncludes() {
        return twitterIncludes;
    }

    public TwitterData getTwitterData() {
        return twitterData;
    }*/
}
