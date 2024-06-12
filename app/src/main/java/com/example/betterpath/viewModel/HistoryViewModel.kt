package com.example.betterpath.viewModel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterpath.data.PathHistory
import com.example.betterpath.repository.HistoryRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    var checkedBox = mutableStateOf(arrayOf(-1,-1))
        private set
    private var numberOfChecked = mutableIntStateOf(0)
    var enableCompareButton = mutableStateOf(false)
        private set

    private val historyRepository : HistoryRepository = HistoryRepository()
    var historyItem = MutableStateFlow<List<PathHistory>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            delay(2000)
            historyItem.value = historyRepository.getSamplePath()
        }
    }

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

    fun fetchFirstPath() : PathHistory?{
        println(checkedBox.value[0])
        return if (checkedBox.value[0] != -1) historyItem.value[checkedBox.value[0]]
        else null
    }

    fun fetchSecondPath() : PathHistory?{
        println(checkedBox.value[1])
        return if (checkedBox.value[1] != -1) historyItem.value[checkedBox.value[1]]
        else null
    }

}