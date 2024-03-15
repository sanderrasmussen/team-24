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

    //this state and the calling function are more just to check that the LocationTracker functions, cant test in junit as it needs the actual application
    private val _weatherState: MutableStateFlow<WeatherDetails?> = MutableStateFlow(WeatherDetails()),
    var weatherState:StateFlow<WeatherDetails?> = _weatherState.asStateFlow()

): ViewModel(){

     fun getCurrentWeather(context:Context){
         if(_weatherState.value == null){
             viewModelScope.launch(Dispatchers.IO) {
                 val position = LocationTracker(context).getLocation()
                 Log.d(TAG, position.toString())
                 locationForecastRepo.fetchLocationForecast(
                     position?.latitude ?: 59.913868,
                     position?.longitude ?: 10.752245
                 )  //default to oslo S for now if pos is null
                 val weather: WeatherDetails? = locationForecastRepo.getWeatherNow()
                 Log.d(TAG, weather.toString())
                 _weatherState.update { weather }
            }
         }else{
             Log.d(TAG, "already got weather state")
         }
    }
}