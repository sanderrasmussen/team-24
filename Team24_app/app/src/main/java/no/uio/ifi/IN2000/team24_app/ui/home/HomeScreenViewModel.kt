package no.uio.ifi.IN2000.team24_app.ui.home

import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.data.location.LocationTracker
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecast
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastDatasource
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastRepository
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails

class HomeScreenViewModel(
    private val TAG:String = "HomeScreenViewModel",
    private val locationForecastRepo : LocationForecastRepository = LocationForecastRepository(),
    //private val metAlertsRepo: MetalertsRepo = MetalertsRepo(),
    private var _userLocation : Location? = null,

): ViewModel(){
    var weatherState:StateFlow<ArrayList<WeatherDetails>?> =
        locationForecastRepo.ObserveTodayWeather();

    fun getCurrentWeather(context:Context){

             viewModelScope.launch(Dispatchers.IO) {
                 //!position broke, todo look into LocationTracker
                 if(_userLocation ==null) {
                     _userLocation = LocationTracker(context).getLocation()
                 }
                 Log.d(TAG, "Position: ${_userLocation.toString()}")
                 locationForecastRepo.fetchLocationForecast(
                     _userLocation?.latitude ?: 59.913868,
                     _userLocation?.longitude ?: 10.752245
                 )

            }
     }

    fun getRelevantAlerts(context: Context){
        viewModelScope.launch(Dispatchers.IO) {
            //!position broke, todo look into LocationTracker
            if (_userLocation == null) {
                _userLocation = LocationTracker(context).getLocation()
            }
            //metAlertsRepo.getRelevantAlerts(_userLocation)
        }
    }
}
