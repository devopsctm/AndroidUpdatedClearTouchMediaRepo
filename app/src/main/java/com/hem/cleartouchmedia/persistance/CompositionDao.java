package com.hem.cleartouchmedia.persistance;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CompositionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void saveAll(List<Composition> compositions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Composition composition);

    @Query("SELECT * FROM Composition ORDER BY id DESC")
    LiveData<List<Composition>> findAll();

//    @Query("SELECT * FROM Composition WHERE name like :text ORDER BY id DESC")
//    LiveData<List<Composition>> search(String text );

    @Query("SELECT * FROM Composition WHERE id like :id ORDER BY id DESC")
    LiveData<List<Composition>> findByID(int id );

    @Query("SELECT * FROM Composition WHERE comID = :id")
    Composition findSingleRecordByID(String id );
//    fun findByID(int id): LiveData<List<Document>>;

    //Delete one item by id
    @Query("DELETE FROM Composition WHERE id = :itemId")
    void deleteByItemId(int itemId);

    /**
     * Updating only price
     * By order id
     */
//    @Query("UPDATE Composition SET compositionId=:compositionId WHERE comID = :id")
//    void updateComposition(int compositionId, String id, byte[] img);

    /**
     * Updating only price
     * By order id
     */
    @Query("UPDATE Composition SET compositionId=:compositionId WHERE comID = :id")
    void updateComposition(int compositionId, String id);

    /**
     * Updating only price
     * By order id
     */
    @Query("UPDATE Composition SET compositionId=:compositionId, layoutType=:layoutType WHERE comID = :id")
    void updateComposition(int compositionId, String id, String layoutType);

    @Update
    void update( Composition composition);

    @Delete
    void delete( Composition composition);
}
