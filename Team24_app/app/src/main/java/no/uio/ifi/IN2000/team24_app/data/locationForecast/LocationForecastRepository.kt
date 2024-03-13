package no.uio.ifi.IN2000.team24_app.data.locationForecast


class LocationForecastRepository{
    val dataSource : LocationForecastDatasource = LocationForecastDatasource()
    //still unsure how often this hould be updated
    var locationForecast : LocationForecast = getUpdatedLocationForecastObject()
    //still unsure if timeseries data should be array og hashmap
    // to me it makes sense to make it an array and re-fetch data every hour so that the first array index is always the current weather
    var timeseriesList : ArrayList<Timeseries>

    //the hashmap will have the date as key an an array of forecasts as values
    var ForecastMap : HashMap<String, ArrayList<Data>>

    //re-fetching api every hour is what i have in mind
    fun getUpdatedLocationForecastObject(lat:Double, lon: Double){
        //get forecast object
        locationForecast : LocationForecast = dataSource.getLocationForecastData(lat, lon)
        timeseries = locationForecast
    }
    private fun UpdateForecastMap(){
        timeseriesList.forEach{
            var date = it.time.split("T")[0]
            if ForecastMap[date]== Null{
                ForecastMap.put(date,ArrayList<Data>)
            }
            else if ForecastMap[date]

        }
    }


}