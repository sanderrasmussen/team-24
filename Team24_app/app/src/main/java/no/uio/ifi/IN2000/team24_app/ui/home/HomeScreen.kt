package no.uio.ifi.IN2000.team24_app.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    LocationPermissionCard()
    viewModel.getCurrentWeather(LocalContext.current)   //this line needs to be here

    val weatherState : ArrayList<WeatherDetails>? by viewModel.weatherState.collectAsState()
    Column {
        Text(text = weatherState?.get(0)?.time ?:"loading")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionCard(){
    val locationPermissionState = rememberPermissionState(permission = android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val showCard = remember{ mutableStateOf(!locationPermissionState.status.isGranted)}

    fun permissionDenied() {
        showCard.value=false
    }

    if(showCard.value){
        AlertDialog(
            title= { Text(text = "Requires location permission") },
            icon = {Icons.Default.LocationOn},
            onDismissRequest = { permissionDenied() },
            confirmButton = {
                Button(onClick = {
                    locationPermissionState.launchPermissionRequest()
                    showCard.value=false
                }) {
                    Text(text = "grant location permission")
                }
            },
            dismissButton = {
                Button(onClick = { permissionDenied()}) {
                    Text(text = "Refuse location permissions")
                }
            }
        )
    }

}
