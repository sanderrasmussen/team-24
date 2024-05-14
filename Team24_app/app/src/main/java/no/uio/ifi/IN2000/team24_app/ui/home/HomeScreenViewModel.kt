package no.uio.ifi.IN2000.team24_app.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.R

import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository

import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.getDefaultBackupCharacter
import no.uio.ifi.IN2000.team24_app.data.character.heads
import no.uio.ifi.IN2000.team24_app.data.character.legs
import no.uio.ifi.IN2000.team24_app.data.character.loadSelectedClothes
import no.uio.ifi.IN2000.team24_app.data.character.torsos
import no.uio.ifi.IN2000.team24_app.data.location.LocationTracker
import no.uio.ifi.IN2000.team24_app.data.locationForecast.ApiAccessException
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecast
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastDatasource
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastRepository
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Point
import no.uio.ifi.IN2000.team24_app.data.metAlerts.VarselKort
import no.uio.ifi.IN2000.team24_app.data.metAlerts.metAlertsRepository.MetAlertsRepo
import no.uio.ifi.IN2000.team24_app.ui.getNextSixDays
import kotlin.math.abs
import kotlin.reflect.typeOf

data class AlertsUiState(
    val alerts: List<VarselKort> = emptyList(),
    val show:Boolean = false
)
data class WeatherDetailsUiState(
    var weatherDetails: List<WeatherDetails>? = null,
    val dayStr : String? = null
)
data class SatisfactionUiState(
    val fillPercent: Float = 0.0f,
    val color : Color = Color.Green,
    val unsatisfiedIcon: Int = R.drawable.too_cold //TODO this should be a custom icon
    )


