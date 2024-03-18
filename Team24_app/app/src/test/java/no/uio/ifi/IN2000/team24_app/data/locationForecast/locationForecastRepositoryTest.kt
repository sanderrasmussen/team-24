package no.uio.ifi.IN2000.team24_app.data.locationForecast

import android.util.Log
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.runBlocking
import org.junit.Test
data class weatherForecastUIState(
    val weatherNow : WeatherDetails?,
    val forecastMap : HashMap<String?, ArrayList<WeatherDetails>>?
)
class locationForecastRepositoryTest {
    val repo : LocationForecastRepository = LocationForecastRepository()
    @Test
    fun testCurrentWeather(){
        runBlocking{
            repo.fetchLocationForecast(59.0, 10.0)


            println("-------------- Test start ---------------")

            """ TESTER STATEFLOW, NÅR DETTE TESTES VIL PRINTING I TERMINALEN ALDRIG TERMINERE, MEN SER UT TIL AT DET FUNGERER ALLIKEVEL
            repo.ObserveCurrentWeather().collect { weatherNow ->
                println(weatherNow?.time)
                println(weatherNow?.air_pressure_at_sea_level)
                println(weatherNow?.air_temperature)
                println(weatherNow?.cloud_area_fraction)
                println(weatherNow?.relative_humidity)
                println(weatherNow?.wind_from_direction)
                println(weatherNow?.wind_speed)
                println(weatherNow?.next_1_hours_symbol_code)
                println(weatherNow?.next_1_hours_precipitation_amount)
            }"""
            repo.ObserveNext7DaysForecast().collect { day ->
                println(day?.get(0)?.get(0)?.time)
                println(day?.get(0)?.get(0)?.air_pressure_at_sea_level)
                println(day?.get(0)?.get(0)?.air_temperature)
                println(day?.get(0)?.get(0)?.cloud_area_fraction)
                println(day?.get(0)?.get(0)?.relative_humidity)
                println(day?.get(0)?.get(0)?.wind_from_direction)
                println(day?.get(0)?.get(0)?.wind_speed)
                println(day?.get(0)?.get(0)?.next_1_hours_symbol_code)
                println(day?.get(0)?.get(0)?.next_1_hours_precipitation_amount)
            }
            """
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
            println(map?.get("2024-03-18")?.get(0)?.time)"""
            println("-------------- Test slutt ---------------")
        }
    }
}