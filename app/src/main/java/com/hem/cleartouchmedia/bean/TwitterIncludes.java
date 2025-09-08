package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TwitterIncludes {

    @SerializedName("media")
    List<TwitterMedia> twitterMediaList;

    @SerializedName("users")
    List<TwitterUser> twitterUserList;

    @SerializedName("tweets")
    List<TwitterTweets> twitterTweetsList;

    public TwitterIncludes(List<TwitterMedia> twitterMediaList, List<TwitterUser> twitterUserList, List<TwitterTweets> twitterTweetsList) {
        this.twitterMediaList = twitterMediaList;
        this.twitterUserList = twitterUserList;
        this.twitterTweetsList = twitterTweetsList;
    }

    public List<TwitterMedia> getTwitterMediaList() {
        return twitterMediaList;
    }

    public List<TwitterUser> getTwitterUserList() {
        return twitterUserList;
    }

    public List<TwitterTweets> getTwitterTweetsList() {
        return twitterTweetsList;
    }

    public void setTwitterMediaList(List<TwitterMedia> twitterMediaList) {
        this.twitterMediaList = twitterMediaList;
    }

    public void setTwitterUserList(List<TwitterUser> twitterUserList) {
        this.twitterUserList = twitterUserList;
    }

    public void setTwitterTweetsList(List<TwitterTweets> twitterTweetsList) {
        this.twitterTweetsList = twitterTweetsList;
    }
}
