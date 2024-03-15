package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class WeatherDetails(
    var time : String? = null,
    var air_pressure_at_sea_level : Double? = null,
    var air_temperature : Double? = null,
    var cloud_area_fraction : Double? = null,
    var relative_humidity : Double? = null,
    var wind_from_direction : Double? = null,
    var wind_speed : Double? = null,
    var next_1_hours_symbol_code : String?= null,
    var next_1_hours_precipitation_amount : Double? = null,
    var next_6_hours_symbol_code : String? = null,
    var next_6_hours_precipitation_amount : Double? = null,
    var next_12_hours_symbol_code : String? = null,
    var next_12_hours_precipitation_amount : Double? = null
)
class LocationForecastRepository{
    private val dataSource : LocationForecastDatasource = LocationForecastDatasource()
    //still unsure how often this hould be updated
    private var locationForecast : LocationForecast? = null

    //denne skal sendes videre til viewmodel og observeres
    private val _forecastMap = MutableStateFlow<HashMap<String?, ArrayList<WeatherDetails>>>(HashMap())
    private val _currentWeather = MutableStateFlow<WeatherDetails?>(null)
    private val _todayForecast = MutableStateFlow<ArrayList<WeatherDetails>?>(null)
    //re-fetching api every hour is what i have in mind
    suspend fun FetchLocationForecast(lat:Double, lon: Double) {
        //get forecast object
        if (locationForecast==null){
            locationForecast = dataSource.getLocationForecastData(lat, lon)
        }
        getTodayWeather()
        organizeForecastIntoMapByDay()
        getWeatherNow()
    }
    private fun getProperties(): Properties? {
        return locationForecast?.properties
    }
    private fun getTimeseries(): ArrayList<Timeseries>? {
        return getProperties()?.timeseries
    }

    private fun createWeatherDetailObject(timeseries_Index : Int): WeatherDetails {
        //HUSK SKRIVE TRY CATCH
        var time: String? = getTimeseries()?.get(timeseries_Index)?.time
        var details: InstantDetails? = getTimeseries()?.get(timeseries_Index)?.data?.instant?.details
        var next_1_hours_details = getTimeseries()?.get(timeseries_Index)?.data?.next1Hours
        var next_6_hours_details = getTimeseries()?.get(timeseries_Index)?.data?.next6Hours
        var next_12_hours_details = getTimeseries()?.get(timeseries_Index)?.data?.next12Hours

        return WeatherDetails(
            time,
            details?.air_pressure_at_sea_level,
            details?.air_temperature,
            details?.cloud_area_fraction,
            details?.relative_humidity,
            details?.wind_from_direction,
            details?.wind_speed,
            next_1_hours_details?.summary?.symbolCode,
            next_1_hours_details?.details?.precipitationAmount,
            next_6_hours_details?.summary?.symbolCode,
            next_6_hours_details?.details?.precipitationAmount,
            next_12_hours_details?.summary?.symbolCode,
            next_12_hours_details?.details?.precipitationAmount
        )
    }

    private fun getWeatherNow(): WeatherDetails? {
        var weatherNow =  createWeatherDetailObject(0)
        updateCurrentWeatherStateFlow(weatherNow)
        return weatherNow
    }

    private fun getTodayWeather(): ArrayList<WeatherDetails>? {
        var data = getTimeseries()?.subList(0,24)
        var todayDate = data?.get(0)?.time?.split("T")?.get(0)
        var todayWeather : ArrayList<WeatherDetails>? = ArrayList<WeatherDetails>()

        data?.forEachIndexed { index, e ->
            var date = e.time?.split("T")?.get(0)
            var time = e.time?.split("T")?.get(1)?.split(":")?.get(0)
            if (date==todayDate){
                var weather : WeatherDetails = createWeatherDetailObject(index)
                weather.time= time
                todayWeather?.add(weather)
            }
        }
        updateTodayForecast(todayWeather)
        return todayWeather
    }


    private fun organizeForecastIntoMapByDay() : HashMap<String?, ArrayList<WeatherDetails>>?{
        var ForecastMap : HashMap<String?, ArrayList<WeatherDetails>>? = HashMap<String?, ArrayList<WeatherDetails>>()
        getTimeseries()?.forEachIndexed { index, e ->
            var weatherObject : WeatherDetails = createWeatherDetailObject(index)
            var date = e.time?.split("T")?.get(0)
            var time = e.time?.split("T")?.get(1)
            time = time?.replace("Z", "")
            weatherObject.time= time

            if (ForecastMap != null) {
                if (!ForecastMap.containsKey(date)){
                    ForecastMap!![date] = arrayListOf<WeatherDetails>()
                }
            }
            ForecastMap!![date]?.add(weatherObject)
        }
        updateForecastMapStateFlow(ForecastMap)
        return ForecastMap
    }


    private fun updateForecastMapStateFlow(newMap : HashMap<String?, ArrayList<WeatherDetails>>?){
        _forecastMap.update {
            newMap!!
        }

    }
    fun ObserveForecastMap(): StateFlow<HashMap<String?, ArrayList<WeatherDetails>>> = _forecastMap.asStateFlow()

    private fun updateCurrentWeatherStateFlow(weather :  WeatherDetails?){
        _currentWeather.update {
            weather!!
        }
    }
    fun ObserveCurrentWeather(): StateFlow<WeatherDetails?> = _currentWeather.asStateFlow()

    private fun updateTodayForecast(forecast : ArrayList<WeatherDetails>?){
        _todayForecast.update {
            forecast!!
        }
    }
    fun ObserveTodayWeather(): StateFlow<ArrayList<WeatherDetails>?> = _todayForecast.asStateFlow()
}