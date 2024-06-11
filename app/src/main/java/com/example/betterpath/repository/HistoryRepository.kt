package com.example.betterpath.repository

import com.example.betterpath.data.PathHistory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HistoryRepository {

    fun getSamplePath() : List<PathHistory>{
        return listOf(
            PathHistory(0, 1, extractDate(Date()),"sample"),
            PathHistory(1,2, extractDate(Date()),"sample"),
            PathHistory(2, 3, extractDate(Date()),"sample"),
            PathHistory(3,4, extractDate(Date()),"sample"),
            PathHistory(4, 5, extractDate(Date()),"sample"),
            PathHistory(5, 6, extractDate(Date()),"sample"),
            PathHistory(6, 7, extractDate(Date()),"sample"),
            PathHistory(7, 8, extractDate(Date()),"sample"),
            PathHistory(8, 9, extractDate(Date()),"sample"),
        )
    }

    private fun extractDate(date: Date?): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault(Locale.Category.FORMAT))
        return dateFormat.format(date!!)
    }
}