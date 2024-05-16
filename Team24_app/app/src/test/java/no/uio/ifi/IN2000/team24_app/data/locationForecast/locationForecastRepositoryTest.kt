package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Test

//REMEMBER TO REMOVE LOG.d FROM FUNCTIONS IN ORDER TO MAKE THE TESTS WORK
class LocationForecastRepositoryTest {

    @Test
    fun tests() {


        runBlocking {
            val forecastRepo = mockLocationForecastRepo()
            val locationForecast: LocationForecast? =
                runBlocking { forecastRepo.fetchLocationForecast(59.91, 10.75) }

            assertNotNull(locationForecast)

            val timeseries = forecastRepo.getTimeseries()
            // Ensure that the timeseries is not null
            assertNotNull(timeseries)

            val weatherDetail = forecastRepo.createWeatherDetailObject(0)
            // Ensure that the created weather detail object is not null
            assertNotNull(weatherDetail)
        }

    }
}