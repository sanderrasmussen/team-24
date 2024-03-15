package no.uio.ifi.IN2000.team24_app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails


@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    navController: NavController
){
    viewModel.getCurrentWeather(LocalContext.current)

    val testState : WeatherDetails? by viewModel.weatherState.collectAsState()
    Column {
        Text(text = testState?.time ?:"loading")
    }
}