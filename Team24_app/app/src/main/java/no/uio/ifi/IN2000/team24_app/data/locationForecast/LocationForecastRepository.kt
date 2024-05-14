package no.uio.ifi.IN2000.team24_app.data.locationForecast

import android.annotation.SuppressLint

import android.content.ContentValues.TAG
import android.util.Log
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
    private var locationForecast : LocationForecast? = null //PREFEREABLY only locationforecast should be nullable

    private var forecastMap : HashMap<String?, ArrayList<WeatherDetails>>? = null

    //re-fetching api every hour is what i have in mind
    suspend fun fetchLocationForecast(lat:Double, lon: Double): LocationForecast? {
        try {
            //get forecast object
            if (locationForecast == null) {
                locationForecast =
                    dataSource.getLocationForecastData(lat, lon)//DATA SOURCE IS NULLABLE
            }
            if (locationForecast!=null){
                keepFirstIndexUpToDate()
                forecastMap = organizeForecastIntoMapByDay()

            }

        }
        catch (e: Exception) {
            // Handle eventual exeptions
            Log.e(TAG, "An error occurred while fetching location forecast: ${e.message}", e)
        }
        return locationForecast;
    }

    fun getTimeseries(): ArrayList<Timeseries>? {
        return locationForecast?.properties?.timeseries
    }

    fun createWeatherDetailObject(timeseries_Index : Int): WeatherDetails {
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

    /*fun getWeatherNow(): WeatherDetails? {
        var weatherNow =  createWeatherDetailObject(0)
        return weatherNow
    }*/

    @SuppressLint("NewApi") //THIS CODE HAS BENN REFACTORED AND SHOULD NOT CASE INDEX OUT OF BOUNDS ANYMORE
    fun keepFirstIndexUpToDate()  {

            var currentTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd:HH:mm:ss")

            var forecast = getTimeseries()?.get(0)?.time
            forecast = forecast?.replace("Z", "")
            forecast = forecast?.replace("T", ":")
            var forecastTime = LocalDateTime.parse(forecast, formatter)

            // Make a copy of the list to avoid ConcurrentModificationException
            val timeseriesCopy = ArrayList(getTimeseries())

            while (forecastTime.isBefore(currentTime.minusHours(1))) {

                // Remove outdated weather data from the copied list
                timeseriesCopy.removeAt(0)

                currentTime = LocalDateTime.now()

                forecast = timeseriesCopy.getOrNull(0)?.time
                forecast = forecast?.replace("Z", "")
                forecast = forecast?.replace("T", ":")
                forecastTime = LocalDateTime.parse(forecast, formatter)

            }
            // Update the original list with the modified copy
            locationForecast?.properties?.timeseries = timeseriesCopy
    }
    fun getTodayWeather(): ArrayList<WeatherDetails> {
        var data = getTimeseries()?.subList(0,24)
        var todayDate = data?.get(0)?.time?.split("T")?.get(0)
        var todayWeather : ArrayList<WeatherDetails> = ArrayList<WeatherDetails>()

        data?.forEachIndexed { index, e ->
            var date = e.time?.split("T")?.get(0)
            var time = e.time?.split("T")?.get(1)?.split(":")?.get(0)
            if (date==todayDate){
                var weather : WeatherDetails = createWeatherDetailObject(index)
                weather.time= time
                todayWeather?.add(weather)
            }
        }
        return todayWeather
    }

    fun getWeatherOnDate(date : String) : ArrayList<WeatherDetails>? {
        return forecastMap?.get(date)
    }

    @SuppressLint("NewApi")
    fun getNext7DaysForecast() : ArrayList<ArrayList<WeatherDetails>?> {
        var next7DaysForecast = ArrayList<ArrayList<WeatherDetails>?>()

        for (i in 0..6) {
            val current = LocalDateTime.now().plusDays(i.toLong())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = current.format(formatter)
            next7DaysForecast.add(getWeatherOnDate(date))
        }
        Log.d("FORECAST", "amount of entries in next7DaysForecast: ${next7DaysForecast.count()}")
        Log.d("FORECAST", "entries per day: ${next7DaysForecast[1]?.count()}")
        Log.d("FORECAST", "entries per day: ${next7DaysForecast[6]?.count()}")

        return next7DaysForecast
    }
    @SuppressLint("NewApi")
    fun getNext6daysForecast() :ArrayList<WeatherDetails?>? { //returns next 6 days with 12:00 as only weatherdetails object of each day
        var next6DaysForecast = ArrayList<WeatherDetails?>()
        for (i in 1..7) {
            val current = LocalDateTime.now().plusDays(i.toLong())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = current.format(formatter)
            var weather = getWeatherOnDate(date)
            weather?.forEach{
                if (it.time == "12"){
                    next6DaysForecast.add(it)
                }
            }
        }
        return next6DaysForecast


    }
    fun organizeForecastIntoMapByDay() : HashMap<String?, ArrayList<WeatherDetails>> {
        var ForecastMap  = HashMap<String?, ArrayList<WeatherDetails>>()
        getTimeseries()?.forEachIndexed { index, e ->
            var weatherObject : WeatherDetails = createWeatherDetailObject(index)
            var date = e.time?.split("T")?.get(0)
            var time = e.time?.split("T")?.get(1)?.split(":")?.get(0)

            weatherObject.time= time

            if (!ForecastMap.containsKey(date)){
                ForecastMap[date] = arrayListOf<WeatherDetails>()
            }
            ForecastMap[date]?.add(weatherObject)
        }
        return ForecastMap
    }





}
