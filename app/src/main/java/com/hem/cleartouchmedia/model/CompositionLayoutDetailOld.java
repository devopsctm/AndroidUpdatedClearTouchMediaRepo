package com.hem.cleartouchmedia.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "compositionLayoutDetail",indices = @Index(value = {"id"},unique = true))
public class CompositionLayoutDetailOld {

    @PrimaryKey@NonNull
    @SerializedName("id")
    @ColumnInfo(name = "id")
    String id;

    @SerializedName("duration")
    @Nullable
    @ColumnInfo(name = "duration")
    String duration;

    @SerializedName("media_id")
    @Nullable
    @ColumnInfo(name = "media_id")
    String media_id;

    @SerializedName("layout_type")
    @Nullable
    @ColumnInfo(name = "layout_type")
    String layout_type;

    @SerializedName("composition_id")
    @Nullable
    @ColumnInfo(name = "composition_id")
    String composition_id;

    @SerializedName("zone_type")
    @Nullable
    @ColumnInfo(name = "zone_type")
    String zone_type;

    @SerializedName("app_type")
    @Nullable
    @ColumnInfo(name = "app_type")
    String app_type;

    @SerializedName("status")
    @Nullable
    @ColumnInfo(name = "status")
    String status;

    @SerializedName("type")
    @Nullable
    @ColumnInfo(name = "type")
    String type;

    @SerializedName("created_at")
    @Nullable
    @ColumnInfo(name = "created_at")
    String created_at;

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setDuration(@Nullable String duration) {
        this.duration = duration;
    }

    public void setMedia_id(@Nullable String media_id) {
        this.media_id = media_id;
    }

    public void setLayout_type(@Nullable String layout_type) {
        this.layout_type = layout_type;
    }

    public void setComposition_id(@Nullable String composition_id) {
        this.composition_id = composition_id;
    }

    public void setZone_type(@Nullable String zone_type) {
        this.zone_type = zone_type;
    }

    public void setApp_type(@Nullable String app_type) {
        this.app_type = app_type;
    }

    public void setStatus(@Nullable String status) {
        this.status = status;
    }

    public void setType(@Nullable String type) {
        this.type = type;
    }

    public void setCreated_at(@Nullable String created_at) {
        this.created_at = created_at;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @Nullable
    public String getDuration() {
        return duration;
    }

    @Nullable
    public String getMedia_id() {
        return media_id;
    }

    @Nullable
    public String getLayout_type() {
        return layout_type;
    }

    @Nullable
    public String getComposition_id() {
        return composition_id;
    }

    @Nullable
    public String getZone_type() {
        return zone_type;
    }

    @Nullable
    public String getApp_type() {
        return app_type;
    }

    @Nullable
    public String getStatus() {
        return status;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public String getCreated_at() {
        return created_at;
    }
}
