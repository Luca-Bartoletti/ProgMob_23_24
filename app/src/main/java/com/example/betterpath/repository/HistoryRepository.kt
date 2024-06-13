package com.example.betterpath.repository

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.betterpath.data.PathHistory
import com.example.betterpath.data.PathHistoryDao
import com.example.betterpath.viewModel.HistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.Locale
import java.util.concurrent.Executor

class HistoryRepository(val historyViewModel: HistoryViewModel, val dao : PathHistoryDao) {
    var allPaths = dao.getAllPath()
    val selectedPath : MutableStateFlow<PathHistory?> = MutableStateFlow(null)
    val selectedPath1 : MutableStateFlow<PathHistory?> = MutableStateFlow(null)
    val selectedPath2 : MutableStateFlow<PathHistory?> = MutableStateFlow(null)

    fun init(){
//        historyViewModel.viewModelScope.launch {
//            withContext(Dispatchers.IO){
//                val list = listOf(
//                    PathHistory(
//                        distance = 0,
//                        date = LocalDate.now().toString(),
//                        pathInfo = "sample"),
//                    PathHistory(
//                        distance = 10,
//                        date = LocalDate.now().plusDays(1).toString(),
//                        pathInfo = "sample"),
//                    PathHistory(
//                        distance = 4,
//                        date = LocalDate.now().plusDays(2).toString(),
//                        pathInfo = "sample"),
//                )
//
//                try {
//                    dao.insertAll(list)
//                } catch (e: Exception){
//                    // Handle the exception
//                    Log.e("ww", "Error inserting data: ${e.message}")
//                }
//            }
//        }
    }

    fun getSelectedPath(id: Int, pathNumber: Int = 0) {
        historyViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val pathFlow = dao.getPathById(id)
                pathFlow.collect { path ->
                    when(pathNumber){
                        0 -> selectedPath.value = path
                        1 -> selectedPath1.value = path
                        2 -> selectedPath2.value = path
                    }
                    Log.i("TG", "selectedPath updated: $selectedPath")
                }
            }
        }
    }

    fun addPath(path: PathHistory) {
        historyViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO){
                dao.insert(path)
            }
        }
    }

}