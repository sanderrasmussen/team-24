package no.uio.ifi.IN2000.team24_app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeScreenViewModel = viewModel(),
    navController: NavController
){
    val locationPermissionState = rememberPermissionState(permission = android.Manifest.permission.ACCESS_COARSE_LOCATION)
    if(!locationPermissionState.status.isGranted){
        AlertDialog(
            title= { Text(text = "Requires location permission") },
            onDismissRequest = { permissionDenied() },
            confirmButton = {
                Button(onClick = { locationPermissionState.launchPermissionRequest()}) {
                    Text(text = "grant location permission")
                }
            },
            dismissButton = {
                Button(onClick = { permissionDenied() }) {
                    Text(text = "Refuse location permissions")
                }
            }
        )
    }
    viewModel.getCurrentWeather(LocalContext.current)   //this line needs to be here

    val weatherState : ArrayList<WeatherDetails>? by viewModel.weatherState.collectAsState()
    Column {
        Text(text = weatherState?.get(0)?.time ?:"loading")
    }
}

fun permissionDenied(){

}