@RequiresApi(Build.VERSION_CODES.O)
class HomeScreenViewModel(
    private val TAG:String = "HomeScreenViewModel",
    private val locationForecastRepo : LocationForecastRepository = LocationForecastRepository(),
    private val metAlertsRepo: MetAlertsRepo = MetAlertsRepo(),
    private val bankRepo : BankRepository = BankRepository(),

    private var _userLocation : MutableStateFlow<Location?> = MutableStateFlow(null),
    val userLocation : StateFlow<Location?> = _userLocation.asStateFlow(),
    private var _alerts : MutableStateFlow<AlertsUiState> = MutableStateFlow(AlertsUiState()),
    val alerts : StateFlow<AlertsUiState> = _alerts.asStateFlow(),
    private var _satisfaction : MutableStateFlow<SatisfactionUiState> = MutableStateFlow(SatisfactionUiState()),
    val satisfaction : StateFlow<SatisfactionUiState> = _satisfaction.asStateFlow(),
    private var _balance: MutableStateFlow<Int?> = MutableStateFlow(0),
    val balance: StateFlow<Int?> = _balance.asStateFlow(),


    private val _weatherDetails : MutableStateFlow<WeatherDetailsUiState> = MutableStateFlow(WeatherDetailsUiState()),
    val weatherDetails : StateFlow<WeatherDetailsUiState> = _weatherDetails.asStateFlow(),
    private val _currentWeatherState: MutableStateFlow<ArrayList<WeatherDetails>> = MutableStateFlow(ArrayList()),
    val currentWeatherState: StateFlow<ArrayList<WeatherDetails>> = _currentWeatherState.asStateFlow(),
    private val _next6DaysState :MutableStateFlow<ArrayList<WeatherDetails?>?> = MutableStateFlow(ArrayList()),
    val next6DaysState: StateFlow<ArrayList<WeatherDetails?>?> = _next6DaysState.asStateFlow()

): ViewModel() {




         //this is now default value in case of failed load form disk
    val characterState = MutableStateFlow(loadClothesFromDisk())
    private var character = characterState.asStateFlow()


    init {
        //I will now laod selected clothes from disk
        viewModelScope.launch {
            characterState.update { loadSelectedClothes() }
        }
        getBalanceFromDb()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun updateWeatherDetails(weatherDetails: WeatherDetails?, dayStr: String? = null) {
        _weatherDetails.update {
            if (weatherDetails == null) {
                return@update WeatherDetailsUiState(null, null)
            } else if (dayStr == null) {
                return@update WeatherDetailsUiState(listOf(weatherDetails), null)
            } else {
                val hours: MutableList<WeatherDetails> = mutableListOf()
                val days = getNextSixDays()

                val allDetails =
                    locationForecastRepo.getNext7DaysForecast()    //todo maybe change this to recieve//collect the state
                days.forEachIndexed { index, day ->
                    if (dayStr == day) {
                        allDetails[index]?.let { detailsForDay -> hours.addAll(detailsForDay) }
                    }
                }
                return@update WeatherDetailsUiState(hours, dayStr)
            }
        }
    }

    fun showAlerts(show:Boolean){
        _alerts.update {
            AlertsUiState(alerts = _alerts.value.alerts, show = show)
        }
    }


    fun loadClothesFromDisk(): Character {
        var character = getDefaultBackupCharacter()
        viewModelScope.launch {
            character = loadSelectedClothes()

        }
        updateSatisfaction(characterTemp = character.findAppropriateTemp())

        return character
    }

    fun getBalanceFromDb() {

        viewModelScope.launch {
            _balance.update {

                bankRepo.getBankBalance()
            }
        }
    }


     fun updateSatisfaction(characterTemp: Double, actualTemp: Double = (_currentWeatherState.value.firstOrNull()?.air_temperature ?:0.0)) {

        var newFillPercent = 0.0f
        var newColor = Color.Green
        var newIcon = R.drawable.too_cold


        Log.d("updateSatisfaction", "Actual Temp: $actualTemp, Character Temp: $characterTemp")
        val delta = abs(actualTemp - characterTemp)
        Log.d("updateSatisfaction", "Delta: $delta")

        //FILL%
        newFillPercent = maxOf((1 - (delta / 10)).toFloat(), 0.01f)
        Log.d("updateSatisfaction", "Satisfaction%: $newFillPercent")

        //ICON
        newIcon = if (delta > 0) {
            Log.d("updateSatisfaction", "Too hot")
            R.drawable.too_hot
        } else {
            Log.d(TAG, "Too cold")
            R.drawable.too_cold
        }

        //COLOR
        /*
        fill should be hex calculated as `(1-progress) * red`, and `progress * green` (0 blue)
        //personally, i like this one. it is a bit more dynamic than the other one, and the line is so cool :)!
        But alas, testing with users says boring old pokemon-style is better.
         */
        //newColor = Color(((1-newFillPercent) * 255).toInt(), (newFillPercent * 255).toInt(), 0)
        newColor = when {
            newFillPercent < 0.33 -> Color.Red
            newFillPercent < 0.66 -> Color(0xffffb733)  //the weird orange is because Color.Yellow is too light with the current background
            else -> Color.Green
        }

        _satisfaction.update {
            SatisfactionUiState(
                fillPercent = newFillPercent,
                color = newColor,
                unsatisfiedIcon = newIcon
            )
        }
    }

     fun makeRequests(context: Context) {
         val backupLocation = Location("")
            backupLocation.latitude = 59.913868
            backupLocation.longitude = 10.752245
         viewModelScope.launch(Dispatchers.IO) {
                val tracker = LocationTracker(context)
                tracker.getLocation().addOnSuccessListener { location ->
                    Log.d(TAG, "In onSuccessListener w/ location: $location")
                    if(location !=null) {
                        getCurrentWeather(location)
                        getRelevantAlerts(location)
                    }
                    else{
                        Log.e(TAG, "Location in success is null")
                        Toast.makeText(context, "Klarte ikke finne din posisjon \n standard-posisjon er Oslo", Toast.LENGTH_LONG).show()
                        makeRequestsWithoutLocation()

                    }
                }.addOnFailureListener { e ->
                    Log.e(TAG, "Failed to get location: ${e.message}.")
                    makeRequestsWithoutLocation()
                }
         }
    }

    /*
    This function is called when the app fails to get the user's location.
    This can be either due to refused permission, or the user's location services being disabled or unavailable.
     */
    fun makeRequestsWithoutLocation(){
        val backupLocation = Location("")
        backupLocation.latitude = 59.913868
        backupLocation.longitude = 10.752245
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentWeather(backupLocation)
            getRelevantAlerts(backupLocation)
        }
    }

    @Throws(ApiAccessException::class)
    fun getCurrentWeather(location : Location) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(
                TAG,
                "Position in getCurrentWeather: ${location.latitude}, ${location.longitude}"
            )
            try {
                locationForecastRepo.fetchLocationForecast(
                    location.latitude,
                    location.longitude
                )
            }catch(e:ApiAccessException){
                throw e
            }

            _currentWeatherState.update {
                Log.d("getCurrentWeather", "Updating current weather: ${locationForecastRepo.getTodayWeather()}")
                locationForecastRepo.getTodayWeather()
            }
            updateSatisfaction(characterTemp = characterState.value.findAppropriateTemp())
            _next6DaysState.update {
                locationForecastRepo.getNext6daysForecast()
            }
        }
    }


    fun getRelevantAlerts(location : Location) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG,
                "Position in getRelevantAlerts: ${location.latitude}, ${location.longitude}"
            )
            val cards = metAlertsRepo.henteVarselKort(
                    latitude = location.latitude,
                    longitude = location.longitude
            )
            _alerts.update { currentState ->
                currentState.copy(alerts = cards)
            }
        }
    }
}

