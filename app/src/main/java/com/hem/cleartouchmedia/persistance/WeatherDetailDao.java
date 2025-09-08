package com.hem.cleartouchmedia.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.WeatherDetail;

import java.util.List;

@Dao
public interface WeatherDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WeatherDetail[] weatherDetails);

    @Query("SELECT DISTINCT * FROM WeatherDetail")
    LiveData<WeatherDetail>  getWeatherDetail();

    @Query("DELETE FROM WeatherDetail")
    void deleteAll();
}
