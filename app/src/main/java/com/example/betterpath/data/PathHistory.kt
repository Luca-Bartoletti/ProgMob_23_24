package com.example.betterpath.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PathHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val distance: Int,
    val date: String,
    val pathInfo: String
)
