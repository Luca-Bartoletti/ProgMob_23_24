package com.example.betterpath.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PathData (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val lat: Double,
    val lng :Double,
    val time : Long,
    var startStop : Int,
    val pathHistoryId : Int
)