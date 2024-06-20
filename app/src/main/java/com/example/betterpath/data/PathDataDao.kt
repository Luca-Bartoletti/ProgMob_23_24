package com.example.betterpath.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PathDataDao {
    //26
    @Insert
    suspend fun insert(pathData: PathData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<PathData>)

    @Query("SELECT * FROM PathData WHERE pathHistoryId = :pathId ORDER BY time")
    fun getAllPathWithHistoryId(pathId :Int) : List<PathData?>

}