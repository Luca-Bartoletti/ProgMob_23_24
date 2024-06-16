package com.example.betterpath.repository

import androidx.lifecycle.viewModelScope
import com.example.betterpath.data.PathHistory
import com.example.betterpath.data.PathHistoryDao
import com.example.betterpath.viewModel.HistoryViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate

class HistoryRepository(private val historyViewModel: HistoryViewModel, private val dao : PathHistoryDao) {
    var allPaths = dao.getAllPath()
    private val todayPath : MutableStateFlow<PathHistory?> = MutableStateFlow(null)
    val selectedPath1 : MutableStateFlow<PathHistory?> = MutableStateFlow(null)
    val selectedPath2 : MutableStateFlow<PathHistory?> = MutableStateFlow(null)
    val todayID : MutableStateFlow<Int> = MutableStateFlow(-1)


    fun init(){
        val today = LocalDate.now().toString()
        historyViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                todayID.value = dao.getPathIdFromDate(today)
                todayPath.value = dao.getPathFromDate(today)
                println("todayPath =  $todayPath -- todayPath.value = ${todayPath.value}")
                if (todayPath.value == null) {
                    dao.insert(
                        PathHistory(
                            distance = 0,
                            date = today
                        )
                    )
                }
            }


        }
    }

    fun getSelectedPath(id: Int, pathNumber: Int = 0) {
        historyViewModel.viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val pathFlow = dao.getPathById(id)
                pathFlow.collect { path ->
                    when(pathNumber){
                        0 -> todayPath.value = path
                        1 -> selectedPath1.value = path
                        2 -> selectedPath2.value = path
                    }
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


