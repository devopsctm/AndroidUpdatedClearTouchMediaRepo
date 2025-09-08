package com.hem.cleartouchmedia.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "CompositionLayoutTwoDetail",indices = @Index(value = {"id"},unique = true))
public class CompositionLayoutTwoDetail {

    @PrimaryKey@NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    String id;

    @SerializedName("title")
    @ColumnInfo(name = "title")
    String title;

    @SerializedName("description")
    @ColumnInfo(name = "description")
    String description;

    @SerializedName("url")
    @ColumnInfo(name = "url")
    String url;

    @SerializedName("duration")
    @ColumnInfo(name = "duration")
    String duration;

    @SerializedName("media_id")
    @ColumnInfo(name = "media_id")
    String media_id;

    @SerializedName("layout_type")
    @ColumnInfo(name = "layout_type")
    String layout_type;

    @SerializedName("composition_id")
    @ColumnInfo(name = "composition_id")
    String composition_id;

    @SerializedName("zone_type")
    @ColumnInfo(name = "zone_type")
    String zone_type;

    @SerializedName("app_type")
    @ColumnInfo(name = "app_type")
    String app_type;

    @SerializedName("mediatype")
    @ColumnInfo(name = "mediatype")
    String mediatype;

    @SerializedName("created_at")
    @ColumnInfo(name = "created_at")
    String created_at;

    @SerializedName("status")
    @ColumnInfo(name = "status")
    String status;

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public void setLayout_type(String layout_type) {
        this.layout_type = layout_type;
    }

    public void setComposition_id(String composition_id) {
        this.composition_id = composition_id;
    }

    public void setZone_type(String zone_type) {
        this.zone_type = zone_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public void setMediatype(String mediatype) {
        this.mediatype = mediatype;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getDuration() {
        return duration;
    }

    public String getMedia_id() {
        return media_id;
    }

    public String getLayout_type() {
        return layout_type;
    }

    public String getComposition_id() {
        return composition_id;
    }

    public String getZone_type() {
        return zone_type;
    }

    public String getApp_type() {
        return app_type;
    }

    public String getMediatype() {
        return mediatype;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getStatus() {
        return status;
    }
}
