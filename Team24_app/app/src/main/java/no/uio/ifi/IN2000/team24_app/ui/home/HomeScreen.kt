package no.uio.ifi.IN2000.team24_app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    navController: NavController
){
    Column {
        Text(text="AYO")
    }
}