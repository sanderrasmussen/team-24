package no.uio.ifi.IN2000.team24_app.ui.home

import android.content.Context
import android.location.Location
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.data.bank.BankRepository
import no.uio.ifi.IN2000.team24_app.data.character.Character
import no.uio.ifi.IN2000.team24_app.data.character.getDefaultBackupCharacter
import no.uio.ifi.IN2000.team24_app.data.character.loadSelectedClothes
import no.uio.ifi.IN2000.team24_app.data.location.LocationTracker
import no.uio.ifi.IN2000.team24_app.data.locationForecast.ApiAccessException
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecast
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastDatasource
import no.uio.ifi.IN2000.team24_app.data.locationForecast.LocationForecastRepository
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import no.uio.ifi.IN2000.team24_app.data.metAlerts.WarningCard
import no.uio.ifi.IN2000.team24_app.data.metAlerts.metAlertsRepository.MetAlertsRepo
import no.uio.ifi.IN2000.team24_app.ui.getNextSixDays
import kotlin.math.abs

/**
 * A ui dataclass for the alerts
 * @param alerts a list of warning cards
 * @param show a boolean to show the alerts
 * @see WarningCard
 * @see no.uio.ifi.IN2000.team24_app.ui.components.alerts.AlertCard
 * @see no.uio.ifi.IN2000.team24_app.ui.components.alerts.AlertCardCarousel
 */
data class AlertsUiState(

    val alerts: List<WarningCard> = emptyList(),
    val show:Boolean = false
)
/**
 * A ui dataclass for the weather details card that appears on click
 * @param weatherDetails a list of weather details - 1 if the card is for an hour today, multiple if for a day in the future
 * @param dayStr a string for the day - null if the card is for an hour today, the day if for a day in the future
 * @see WeatherDetails
 * @see no.uio.ifi.IN2000.team24_app.ui.components.forecast.WeatherDetailCard
 */
data class WeatherDetailsUiState(
    var weatherDetails: List<WeatherDetails>? = null, //basically, we don't need a show variable, because if this is null, we don't show the card
    val dayStr : String? = null
)

/**
 * A ui dataclass for the satisfaction meter
 * @param fillPercent a float for the fill percentage of the meter
 * @param color a color for the meter
 * @param unsatisfiedIcon an int for the icon to show when the user is unsatisfied
 * @see no.uio.ifi.IN2000.team24_app.ui.components.character.SatisfactionBar
 */
data class SatisfactionUiState(
    val fillPercent: Float = 0.0f,
    val color : Color = Color.Green,
    val unsatisfiedIcon: Int = R.drawable.too_cold
    )

/**
 * A ViewModel for the HomeScreen
 * @param locationForecastRepo a repository for the location forecast
 * @param metAlertsRepo a repository for the met alerts
 * @param bankRepo a repository for the bank
 * @param alerts a stateflow for the alerts
 * @param satisfaction a stateflow for the satisfaction
 * @param weatherDetails a stateflow for the weather details card
 * @param currentWeatherState a stateflow for the current weather - hour by hour, for the day
 * @param next6DaysState a stateflow for the weather for the next 6 days
 */
