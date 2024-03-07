package no.uio.ifi.IN2000.team24_app.data.locationForecast

import android.location.Location
import android.location.LocationManager
import kotlinx.coroutines.runBlocking
import org.junit.Test

class locationForecastDatasourceKtTest {

    val source = locationForecastDatasource()
     @Test
     fun testForecast(){
         runBlocking {

             val forecast = source.getLocationForecastData(59.0, 10.0)
            println(forecast)
         }
     }
}