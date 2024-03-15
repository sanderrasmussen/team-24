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
            var map = repo.get_ForecastMap()

            println("-------------- Test start ---------------")
            println(weatherNow?.time)
            println(weatherNow?.air_pressure_at_sea_level)
            println(weatherNow?.air_temperature)
            println(weatherNow?.cloud_area_fraction)
            println(weatherNow?.relative_humidity)
            println(weatherNow?.wind_from_direction)
            println(weatherNow?.wind_speed)
            println(weatherNow?.next_1_hours_symbol_code)
            println(weatherNow?.next_1_hours_precipitation_amount)
            println("----mapTest----")
            //oops datoen i testfilen må endres frem i tid slik at den ikke blir null
            println(map?.get("2024-03-18")?.size)
            println(map?.get("2024-03-18")?.get(0)?.time)
            println("-------------- Test slutt ---------------")
        }
    }
}