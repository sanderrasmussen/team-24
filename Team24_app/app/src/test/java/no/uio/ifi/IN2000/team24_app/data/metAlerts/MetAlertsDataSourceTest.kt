package no.uio.ifi.IN2000.team24_app.data.metAlerts

import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

class MetAlertsDataSourceTest {

    @Test
    fun getMetAlertData() {
        val dataSource = MetAlertsDataSource(testSource = true)
        val metAlerts = runBlocking { dataSource.getMetAlertData(59.913868, 10.752245) }
        /*metalerts should never be null: if it is not previously cached, it should be fetched from server.
        this only returns null if the response code is not a success (2xx)*/
        assertNotNull(metAlerts)
        assertNotNull(metAlerts?.features)
        assertEquals(5, metAlerts?.features?.size) //the test endpoint has 25 features
        assertEquals(true, metAlerts?.features?.get(0)?.geometry is Polygon) //the first feature should be a polygon
        assertEquals("forestFire", metAlerts?.features?.get(0)?.properties?.event) //the first feature should be a rainFlood-event

    }
}