package no.uio.ifi.IN2000.team24_app.data.locationForecast

import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.SerialName
import org.junit.Test

class locationForecastDatasourceKtTest {

    val source = mockLocationForecastDatasource()
    @Test
    fun test(){
        runBlocking {
            val forecast = source.getLocationForecastData(59.0, 10.0)
            println(forecast)
            assertNotNull(forecast)
            assertNotNull(forecast?.type)
            assertNotNull(forecast?.geometry)
            assertNotNull(forecast?.properties)

        }
    }
}
