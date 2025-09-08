package com.hem.cleartouchmedia.response;

public class TweetData {
    // image url is used to
    // store the url of image
    private String tweetID;
    private String tweetUrl;
    private String tweetTitle;
    private String tweetType;


    // Constructor method.
    public TweetData(String id, String url, String title, String type) {
        this.tweetID = id;
        this.tweetUrl = url;
        this.tweetTitle = title;
        this.tweetType = type;
    }

    // Getter method
    public String getTweetID() {
        return tweetID;
    }

    // Getter method
    public String getTweetUrl() {
        return tweetUrl;
    }

    // Getter method
    public String getTweetTitle() {
        return tweetTitle;
    }

    // Getter method
    public String getTweetType() {
        return tweetType;
    }

    // Setter method
    public void setTweetID(String id) {
        this.tweetID = id;
    }

    // Setter method
    public void setTweetUrl(String url) {
        this.tweetUrl = url;
    }

    // Setter method
    public void setTweetTitle(String title) {
        this.tweetTitle = title;
    }

    // Setter method
    public void setTweetType(String type) {
        this.tweetType = type;
    }
}
