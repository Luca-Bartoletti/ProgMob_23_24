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
    val repository : HistoryRepository = HistoryRepository(this, pathHistoryDao!!)

    //tengo in memoria i percorsi salvati fino ad ora
    var pathHistory = repository.allPaths
        private set

    //varibili per il fetch delle informazioni richieste nella pagina di confronto
    var selectedPathInfo1 = repository.selectedPathInfo1
    var selectedPathInfo2 = repository.selectedPathInfo2

    // identificativo della giornata odierna del db Room
    var todayId = repository.todayID
    // booleno per controllare se todayPathId sia valido o meno
    val isFetchedID = repository.fetchedID

    // tengo traccia delle date di interesse e edel numero di date selezionate
    var checkedBox = mutableStateOf(arrayOf(-1,-1))
        private set
    var numberOfChecked = mutableIntStateOf(0)

    //variabile per abilitare o meno la prosecuzione alla pagina di comparazione
    var enableCompareButton = mutableStateOf(false)
        private set

    init {
        repository.init()
    }

    /**
     * Riporta i valori di conrollo della checkBox allo stato di partenza in cui nessun valore é
     * selezionato
     * */
    fun resetCheckBoxValue(){
        checkedBox.value = arrayOf(-1,-1)
        numberOfChecked.intValue = 0
        enableCompareButton.value = false
    }


    /**
     * Gestisce lo stato dell'array di valori delle checkbox e aggiorna le variabili in accordo alle
     * modifiche richieste (quando esse sono valide)
     * @param index l'identificativo associato alla checkbox che si vuole aggiornare
     * @param value valore booleano che indica lo stato della checkBox (true se selezionato,
     *              false altrimenti)
     * @return booleano per indicare se la casella deve risultare spuntata o meno
     * */
    fun updateCheckedBox(index: Int, value: Boolean): Boolean {
        //inizializzo il valore di ritorno
        var result = false

        // se ho segnato meno di due valori posso aggiungere l'id passato a quelli selezionati
        if (value && numberOfChecked.intValue < 2) {
            result = true
            checkedBox.value[numberOfChecked.intValue] = index
            numberOfChecked.intValue++
        }

        // se ho un solo valore e voglio toglierlo dalla lista aggiorno senza problemi
        if (!value && numberOfChecked.intValue == 1) {
            checkedBox.value[0] = -1
            numberOfChecked.intValue--
        }
        // se ho due valori nella lista cerco quello che voglio togliere e compatto
        // il valore residuo a sinistra
        if (!value && numberOfChecked.intValue == 2) {
            if (checkedBox.value[1] == index) {
                checkedBox.value[1] = -1
            } else {
                checkedBox.value[0] = checkedBox.value[1]
                checkedBox.value[1] = -1
            }
            numberOfChecked.intValue--
        }

        // se ho due valori segnati posso procedere all pagina di confronto, segno a true il flag
        // altrimenti disabiolito l'operazione segnado il flag a false
        if (numberOfChecked.intValue == 2)
            enableCompareButton.value = true
        else
            enableCompareButton.value = false

        return result
    }

    /**
     * Richiama il repository per ottenre le informazioni sul percoso con id indicato
     * @param id il valore dell'id del PathHistory di interesse
     * @param pathNumber indica dove si vuole salvare il PathHistory nle repository (0 nel percorso
     *      odierno, 1 in selectedPathInfo1, 2 in selectedPathInfo2)
     * */
    private fun fetchPathById(id: Int, pathNumber: Int = 0){
        repository.getSelectedPath(id, pathNumber)
    }

    /**
     * Verifica che sia stato selezionato un id valido e richiede al repository, tramite
     * fetchPathById, di salvarlo in selectedPathInfo1
     * @see fetchPathById
     * */
    fun fetchFirstPath() {
        if (checkedBox.value[0] != -1)
            fetchPathById(checkedBox.value[0], 1)

    }

    /**
     * Verifica che sia stato selezionato un id valido e richiede al repository, tramite
     * fetchPathById, di salvarlo in selectedPathInfo2
     * @see fetchPathById
     * */
    fun fetchSecondPath() {
        if (checkedBox.value[1] != -1)
            fetchPathById(checkedBox.value[1], 2)
    }

    /**
     * Ritorna il valore dell'id odierno
     * @return todayID : Int
     * */
    fun getTodayId(): Int {
        return repository.todayID.value
    }

    /**
     * Data una lista di PathData viene calcolato in maniera asincrona la distanza tra tutti i punti
     * per poi richiedere al repository un aggiornamento del valore odierno con quello calcolato.
     * @param list List<PathData?>
     * @see com.example.betterpath.repository.HistoryRepository.updateDistance
     * */
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