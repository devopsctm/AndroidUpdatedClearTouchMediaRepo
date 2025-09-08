package com.hem.cleartouchmedia.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.Screen;

@Dao
public interface CompositionDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CompositionDetail[] compositionDetails);

    @Query("SELECT DISTINCT * FROM CompositionDetail")
    LiveData<CompositionDetail>  getCompositionDetail();

    @Query("DELETE FROM CompositionDetail")
    void deleteAll();
}
