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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.viewModel.HistoryViewModel

@Composable
fun HistoryScreen(navController: NavController? = null, viewModel: HistoryViewModel? = null) {

    Scaffold(
        topBar = { Header() },
        bottomBar = { Footer(navController = navController, homeButton = true, compareButton = true, viewModel = viewModel) },

        ) { innerPadding ->
        HistoryContent(innerPadding, viewModel)
    }

}
@Composable
fun HistoryContent(innerPadding: PaddingValues, viewModel: HistoryViewModel?){
    val pathInfo = viewModel?.historyItem?.collectAsState()
    LazyColumn(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(pathInfo?.value?.size ?: 0){ index ->
            val path = pathInfo?.value?.get(index)
            InfoRow(id = path!!.id, viewModel = viewModel, distance = path.distance, data = path.date, pathInfo = path.pathInfo)
        }
    }
}


@Composable
fun InfoRow(id:Int, viewModel: HistoryViewModel?, distance: Int, data: String, pathInfo: String){
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
            Text(text = stringResource(R.string.path_info_date) + " : $data")
            Text(text = stringResource(R.string.path_info_distance) + " : $distance km")
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
                Text(text = pathInfo)
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
            Checkbox(
                checked = isChecked.value,
                onCheckedChange = {
                    isChecked.value = viewModel?.updateCheckedBox(id, it) ?: false
                })
        }
    }

}

@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen()
}