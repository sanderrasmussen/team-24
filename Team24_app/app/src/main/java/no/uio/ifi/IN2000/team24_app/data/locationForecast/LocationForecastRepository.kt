package no.uio.ifi.IN2000.team24_app.data.locationForecast


class LocationForecastRepository{
    val dataSource : LocationForecastDatasource = LocationForecastDatasource()
    //still unsure how often this hould be updated
    var locationForecast : LocationForecast = UpdateLocationForecastObject()
    var ForecastMap : HashMap<String?, ArrayList<Timeseries>>? //dato som nøkkel og timeseries som verdi

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

    fun organizeForecastIntoMapByDay(){
        getTimeseries().forEach{
            var date = it.time.split("T")[0]
            var time = it.time.split("T")[1]
            time = time.replace("Z", "")
            if !ForecastMap.containsKey(date){
                ForecastMap.put(date, arrayListOf<timeseries>())
            }
            ForecastMap[date]?.add(it)
        }
    }





}