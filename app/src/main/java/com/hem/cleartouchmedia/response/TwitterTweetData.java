package com.hem.cleartouchmedia.response;

public class TwitterTweetData {
    // image url is used to
    // store the url of image
    private String tweetID;
    private String tweetMediaKeys;
    private String tweetTitle;

    public TwitterTweetData(String tweetID, String tweetMediaKeys, String tweetTitle) {
        this.tweetID = tweetID;
        this.tweetMediaKeys = tweetMediaKeys;
        this.tweetTitle = tweetTitle;
    }

    public void setTweetID(String tweetID) {
        this.tweetID = tweetID;
    }

    public void setTweetMediaKeys(String tweetMediaKeys) {
        this.tweetMediaKeys = tweetMediaKeys;
    }

    public void setTweetTitle(String tweetTitle) {
        this.tweetTitle = tweetTitle;
    }

    public String getTweetID() {
        return tweetID;
    }

    public String getTweetMediaKeys() {
        return tweetMediaKeys;
    }

    public String getTweetTitle() {
        return tweetTitle;
    }
}
