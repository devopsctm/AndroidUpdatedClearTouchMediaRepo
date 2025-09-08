package com.hem.cleartouchmedia.response;

public class TwitterMediaData {
    // image url is used to
    // store the url of image
    private String mediaID;
    private String mediaUrl;
    private String mediaType;

    public TwitterMediaData(String mediaID, String mediaUrl, String mediaType) {
        this.mediaID = mediaID;
        this.mediaUrl = mediaUrl;
        this.mediaType = mediaType;
    }

    public void setMediaID(String mediaID) {
        this.mediaID = mediaID;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getMediaID() {
        return mediaID;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }
}
