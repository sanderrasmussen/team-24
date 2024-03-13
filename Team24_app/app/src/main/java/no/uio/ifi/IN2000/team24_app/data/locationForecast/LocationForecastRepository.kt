package no.uio.ifi.IN2000.team24_app.data.locationForecast

class LocationForecastRepository{
    val dataSource : LocationForecastDatasource = LocationForecastDatasource()
    //still unsure how often this hould be updated
    var locationForecast : LocationForecast = getUpdatedLocationForecastObject()
    //still unsure if timeseries data should be array og hashmap
    // to me it makes sense to make it an array and re-fetch data every hour so that the first array index is always the current weather
    var timeseriesList : ArrayList<Timeseries>

    //re-fetching api every hour is what i have in mind
    fun getUpdatedLocationForecastObject(lat:Double, lon: Double){
        //get forecast object
        locationForecast : LocationForecast = dataSource.getLocationForecastData(lat, lon)
        timeseries = locationForecast
    }

    fun getTemperatureByDateAndTime(DateTime : Date ) : Details{
        return Null
    }

}