package com.hem.cleartouchmedia.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.hem.cleartouchmedia.model.CompositionDetail;
import com.hem.cleartouchmedia.model.CompositionLayoutDetail;

import java.util.List;

@Dao
public interface CompositionLayoutDetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CompositionLayoutDetail[] compositionLayoutDetails);

    @Query("SELECT DISTINCT * FROM CompositionLayoutDetail")
    LiveData<List<CompositionLayoutDetail>>  getCompositionLayoutDetail();

    /*@Query("SELECT DISTINCT * FROM CompositionLayoutDetail")
    List<CompositionLayoutDetail>  getCompositionLayoutList();*/

    @Query("SELECT DISTINCT * FROM CompositionLayoutDetail WHERE media_id = :media_id")
    CompositionLayoutDetail getCompositionLayoutDetailByMediaId(String media_id);

    @Query("DELETE FROM CompositionLayoutDetail")
    void deleteAll();

    @Delete
    void delete( CompositionLayoutDetail detail );
}
