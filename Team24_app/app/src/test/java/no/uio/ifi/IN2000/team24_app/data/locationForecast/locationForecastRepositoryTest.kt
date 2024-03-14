package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.coroutines.runBlocking
import org.junit.Test

class locationForecastRepositoryTest {
    val repo : LocationForecastRepository = LocationForecastRepository()
    @Test
    fun testCurrentWeather(){
        runBlocking{
            repo.FetchLocationForecast(59.0, 10.0)
            var weatherNow : WeatherNow? = repo.getWeatherNow()
            println(weatherNow?.time)
        }
    }
}