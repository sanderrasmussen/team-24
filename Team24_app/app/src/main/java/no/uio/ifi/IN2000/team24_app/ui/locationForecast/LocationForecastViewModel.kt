package no.uio.ifi.IN2000.team24_app.ui.locationForecast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import no.uio.ifi.IN2000.team24_app.data.locationForecast.*


data class LocationUIState(val locationInfo: List <LocationForecast>)
class LocationForecastViewModel : ViewModel() {

    //val locationForecastRepository : Repository = null

    var locationUIState  by mutableStateOf(LocationUIState(listOf()))


}