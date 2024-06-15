package com.example.betterpath.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class PathData (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lat: Double,
    val lng :Double,
    val time : Long,
    val pathHistoryId : Int
)