class HomeScreenViewModel(
    private val TAG:String = "HomeScreenViewModel",
    private val locationForecastRepo : LocationForecastRepository = LocationForecastRepository(),
    private val metAlertsRepo: MetAlertsRepo = MetAlertsRepo(),
    private val bankRepo : BankRepository = BankRepository(),

    private var _alerts : MutableStateFlow<AlertsUiState> = MutableStateFlow(AlertsUiState()),
    val alerts : StateFlow<AlertsUiState> = _alerts.asStateFlow(),
    private var _satisfaction : MutableStateFlow<SatisfactionUiState> = MutableStateFlow(SatisfactionUiState()),
    val satisfaction : StateFlow<SatisfactionUiState> = _satisfaction.asStateFlow(),

    private val _weatherDetails : MutableStateFlow<WeatherDetailsUiState> = MutableStateFlow(WeatherDetailsUiState()),
    val weatherDetails : StateFlow<WeatherDetailsUiState> = _weatherDetails.asStateFlow(),
    private val _currentWeatherState: MutableStateFlow<ArrayList<WeatherDetails>> = MutableStateFlow(ArrayList()),
    val currentWeatherState: StateFlow<ArrayList<WeatherDetails>> = _currentWeatherState.asStateFlow(),
    private val _next6DaysState :MutableStateFlow<ArrayList<WeatherDetails?>?> = MutableStateFlow(ArrayList()),
    val next6DaysState: StateFlow<ArrayList<WeatherDetails?>?> = _next6DaysState.asStateFlow()

): ViewModel() {

    //this is now default value in case of failed load form disk
    val characterState = MutableStateFlow(loadClothesFromDisk())

    init {
        //I will now laod selected clothes from disk
        viewModelScope.launch {
            characterState.update { loadSelectedClothes() }
        }
    }

    /**
     * Function to update the weather details card
     * @param weatherDetails a list of weather details - 1 if the card is for an hour today, multiple if for a day in the future. null if the card should be hidden
     * @param dayStr a string for the day - null if the card is for an hour today, the day if for a day in the future
     * @see WeatherDetails
     * @see WeatherDetailsUiState
     */
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

    /**
     * Function to show or hide the alerts
     * @param show a boolean to show the alerts
     * @see AlertsUiState
     */
    fun showAlerts(show:Boolean){
        _alerts.update {
            AlertsUiState(alerts = _alerts.value.alerts, show = show)
        }
    }

    /**
     * Function to get the stored character.
     * @return the [Character] as stored in the database
     * @see Character
     */
    private fun loadClothesFromDisk(): Character {
        var character = getDefaultBackupCharacter()
        viewModelScope.launch {
            character = loadSelectedClothes()

        }
        updateSatisfaction(characterTemp = character.findAppropriateTemp())

        return character
    }

    /**
     * Function to update the satisfaction meter
     * @param characterTemp the temperature the character is dressed for
     * @param actualTemp the actual temperature
     * @see SatisfactionUiState
     * @see Character.findAppropriateTemp
     */
    fun updateSatisfaction(characterTemp: Double, actualTemp: Double = (_currentWeatherState.value.firstOrNull()?.air_temperature ?:0.0)) {

        var newFillPercent = 0.0f
        var newColor = Color.Green
        var newIcon = R.drawable.cold_emoji

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

    /**
     * function to make the api requests. There are two paths to make requests to the api: with or without location.
     * If the app has permission to use location, this function is called, the location is fetched, and the requests are made.
     * If the app does not have permission to use location, or the location services are disabled, the function [makeRequestsWithoutLocation] is called.
     * @param context the context of the app
     * @see LocationTracker
     */
     fun makeRequests(context: Context) {
         viewModelScope.launch(Dispatchers.IO) {
                val tracker = LocationTracker(context)
                tracker.getLocation().addOnSuccessListener { location ->
                    Log.d(TAG, "In onSuccessListener w/ location: $location")

                    if(location !=null) {
                        try {
                            getCurrentWeather(location)
                            getRelevantAlerts(location)
                        }catch(e:ApiAccessException){
                            Toast.makeText(context, "MET-tjenestene er ikke tilgjengelige for øyeblikket - prøv igjen senere", Toast.LENGTH_LONG).show()
                            return@addOnSuccessListener
                        }
                    }
                    else{
                        Log.e(TAG, "Location in success is null")
                        Toast.makeText(context, "Klarte ikke finne din posisjon \n standard-posisjon er Oslo", Toast.LENGTH_LONG).show()
                        try {
                            makeRequestsWithoutLocation()
                        }catch(e:ApiAccessException){
                            Toast.makeText(context, "MET-tjenestene er ikke tilgjengelige for øyeblikket - prøv igjen senere", Toast.LENGTH_LONG).show()
                            return@addOnSuccessListener
                        }

                    }
                }.addOnFailureListener { e ->
                    Log.e(TAG, "Failed to get location: ${e.message}.")
                    try {
                        makeRequestsWithoutLocation()
                    }catch(e:ApiAccessException){
                        Toast.makeText(context, "MET-tjenestene er ikke tilgjengelige for øyeblikket - prøv igjen senere", Toast.LENGTH_LONG).show()
                    }
                }
         }
    }

    /**
     *This function is called when the app fails to get the user's location.
     *This can be either due to refused permission, or the user's location services being disabled or unavailable.
     */
    @Throws(ApiAccessException::class)
    fun makeRequestsWithoutLocation(){
        val backupLocation = Location("")
        backupLocation.latitude = 59.913868
        backupLocation.longitude = 10.752245
        viewModelScope.launch(Dispatchers.IO) {
            try {
                getCurrentWeather(backupLocation)
                getRelevantAlerts(backupLocation)
            }catch(e:ApiAccessException){
                throw e
            }
        }
    }

    /**
     * Function to get the current weather
     * @param location the location to get the weather for
     * @throws ApiAccessException if the api access fails
     * @see LocationForecastRepository.fetchLocationForecast
     * @see LocationForecastRepository.getTodayWeather
     * @see LocationForecastRepository.getNext6daysForecast
     */
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


    /**
     * Function to get the relevant alerts
     * @param location the location to get the alerts for
     * @see MetAlertsRepo.getWarningCards
     */
    fun getRelevantAlerts(location : Location) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG,
                "Position in getRelevantAlerts: ${location.latitude}, ${location.longitude}"
            )
            val cards = metAlertsRepo.getWarningCards(
                    latitude = location.latitude,
                    longitude = location.longitude
            )
            _alerts.update { currentState ->
                currentState.copy(alerts = cards)
            }
        }
    }
}

