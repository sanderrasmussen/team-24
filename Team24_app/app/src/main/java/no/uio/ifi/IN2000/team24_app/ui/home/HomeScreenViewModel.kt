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

class HomeScreenViewModel(
    private val TAG:String = "HomeScreenViewModel",
    //this state and the calling function are more just to check that the LocationTracker functions, cant test in junit as it needs the actual application
    private val _weatherState: MutableStateFlow<LocationForecast>,
    var weatherState:StateFlow<LocationForecast> = _weatherState.asStateFlow()

): ViewModel(){

     fun getWeather(context:Context){
         viewModelScope.launch(Dispatchers.IO){
            val pos = LocationTracker( context).getLocation()

             val weather : LocationForecast? = LocationForecastDatasource().getLocationForecastData(pos?.latitude ?:59.913868, pos?.longitude ?:10.752245)  //default to oslo S for now if pos is null
             Log.d(TAG, weather.toString())
             println(weather)
         }
    }
}