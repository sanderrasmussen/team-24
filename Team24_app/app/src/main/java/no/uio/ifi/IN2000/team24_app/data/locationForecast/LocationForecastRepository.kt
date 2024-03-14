package no.uio.ifi.IN2000.team24_app.data.locationForecast


data class WeatherNow(
    var time : String? = null,
    var air_pressure_at_sea_level : Double? = null,
    var air_temperature : Double? = null,
    var cloud_area_fraction : Double? = null,
    var relative_humidity : Double? = null,
    var wind_from_direction : Double? = null,
    var wind_speed : Double? = null
)
class LocationForecastRepository{
    val dataSource : LocationForecastDatasource = LocationForecastDatasource()
    //still unsure how often this hould be updated
    var locationForecast : LocationForecast? = null
    var ForecastMap : HashMap<String?, ArrayList<Timeseries>>? = HashMap<String?, ArrayList<Timeseries>>() //dato som nøkkel og timeseries som verdi

    //re-fetching api every hour is what i have in mind
    suspend fun FetchLocationForecast(lat:Double, lon: Double) {
        //get forecast object
        if (locationForecast==null){
            locationForecast = dataSource.getLocationForecastData(lat, lon)
        }
    }
    fun getProperties(): Properties? {
        return locationForecast?.properties
    }
    fun getTimeseries(): ArrayList<Timeseries>? {
        return getProperties()?.timeseries
    }

    fun getWeatherNow(): WeatherNow? {
        var time: String? = getTimeseries()?.get(0)?.time
        var details: InstantDetails? = getTimeseries()?.get(0)?.data?.instant?.details
        return WeatherNow(
            time,
            details?.air_pressure_at_sea_level,
            details?.air_temperature,
            details?.cloud_area_fraction,
            details?.relative_humidity,
            details?.wind_from_direction,
            details?.wind_speed
        )
    }
    fun organizeForecastIntoMapByDay(){
        getTimeseries()?.forEach{
            var date = it.time?.split("T")?.get(0)
            var time = it.time?.split("T")?.get(1)
            time = time?.replace("Z", "")

            if (ForecastMap?.containsKey(date)!=null){
                ForecastMap!![date] = arrayListOf<Timeseries>()
            }
            ForecastMap?.get(date)?.add(it)
        }
    }



}