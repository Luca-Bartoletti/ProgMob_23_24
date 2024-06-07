package com.example.betterpath.viewModel

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.betterpath.data.PathHistory
import com.example.betterpath.repository.HistoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class HistoryViewModel : ViewModel() {
    private var checkedBox = mutableStateOf(arrayOf(-1,-1))
    private var numberOfChecked = mutableIntStateOf(0)
    var enableCompareButton = mutableStateOf(false)
        private set

    val historyRepository : HistoryRepository = HistoryRepository()
    var historyItem = MutableStateFlow<List<PathHistory>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            historyItem.value = historyRepository.getSamplePath()
        }
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

}

/*
 var celestialBodies = MutableStateFlow<List<CelestialBody>>(emptyList())
        private set
    val selectedCelestialBody = MutableStateFlow<CelestialBody?>(null)

    val repository: SpaceRepository = SpaceRepository()

    init {
        viewModelScope.launch {
            celestialBodies.value = repository.getCelestialBodies()
        }
    }


    fun fetchCelestialBodyById(id: Int) = viewModelScope.launch {
        delay(1000)

        val body = celestialBodies.value.find { it.id == id }

        selectedCelestialBody.value = body
    }
 */