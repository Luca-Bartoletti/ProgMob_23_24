package com.example.betterpath.viewModel

import android.location.Location
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.betterpath.data.PathData
import com.example.betterpath.data.PathHistory
import com.example.betterpath.database.MyAppDatabase
import com.example.betterpath.repository.HistoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.random.Random

class HistoryViewModel(database: MyAppDatabase) : ViewModel() {
    //modifiche per Room
    private var pathHistoryDao = database.pathHistoryDao()
    private val repository : HistoryRepository = HistoryRepository(this, pathHistoryDao!!)
    var pathHistory = repository.allPaths
        private set
    var selectedPathInfo1 = repository.selectedPathInfo1
    var selectedPathInfo2 = repository.selectedPathInfo2
    var todayId = repository.todayID
    val isFetchedID = repository.fetchedID

    var checkedBox = mutableStateOf(arrayOf(-1,-1))
    private set
    private var numberOfChecked = mutableIntStateOf(0)
    var enableCompareButton = mutableStateOf(false)
        private set

    fun fetchPathById(id: Int){
        repository.getSelectedPath(id)
    }

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
        if(value && numberOfChecked.intValue < 2) {
            result = true
            checkedBox.value[numberOfChecked.intValue] = index
            numberOfChecked.intValue++
        }
        if (!value && numberOfChecked.intValue == 1){
            checkedBox.value[0] = -1
            numberOfChecked.intValue--
        }
        if (!value && numberOfChecked.intValue == 2){
            if(checkedBox.value[1] == index) {
                checkedBox.value[1] = -1
            } else{
                checkedBox.value[0] = checkedBox.value[1]
                checkedBox.value[1] = -1
            }
            numberOfChecked.intValue--
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

    fun getTodayId(): Int {
        return repository.todayID.value
    }

    fun updateDistance(list : List<PathData?>){
        viewModelScope.launch {
            withContext(Dispatchers.Default){
                var totalDistance = 0f
                for (i in 0 until list.size - 1) {
                    val startPoint = list[i]
                    val endPoint = list[i + 1]

                    val results = FloatArray(1)
                    Location.distanceBetween(
                        startPoint!!.lat, startPoint.lng,
                        endPoint!!.lat, endPoint.lng,
                        results
                    )
                    totalDistance += results[0]
                }
                repository.updateDistance(totalDistance.toInt())
            }
        }
    }

}