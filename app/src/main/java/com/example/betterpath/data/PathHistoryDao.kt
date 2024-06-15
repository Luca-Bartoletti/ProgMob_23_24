package com.example.betterpath.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PathHistoryDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(pathHistory: PathHistory)

    @Insert
    suspend fun insertAll(list: List<PathHistory>)

    @Query("SELECT * FROM PathHistory ORDER BY date DESC")
    fun getAllPath(): Flow<List<PathHistory>>

    @Query("SELECT * FROM PathHistory WHERE id = :id")
    fun getPathById(id: Int): Flow<PathHistory?>

    @Query("SELECT id FROM PathHistory WHERE date = :date")
    fun getPathIdFromDate(date : String): Int
}
