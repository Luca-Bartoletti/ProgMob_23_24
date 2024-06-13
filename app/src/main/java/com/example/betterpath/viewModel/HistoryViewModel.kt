package com.example.betterpath.viewModel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterpath.data.PathHistory
import com.example.betterpath.database.MyAppDatabase
import com.example.betterpath.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.random.Random

class HistoryViewModel(database: MyAppDatabase) : ViewModel() {
    //modifiche per Room
    private var pathHistorydao = database.pathHistoryDao()
    private val repository : HistoryRepository = HistoryRepository(this, pathHistorydao!!)
    var pathHistory = repository.allPaths
        private set
    var selectedPath1 = repository.selectedPath1
    var selectedPath2 = repository.selectedPath2

    fun fetchPathById(id: Int){
        repository.getSelectedPath(id)
    }

    fun addPathSample() {
        repository.addPath(
            PathHistory(
                distance = 0,
                date = LocalDate.now().plusDays(Random.nextInt(10).toLong()).toString(),
                pathInfo = "sampleTest"
            )
        )
    }



    var checkedBox = mutableStateOf(arrayOf(-1,-1))
        private set
    private var numberOfChecked = mutableIntStateOf(0)
    var enableCompareButton = mutableStateOf(false)
        private set

    private val historyRepository : HistoryRepository = HistoryRepository(this, pathHistorydao!!)
    var historyItem = MutableStateFlow<List<PathHistory>>(emptyList())
        private set

    init {
        repository.init()
    }

    //logica CheckBox
    fun resetCheckBoxValue(){
        checkedBox.value = arrayOf(-1,-1)
        numberOfChecked.intValue = 0
        enableCompareButton.value = false
    }

    fun updateCheckedBox(index: Int, value: Boolean) : Boolean {
        var result = false
        println(value)
        if(value && numberOfChecked.intValue < 2) {
            result = true
            checkedBox.value[numberOfChecked.intValue] = index
            numberOfChecked.intValue++
            println(""+checkedBox.value[0] +" "+ checkedBox.value[1])
        }
        if (!value && numberOfChecked.intValue == 1){
           //result = false
            checkedBox.value[0] = -1
            numberOfChecked.intValue--
            println(""+checkedBox.value[0] +" "+ checkedBox.value[1])
        }
        if (!value && numberOfChecked.intValue == 2){
            //result = false
            if(checkedBox.value[1] == index) {
                checkedBox.value[1] = -1
            } else{
                checkedBox.value[0] = checkedBox.value[1]
                checkedBox.value[1] = -1
            }
            numberOfChecked.intValue--
            println(""+checkedBox.value[0] +" "+ checkedBox.value[1])
        }

        if(numberOfChecked.intValue == 2)
            enableCompareButton.value = true
        else
            enableCompareButton.value = false

        return result
    }

    fun fetchFirstPath() {
        if (checkedBox.value[0] != -1)
            repository.getSelectedPath(checkedBox.value[0], 1)
    }

    fun fetchSecondPath() {
        if (checkedBox.value[1] != -1)
            repository.getSelectedPath(checkedBox.value[1], 2)
    }

}