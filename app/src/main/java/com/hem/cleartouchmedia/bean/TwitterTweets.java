package com.hem.cleartouchmedia.bean;

import com.google.gson.annotations.SerializedName;

public class TwitterTweets {

    @SerializedName("author_id")
    String author_id;

    @SerializedName("text")
    String description;

    public TwitterTweets(String author_id, String description) {
        this.author_id = author_id;
        this.description = description;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public String getDescription() {
        return description;
    }
}
