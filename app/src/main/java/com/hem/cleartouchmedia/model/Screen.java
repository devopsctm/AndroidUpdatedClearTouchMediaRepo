package com.hem.cleartouchmedia.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "screen",indices = @Index(value = {"id"},unique = true))
public class Screen {

    @PrimaryKey@NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    String id;

    @SerializedName("composition_id")
    @ColumnInfo(name = "composition_id")
    String composition_id;

    @SerializedName("orientation")
    @ColumnInfo(name = "orientation")
    String orientation;

    @SerializedName("media_id")
    @ColumnInfo(name = "media_id")
    String mediaId;

    @SerializedName("layout_type")
    @ColumnInfo(name = "layout_type")
    String layoutType;

    @SerializedName("zone_type")
    @ColumnInfo(name = "zone_type")
    String zoneType;

    @SerializedName("app_type")
    @ColumnInfo(name = "app_type")
    String appType;

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setComposition_id(String composition_id) {
        this.composition_id = composition_id;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public void setLayoutType(String layoutType) {
        this.layoutType = layoutType;
    }

    public void setZoneType(String zoneType) {
        this.zoneType = zoneType;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public String getComposition_id() {
        return composition_id;
    }

    public String getOrientation() {
        return orientation;
    }

    public String getMediaId() {
        return mediaId;
    }

    public String getLayoutType() {
        return layoutType;
    }

    public String getZoneType() {
        return zoneType;
    }

    public String getAppType() {
        return appType;
    }
}
