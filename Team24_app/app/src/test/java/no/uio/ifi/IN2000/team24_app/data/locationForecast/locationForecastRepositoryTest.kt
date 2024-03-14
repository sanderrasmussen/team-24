package no.uio.ifi.IN2000.team24_app.data.locationForecast

import android.util.Log
import kotlinx.coroutines.runBlocking
import org.junit.Test

class locationForecastRepositoryTest {
    val repo : LocationForecastRepository = LocationForecastRepository()
    @Test
    fun testCurrentWeather(){
        runBlocking{
            repo.FetchLocationForecast(59.0, 10.0)
            var weatherNow : WeatherDetails? = repo.getWeatherNow()
            Log.d(weatherNow?.time)
        }
    }
}