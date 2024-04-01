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



class metAlertsRepo {
    val dataSource: MetAlertsDataSource = MetAlertsDataSource()


    suspend fun hentFeatures(): List<Features> {
        val content: MetAlerts? = dataSource.getMetAlertData()
        return content?.features ?: emptyList()
    }


    fun hentProperties(feature: Features): Properties? {
        return feature.properties

    }

    fun hentResources(feature: Features): List<Resources> {
        return hentProperties(feature)?.resources ?: emptyList()
    }


    fun hentInterval(feature: Features): List<String> {
        return feature.wen?.interval ?: emptyList()
    }


    @SuppressLint("SimpleDateFormat")
    fun skrivUtInterval(interval: List<String>): String {
        val pattern = "yyyy-MM-dd'T'HH:mm:ssXXX"
        val sdf = SimpleDateFormat(pattern)
        val startDateTime = sdf.parse(interval[0])
        val endDateTime = sdf.parse(interval[1])

        val dateFormat =
            DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT, Locale.getDefault())

        val startFormatted = startDateTime?.let { dateFormat.format(it) }
        val endFormatted = endDateTime?.let { dateFormat.format(it) }

        return "Tidsperiode\n$startFormatted - faren pågår\n$endFormatted - faren over"
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



    fun hentCoordinatesPolygon(feature: Features?): ArrayList<ArrayList<ArrayList<Double>>> {
        val geometry: Geometry? = feature?.geometry
        return when (geometry) {
            is Polygon -> geometry.coordinates
            else -> ArrayList()
        }

    }


    fun hentCoordinatesMultiPolygon(feature: Features?): ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> {
        val geometry: Geometry? = feature?.geometry
        return when (geometry) {
            is MultiPolygon -> geometry.coordinates
            else -> ArrayList()
        }

    }

    fun lagPolygon(coordinates: ArrayList<ArrayList<ArrayList<Double>>>): ArrayList<Point> {
        val listeMedPoints = ArrayList<Point>()
        for (ytterListe in coordinates) {
            for (indreListe in ytterListe) {
                val point = Point(indreListe[0], indreListe[1])
                listeMedPoints.add(point)
            }

        }
        return listeMedPoints


    }

    fun lagMultiPolygon(coordinates: ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>): ArrayList<ArrayList<Point>> {
        val multiPolygonPoints = ArrayList<ArrayList<Point>>()
        for (coordinatesPolygon in coordinates) {
            val polygonPoints = ArrayList<Point>()
            for (ytterListe in coordinatesPolygon) {
                for (indreListe in ytterListe) {
                    val point = Point(indreListe[0], indreListe[1])
                    polygonPoints.add(point)
                }
            }
            multiPolygonPoints.add(polygonPoints)
        }
        return multiPolygonPoints
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

//                    //sjekke om om punktet er over den øvre kanten av polygonet
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


    suspend fun henteVarselKort(point:Point): ArrayList<VarselKort>{
        val fareVarsler = arrayListOf<VarselKort>()
        val features: List<Features> = hentFeatures()
        features.forEach { feature ->
            val geometry: Geometry? = feature.geometry
            if(geometry!=null){
                when(geometry){
                    is Polygon -> {
                        val coordinates= hentCoordinatesPolygon(feature)
                        val polygon = lagPolygon(coordinates)
                        val gyldig= sjekkOmBrukerIPolygon(point, polygon)

                        if(gyldig){
                            lagKort(feature, fareVarsler)
                        }
                    }
                    is MultiPolygon ->{
                        val coordinates= hentCoordinatesMultiPolygon(feature)
                        val multiPolygon = lagMultiPolygon(coordinates)
                        val gyldig= sjekkOmBrukerIMultiPolygon(point, multiPolygon)
                        if(gyldig){
                            lagKort(feature, fareVarsler)
                        }
                    }
                }
            }

        }
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




fun main ()= runBlocking {
    val repo:metAlertsRepo = metAlertsRepo()
    val point = Point(16.8645, 69.3163)

    val features: List<Features> = repo.hentFeatures()
    val feature: Features? = features.getOrNull(1)
    val coordinates:ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> = repo.hentCoordinatesMultiPolygon(feature)
    val polygonen:ArrayList<ArrayList<Point>> = repo.lagMultiPolygon(coordinates)

    println(polygonen)


    if (repo.sjekkOmBrukerIMultiPolygon(point, polygonen)) {
        println("Point is inside the polygon")
    } else {
        println("Point is outside the polygon")
    }

}

