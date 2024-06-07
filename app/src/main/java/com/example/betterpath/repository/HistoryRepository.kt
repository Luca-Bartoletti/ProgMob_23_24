package com.example.betterpath.repository

import com.example.betterpath.data.PathHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

class HistoryRepository {

    fun getSamplePath() : List<PathHistory>{
        return listOf(
            PathHistory(0, Random.nextInt(10), extractDate(Date()),"sample"),
            PathHistory(1, Random.nextInt(10), extractDate(Date()),"sample"),
            PathHistory(2, Random.nextInt(10), extractDate(Date()),"sample"),
            PathHistory(3, Random.nextInt(10), extractDate(Date()),"sample"),
            PathHistory(4, Random.nextInt(10), extractDate(Date()),"sample"),
            PathHistory(5, Random.nextInt(10), extractDate(Date()),"sample"),
            PathHistory(6, Random.nextInt(10), extractDate(Date()),"sample"),
            PathHistory(7, Random.nextInt(10), extractDate(Date()),"sample"),
            PathHistory(8, Random.nextInt(10), extractDate(Date()),"sample"),
        )
    }

    fun extractDate(date: Date?): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault(Locale.Category.FORMAT))
        return dateFormat.format(date)
    }
}