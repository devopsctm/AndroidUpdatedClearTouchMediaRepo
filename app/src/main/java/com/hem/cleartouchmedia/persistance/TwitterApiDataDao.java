package com.hem.cleartouchmedia.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hem.cleartouchmedia.model.TwitterApiData;
import com.hem.cleartouchmedia.model.WeatherDetail;

@Dao
public interface TwitterApiDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TwitterApiData[] twitterApiData);

    @Query("SELECT DISTINCT * FROM TwitterApiData")
    LiveData<TwitterApiData>  getTwitterApiData();

    @Query("DELETE FROM TwitterApiData")
    void deleteAll();
}
