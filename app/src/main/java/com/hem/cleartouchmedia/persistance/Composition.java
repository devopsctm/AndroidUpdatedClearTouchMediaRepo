package com.hem.cleartouchmedia.persistance;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity(tableName = "composition")
public class Composition {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    private String comID;

    @NonNull
    private String layoutType;

    @NonNull
    private String compositionId;

    @NonNull
    private String zoneType;

    @NonNull
    private String status;

    public void setId(int id) {
        this.id = id;
    }

    public void setComID(@NonNull String comID) {
        this.comID = comID;
    }

    public void setLayoutType(@NonNull String layoutType) {
        this.layoutType = layoutType;
    }

    public void setCompositionId(@NonNull String compositionId) {
        this.compositionId = compositionId;
    }

    public void setZoneType(@NonNull String zoneType) {
        this.zoneType = zoneType;
    }

    public void setStatus(@NonNull String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public String getComID() {
        return comID;
    }

    @NonNull
    public String getLayoutType() {
        return layoutType;
    }

    @NonNull
    public String getCompositionId() {
        return compositionId;
    }

    @NonNull
    public String getZoneType() {
        return zoneType;
    }

    @NonNull
    public String getStatus() {
        return status;
    }
}
