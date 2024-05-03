package no.uio.ifi.IN2000.team24_app.ui.home

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.location.Location
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.LaunchedEffect
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.R

import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository

import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.heads
import no.uio.ifi.IN2000.team24_app.data.character.legs
import no.uio.ifi.IN2000.team24_app.data.character.torsos
import no.uio.ifi.IN2000.team24_app.data.location.LocationTracker
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecast
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastDatasource
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastRepository
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Point
import no.uio.ifi.IN2000.team24_app.data.metAlerts.VarselKort
import no.uio.ifi.IN2000.team24_app.data.metAlerts.metAlertsRepository.MetAlertsRepo
import kotlin.math.abs
import kotlin.reflect.typeOf

data class AlertsUiState(
    val alerts: List<VarselKort> = emptyList()
)
data class SatisfactionUiState(
    val fillPercent: Float = 0.0f,
    val color : Color = Color.Green,
    val unsatisfiedIcon: Int = R.drawable.too_cold //TODO this should be a custom icon
    )


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



): ViewModel() {
    //todo move these states to the viewModel
    var currentWeatherState: StateFlow<ArrayList<WeatherDetails>?> =
        locationForecastRepo.ObserveTodayWeather();
    val next6DaysState: StateFlow<ArrayList<WeatherDetails?>?> =
        locationForecastRepo.ObserveNext6DaysForecast()


    //this is just to render a default character, TODO should call a load from disk()-method on create
    private val character =
        Character(head = heads().first(), torso = torsos().first(), legs = legs().first())
    val characterState = MutableStateFlow(character)


    init {
        updateSatisfaction(characterTemp = character.findAppropriateTemp())
        getBalanceFromDb()
    }


    fun getBalanceFromDb() {

        viewModelScope.launch {
            _balance.update {

                bankRepo.getBankBalance()
            }
        }
    }


    fun updateSatisfaction(characterTemp: Double) {
        var newFillPercent = 0.0f
        var newColor = Color.Green
        var newIcon = R.drawable.too_cold

        val temp: Double = currentWeatherState.value?.firstOrNull()?.air_temperature ?: 0.0
        Log.d(TAG, "Temp: $temp")
        Log.d(TAG, "CharacterTemp: $characterTemp")
        val delta = temp - characterTemp
        Log.d(TAG, "Delta: $delta")

        //FILL%
        newFillPercent = maxOf((1 - (abs(delta) / 10)).toFloat(), 0.01f)
        Log.d(TAG, "Satisfaction%: $newFillPercent")

        //ICON
        newIcon = if (delta > 0) {
            Log.d(TAG, "Too hot")
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
         viewModelScope.launch(Dispatchers.IO) {
            if (_userLocation.value == null) {
                val tracker = LocationTracker(context)
                _userLocation.value = tracker.getLocation().result
            }
         }
        getCurrentWeather()
        getRelevantAlerts()

    }

    fun getCurrentWeather() {
        viewModelScope.launch(Dispatchers.IO) {
            //!position broke, todo look into LocationTracker
            Log.d(
                TAG,
                "Position: ${_userLocation.value?.latitude}, ${_userLocation.value?.longitude}"
            )
            locationForecastRepo.fetchLocationForecast(
                _userLocation.value?.latitude ?: 59.913868,
                _userLocation.value?.longitude ?: 10.752245
            )

        }
    }


    fun getRelevantAlerts() {
        viewModelScope.launch(Dispatchers.IO) {
            //!position broke, todo look into LocationTracker
            val cards = metAlertsRepo.henteVarselKort(
                Point(
                    _userLocation.value?.latitude ?: 59.913868,
                    _userLocation.value?.longitude ?: 10.752245
                )
            )
            _alerts.update { currentState ->
                currentState.copy(alerts = cards)
            }
        }
    }
}

