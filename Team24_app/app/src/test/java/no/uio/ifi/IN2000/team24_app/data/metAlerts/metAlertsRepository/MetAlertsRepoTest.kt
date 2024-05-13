package no.uio.ifi.IN2000.team24_app.data.metAlerts.metAlertsRepository

import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.metAlerts.MetAlertsDataSource
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.ui.getDrawableResourceId
import org.junit.Assert.*
import org.junit.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow

class MetAlertsRepoTest {
    val repo = MetAlertsRepo(dataSource = MetAlertsDataSource(testSource = true))

    @Test
    fun hentIkonID(){
        val ids = listOf("avalanches", "flood", "ice", "landslide") //chose some random ids
        val icons = listOf("icon_warning_avalanches", "icon_warning_flood", "icon_warning_ice", "icon_warning_landslide")
        assertAll(
            {assertEquals(icons[0], repo.hentIkonID(ids[0]))},
            {assertEquals(icons[1], repo.hentIkonID(ids[1]))},
            {assertEquals(icons[2], repo.hentIkonID(ids[2]))},
            {assertEquals(icons[3], repo.hentIkonID(ids[3]))}
        )

        assertDoesNotThrow{
            R.drawable::class.java.getField("${repo.hentIkonID(ids[0])}_yellow")    //asserts that the icon exists - if it didn't a NoSuchFieldException would be thrown
        }
    }

    @Test
    fun henteVarselKort() {
        //backupLocation.latitude = 59.913868
        //backupLocation.longitude = 10.752245
        val varselKort = runBlocking {  repo.henteVarselKort(59.913868,10.752245,) }
        assertNotNull(varselKort)
        println(varselKort)
        println("actual size: ${varselKort.size}")

        assertEquals(6, varselKort.size)   //the full test-file has 6 alerts, all farePaagar=Pågår
        varselKort.forEach{assertEquals("Pågår", it.farePaagar)}
        assertEquals("icon_warning_rainflood_yellow", varselKort[0].kortImageUrl)
        assertEquals("fareNiva=Gult nivå", varselKort[0].fareNiva)
    }
}