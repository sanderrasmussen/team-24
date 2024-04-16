package no.uio.ifi.IN2000.team24_app.data.metAlerts.metAlertsRepository

import android.annotation.SuppressLint
import kotlinx.coroutines.runBlocking
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Features
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Geometry
import no.uio.ifi.IN2000.team24_app.data.metAlerts.MetAlerts
import no.uio.ifi.IN2000.team24_app.data.metAlerts.MetAlertsDataSource
import no.uio.ifi.IN2000.team24_app.data.metAlerts.MultiPolygon
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Point
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Polygon
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Properties
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Resources
import no.uio.ifi.IN2000.team24_app.data.metAlerts.VarselKort
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max
import kotlin.math.min



class MetAlertsRepo {
    val dataSource: MetAlertsDataSource = MetAlertsDataSource()
    fun hentInterval(feature: Features): List<String> {
        return feature.wen?.interval ?: emptyList()
    }

    @SuppressLint("SimpleDateFormat")
    fun omFarePaagaar(interval: List<String>): String {
        val pattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
        val sdf = SimpleDateFormat(pattern)
        val startTime = sdf.parse(interval[0])
        val endTime = sdf.parse(interval[1])
        val currentTime = Date()

        return if (currentTime.before(startTime)) {
            "Ventes"
        } else if (currentTime.after(endTime)) {
            "Ferdig"
        } else {
            "Pågår"
        }
    }


    fun hentFareNivaFraAwarenessLevel(awarenessLevel: String?): String? {
        val deler = awarenessLevel?.split(";")
        var nivaa: String? = null

        if (deler != null) {
            if (deler.size > 1) {
                nivaa = deler[1].trim()
            }
        }

        return when (nivaa) {
            "yellow" -> "Gult nivå"
            "orange" -> "Oransje nivå"
            "red" -> "Rødt nivå"
            else -> null
        }


    }

    fun hentFarge(awarenessLevel: String?):String?{
        val deler = awarenessLevel?.split(";")
        var nivaa: String? = null

        if (deler != null) {
            if (deler.size > 1) {
                nivaa = deler[1].trim()
            }
        }
        return nivaa

    }

    fun hentIkonID(event: String?): String {
        //todo this may need to be remapped to use underscores like the files they actually provide
        return when (event) {
            "avalanches" -> "icon-warning-avalanches"
            "blowingSnow" -> "icon-warning-snow"
            "drivingConditions" -> "icon-warning-drivingconditions"
            "flood" -> "icon-warning-flood"
            "forestFire" -> "icon-warning-forestfire"
            "gale" -> "icon-warning-wind"
            "ice" -> "icon-warning-ice"
            "icing" -> "icon-warning-generic"
            "landslide" -> "icon-warning-landslide"
            "polarLow" -> "icon-warning-polarlow"
            "rain" -> "icon-warning-rain"
            "rainFlood" -> "icon-warning-rainflood"
            "snow" -> "icon-warning-snow"
            "stormSurge" -> "icon-warning-stormsurge"
            "lightning" -> "icon-warning-lightning"
            "wind" -> "icon-warning-wind"
            else -> "icon-warning-generic"
        }
    }

    fun sjekkOmBrukerIPolygon(point: Point, polygon: ArrayList<Point>): Boolean {
        val antVertices: Int = polygon.size

        val x = point.x
        val y = point.y

        var inArea: Boolean = false

        var point1 = polygon[0]

        for (i in 1..antVertices) {
            val point2 = polygon[i % antVertices]

                    //sjekke om om punktet er under den nedre kanten av polygonet
            if (y > min(point1.y, point2.y)) {

                //sjekke om om punktet er over den øvre kanten av polygonet
                if (y <= max(point1.y, point2.y)) {

                    //sjekke om punktet kan være til venstre for polygonet
                    if (x <= max(point1.x, point2.x)) {

                        val intersectionX: Double =
                            (y - point1.y) * (point2.x - point1.x) / (point2.y - point1.y) + point1.x

                        if (point1.x == point2.x || x <= intersectionX) {
                            inArea = !inArea;
                        }

                    }

                }
            }
            point1 = point2
        }
        return inArea
    }


    fun sjekkOmBrukerIMultiPolygon(
        point: Point,
        multiPolygon: ArrayList<ArrayList<Point>>
    ): Boolean {
        for (polygon in multiPolygon) {
            if (sjekkOmBrukerIPolygon(point, polygon)) {
                return true
            }
        }
        return false

    }


    suspend fun henteVarselKort(latitude:Double, longitude:Double): ArrayList<VarselKort> {
        val fareVarsler = arrayListOf<VarselKort>()
        val features: List<Features> =
            dataSource.getMetAlertData(latitude, longitude)?.features ?: listOf()
        features.forEach { feature ->
            val geometry: Geometry? = feature.geometry
            if (geometry != null) {

                lagKort(feature, fareVarsler)


            }
        }

        //debug
        /*
        val cards = arrayListOf(

            VarselKort("Pågår", "icon_warning_avalanches_red", "Oslo", "2;yellow;moderate"),
            VarselKort("Ventes", "icon_warning_avalanches_orange", "Viken", "2;yellow;moderate"),
            VarselKort("Ferdig", "icon_warning_avalanches_yellow", "Vestland", "2;yellow;moderate"),
            VarselKort("Pågår", "icon_warning_extreme", "Oslo", "2;yellow;moderate"),
        )
        return cards

         */
        //end debug
        return fareVarsler
    }

    fun lagKort(feature:Features, farevarsler: ArrayList<VarselKort> ){
        val interval = hentInterval(feature)
        val farge = hentFarge(feature.properties?.awarenessLevel)
        val farePaagar = omFarePaagaar(interval)

        val kortImageUrl = "${hentIkonID(feature.properties?.event)}_$farge"


        val lokasjon = feature.properties?.area

        val fareNiva = hentFareNivaFraAwarenessLevel(feature.properties?.awarenessLevel)

        if(lokasjon!= null && fareNiva != null){
            val varselKort= VarselKort(farePaagar, kortImageUrl, lokasjon, fareNiva)
            farevarsler.add(varselKort)
        }
    }
}