package no.uio.ifi.IN2000.team24_app.data.locationForecast


class LocationForecastRepository{
    val dataSource : LocationForecastDatasource = LocationForecastDatasource()
    //still unsure how often this hould be updated
    var locationForecast : LocationForecast = UpdateLocationForecastObject()


    //re-fetching api every hour is what i have in mind
    fun UpdateLocationForecastObject(lat:Double, lon: Double){
        //get forecast object
        return locationForecast : LocationForecast = dataSource.getLocationForecastData(lat, lon)
    }

    fun getProperties(){
        return locationForecast.properties
    }
    fun getTimeseries(){
        return getProperties().timeseries
    }



    fun organizeTimeseriesIntoMap(){
        return Null
    }





}