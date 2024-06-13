package com.example.betterpath.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface PathHistoryDao {

    @Insert
    suspend fun insert(pathHistory: PathHistory)

    @Insert
    suspend fun insertAll(list: List<PathHistory>)

    @Query("SELECT * FROM PathHistory ORDER BY date DESC")
    fun getAllPath(): Flow<List<PathHistory>>

    @Query("SELECT * FROM PathHistory WHERE id = :id")
    fun getPathById(id: Int): Flow<PathHistory?>
}
