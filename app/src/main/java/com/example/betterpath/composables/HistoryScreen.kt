package com.example.betterpath.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HistoryScreen(navController: NavController? = null) {

    Scaffold(
        topBar = { Header() },
        bottomBar = { Footer(navController = navController, historyButton = false) },

        ) { innerPadding ->
        HistoryContent(innerPadding)
    }

}
@Composable
fun HistoryContent(innerPadding: PaddingValues){
    Column(
        modifier = Modifier
            .selectableGroup()
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val checkedBox = remember {
            mutableStateOf(arrayOf(-1,-1))
        }
        val numberOfChecked = remember {
            mutableIntStateOf(0)
        }
        for(i in 0..3)
            InfoRow(i,numberOfChecked, checkedBox)

    }
}


@Composable
fun InfoRow(id:Int, numberOfChecked: MutableState<Int>
            , checkedBox: MutableState<Array<Int>>){
    Row (
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(1.dp, MaterialTheme.colorScheme.onSurface),
        horizontalArrangement = Arrangement.Center,
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.5f)
                .padding(vertical = 4.dp)
                .padding(start = 8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            Text(text = "DATA : xx/xx/xxxx")
            Text(text = "DISTANZA : X Km")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.4f)
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Text(text = "MAPPA")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(0.1f)
                .padding(vertical = 4.dp)
        ) {
            val isChecked = remember {
                mutableStateOf(false)
            }
            /**
             * Blocco di codice che verifica se sia possibile selezionare l'elemento della lista
             * aggiornando i valori di numberOfChecked e checkedBox in base al valore di isChecked.
             * TODO: spostare nel model(?)
             */
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = {
                    println(it)
                    if(it && numberOfChecked.value < 2) {
                        isChecked.value = true
                        checkedBox.value[numberOfChecked.value] = id
                        numberOfChecked.value++
                        println(""+checkedBox.value[0] +" "+ checkedBox.value[1])
                    }
                    if (!it && numberOfChecked.value == 1){
                        isChecked.value = false
                        checkedBox.value[0] = -1
                        numberOfChecked.value--
                        println(""+checkedBox.value[0] +" "+ checkedBox.value[1])
                    }
                    if (!it && numberOfChecked.value == 2){
                        isChecked.value = false
                        if(checkedBox.value[1] == id) {
                            checkedBox.value[1] = -1
                        } else{
                            checkedBox.value[0] = checkedBox.value[1]
                            checkedBox.value[1] = -1
                        }
                        numberOfChecked.value--
                        println(""+checkedBox.value[0] +" "+ checkedBox.value[1])
                    }
                })
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen()
}