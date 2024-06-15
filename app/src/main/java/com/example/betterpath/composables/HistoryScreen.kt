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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.betterpath.R
import com.example.betterpath.viewModel.HistoryViewModel
import com.example.betterpath.viewModel.LoginViewModel

@Composable
fun HistoryScreen(navController: NavController, viewModel: HistoryViewModel, loginViewModel: LoginViewModel) {

    if (navController.currentBackStackEntry?.destination?.route == "historyScreen")
        viewModel.resetCheckBoxValue()

    ScreenWithMenu(content = {
        Scaffold(
            topBar = { Header(navController = navController, loginViewModel = loginViewModel) },
            bottomBar = {
                Footer(
                    navController = navController,
                    homeButton = true,
                    compareButton = true,
                    historyViewModel = viewModel
                )
            },
            //todo rimuovere FAB post test
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.addPathSample() },
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }

        ) { innerPadding -> HistoryContent(innerPadding, viewModel) }
    }, navController = navController, loginViewModel = loginViewModel)
}
@Composable
fun HistoryContent(innerPadding: PaddingValues, viewModel: HistoryViewModel) {
    val allPath = viewModel.pathHistory.collectAsState(null)
    LazyColumn(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(innerPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        allPath.value?.let {
            items(allPath.value!!.size) { index ->
                val path = allPath.value!![index]
                InfoRow(
                    id = path.id,
                    viewModel = viewModel,
                    distance = path.distance,
                    data = path.date
                )
            }
        }
    }
}



@Composable
fun InfoRow(id:Int, viewModel: HistoryViewModel?, distance: Int, data: String){
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
                .weight(0.8f)
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
                .weight(0.2f)
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

//@Preview(showBackground = true)
//@Composable
//fun HistoryScreenPreview() {
//    val context = LocalContext.current
//    HistoryScreen(
//        navController = NavController(context),
//        loginViewModel = LoginViewModel(PreferenceRepository(context)),
//        viewModel = HistoryViewModel()
//    )
//}