package com.hem.cleartouchmedia.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hem.cleartouchmedia.model.Screen;

import java.util.List;

@Dao
public interface ScreenDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Screen[] screen);

    @Query("SELECT DISTINCT * FROM Screen")
    LiveData<Screen>  getScreen();

    @Query("DELETE FROM Screen")
    void deleteAll();
}
