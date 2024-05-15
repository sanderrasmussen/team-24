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


class mockLocationForecastRepo{
    private val dataSource : LocationForecastDatasource = LocationForecastDatasource()
    private var locationForecast : LocationForecast? = null //PREFEREABLY only locationforecast should be nullable

    private var forecastMap : HashMap<String?, ArrayList<WeatherDetails>>? = null

    //re-fetching api every hour is what i have in mind
    /**
     * Fetches location forecast data from the datasource
     * @param lat: Latitude of the location
     * @param lon: Longitude of the location
     * @return LocationForecast: The location forecast data
     * @throws ApiAccessException: If there is a server error on the API side
     * @see ApiAccessException
     */
    @Throws(ApiAccessException::class)
    suspend fun fetchLocationForecast(lat:Double, lon: Double): LocationForecast? {
        try {
            //get forecast object
            if (locationForecast == null) {
                try {
                    locationForecast =
                        dataSource.getLocationForecastData(lat, lon)//DATA SOURCE IS NULLABLE
                }catch(e: ApiAccessException){
                    throw e     //this needs to be thrown to a ui layer, to show a message to the user
                }
            }
            if (locationForecast!=null){
                keepFirstIndexUpToDate()
                forecastMap = organizeForecastIntoMapByDay()

            }

        }
        catch (e: Exception) {
            // Handle eventual exeptions
            //Log.e(TAG, "An error occurred while fetching location forecast: ${e.message}", e)
            if(e is ApiAccessException){
                throw e
            }
        }
        return locationForecast;
    }

    fun getTimeseries(): ArrayList<Timeseries>? {
        return locationForecast?.properties?.timeseries
    }

    /**
     * Creates a WeatherDetails object from the given timeseries data
     * @param timeseries_Index: Index of the timeseries object in the list
     * @return WeatherDetails: The weather details object
     * @see WeatherDetails
     */
    fun createWeatherDetailObject(timeseries_Index : Int): WeatherDetails {
        val time: String? = getTimeseries()?.get(timeseries_Index)?.time
        val details: InstantDetails? = getTimeseries()?.get(timeseries_Index)?.data?.instant?.details
        val next_1_hours_details = getTimeseries()?.get(timeseries_Index)?.data?.next1Hours
        val next_6_hours_details = getTimeseries()?.get(timeseries_Index)?.data?.next6Hours
        val next_12_hours_details = getTimeseries()?.get(timeseries_Index)?.data?.next12Hours

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

    /**
     * this function returns the weather for today, as a list of WeatherDetails-objects.
     * The first object will be the details for the hour that the function is called. these objects are hourly, until midnight.
     * @return ArrayList<WeatherDetails>: The weather details for today
     * @see WeatherDetails
     */
    fun getTodayWeather(): ArrayList<WeatherDetails> {
        val data = getTimeseries()?.subList(0,24)
        val todayDate = data?.get(0)?.time?.split("T")?.get(0)
        val todayWeather : ArrayList<WeatherDetails> = ArrayList<WeatherDetails>()

        data?.forEachIndexed { index, e ->
            val date = e.time?.split("T")?.get(0)
            val time = e.time?.split("T")?.get(1)?.split(":")?.get(0)
            if (date==todayDate){
                val weather : WeatherDetails = createWeatherDetailObject(index)
                weather.time= time
                todayWeather.add(weather)
            }
        }
        return todayWeather
    }

    fun getWeatherOnDate(date : String) : ArrayList<WeatherDetails>? {
        return forecastMap?.get(date)
    }

    @SuppressLint("NewApi")
    fun getNext7DaysForecast() : ArrayList<ArrayList<WeatherDetails>?> {
        val next7DaysForecast = ArrayList<ArrayList<WeatherDetails>?>()

        for (i in 0..6) {
            val current = LocalDateTime.now().plusDays(i.toLong())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = current.format(formatter)
            next7DaysForecast.add(getWeatherOnDate(date))
        }

        return next7DaysForecast
    }
    @SuppressLint("NewApi")
    fun getNext6daysForecast() :ArrayList<WeatherDetails?> { //returns next 6 days with 12:00 as only weatherdetails object of each day
        val next6DaysForecast = ArrayList<WeatherDetails?>()
        for (i in 1..7) {
            val current = LocalDateTime.now().plusDays(i.toLong())
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val date = current.format(formatter)
            val weather = getWeatherOnDate(date)
            weather?.forEach{
                if (it.time == "12"){
                    next6DaysForecast.add(it)
                }
            }
        }
        return next6DaysForecast


    }
    fun organizeForecastIntoMapByDay() : HashMap<String?, ArrayList<WeatherDetails>> {
        val ForecastMap  = HashMap<String?, ArrayList<WeatherDetails>>()
        getTimeseries()?.forEachIndexed { index, e ->
            val weatherObject : WeatherDetails = createWeatherDetailObject(index)
            val date = e.time?.split("T")?.get(0)
            val time = e.time?.split("T")?.get(1)?.split(":")?.get(0)

            weatherObject.time= time

            if (!ForecastMap.containsKey(date)){
                ForecastMap[date] = arrayListOf<WeatherDetails>()
            }
            ForecastMap[date]?.add(weatherObject)
        }
        return ForecastMap
    }





}
