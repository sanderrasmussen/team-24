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
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.heads
import no.uio.ifi.IN2000.team24_app.data.character.legs
import no.uio.ifi.IN2000.team24_app.data.character.torsos
import no.uio.ifi.IN2000.team24_app.data.location.LocationTracker
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecast
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastDatasource
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastRepository
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import kotlin.reflect.typeOf

class HomeScreenViewModel(
    private val TAG:String = "HomeScreenViewModel",
    private val locationForecastRepo : LocationForecastRepository = LocationForecastRepository(),
    //private val metAlertsRepo: MetalertsRepo = MetalertsRepo(),
    private var _userLocation : Location? = null,
    //private var _alerts = MutableStateFlow<>

): ViewModel(){
    var currentWeatherState:StateFlow<ArrayList<WeatherDetails>?> =
        locationForecastRepo.ObserveTodayWeather();
    val next6DaysState: StateFlow<ArrayList<WeatherDetails?>?> =
        locationForecastRepo.ObserveNext6DaysForecast()

    //TODO character should be stored in viewmodel, and needs the current temp (from currentWeatherState)
    //this is just to render a default character, TODO should call a load from disk()-method on create
    private val character = Character(head = heads().first(), torso = torsos().first(), legs = legs().first())
    val characterState = MutableStateFlow(character)
    fun getCurrentWeather(context:Context){

             viewModelScope.launch(Dispatchers.IO) {
                 //!position broke, todo look into LocationTracker
                 if(_userLocation ==null) {
                     Log.d(TAG, "context in viewModel: $context")
                     Log.d(TAG, "typeof context in viewModel: ${context.javaClass}")

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
