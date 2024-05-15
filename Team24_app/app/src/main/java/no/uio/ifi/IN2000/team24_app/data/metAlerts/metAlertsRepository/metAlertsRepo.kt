package no.uio.ifi.IN2000.team24_app.data.metAlerts.metAlertsRepository

import android.annotation.SuppressLint
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Features
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Geometry
import no.uio.ifi.IN2000.team24_app.data.metAlerts.MetAlertsDataSource
import no.uio.ifi.IN2000.team24_app.data.metAlerts.WarningCard
import java.text.SimpleDateFormat
import java.util.Date



class MetAlertsRepo {
    val dataSource: MetAlertsDataSource = MetAlertsDataSource()
    fun getInterval(feature: Features): List<String> {
        return feature.wen?.interval ?: emptyList()
    }

    @SuppressLint("SimpleDateFormat")
    fun ongoingWarning(interval: List<String>): String {
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


    fun getDangerLevelFromAwarenessLevel(awarenessLevel: String?): String? {
        val awareness = awarenessLevel?.split(";")
        var level: String? = null

        if (awareness != null) {
            if (awareness.size > 1) {
                level = awareness[1].trim()
            }
        }

        return when (level) {
            "yellow" -> "Gult nivå"
            "orange" -> "Oransje nivå"
            "red" -> "Rødt nivå"
            else -> null
        }


    }

    fun getColour(awarenessLevel: String?):String?{
        val awareness = awarenessLevel?.split(";")
        var level: String? = null

        if (awareness != null) {
            if (awareness.size > 1) {
                level = awareness[1].trim()
            }
        }
        return level

    }

    fun getIconId(event: String?): String {
        return when (event) {
            "avalanches" -> "icon_warning_avalanches"
            "blowingSnow" -> "icon_warning_snow"
            "drivingConditions" -> "icon_warning_drivingconditions"
            "flood" -> "icon_warning_flood"
            "forestFire" -> "icon_warning_forestfire"
            "gale" -> "icon_warning_wind"
            "ice" -> "icon_warning_ice"
            "icing" -> "icon_warning_generic"
            "landslide" -> "icon_warning_landslide"
            "polarLow" -> "icon_warning_polarlow"
            "rain" -> "icon_warning_rain"
            "rainFlood" -> "icon_warning_rainflood"
            "snow" -> "icon_warning_snow"
            "stormSurge" -> "icon_warning_stormsurge"
            "lightning" -> "icon_warning_lightning"
            "wind" -> "icon_warning_wind"
            else -> "icon_warning_generic"
        }
    }


    suspend fun getWarningCards(latitude:Double, longitude:Double): ArrayList<WarningCard> {
        val warnings = arrayListOf<WarningCard>()
        val features: List<Features> =
            dataSource.getMetAlertData(latitude, longitude)?.features ?: listOf()
        features.forEach { feature ->
            val geometry: Geometry? = feature.geometry
            if (geometry != null) {

                makeCard(feature, warnings)
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
        return warnings
    }

    fun makeCard(feature:Features, warnings: ArrayList<WarningCard> ){
        val interval = getInterval(feature)
        val colour = getColour(feature.properties?.awarenessLevel)
        val ongoingDanger = ongoingWarning(interval)
        val imageURL = "${getIconId(feature.properties?.event)}_$colour"
        val location = feature.properties?.area
        val riskLevel = getDangerLevelFromAwarenessLevel(feature.properties?.awarenessLevel)
        if(location!= null && riskLevel != null){
            val warningCard= WarningCard(ongoingDanger, imageURL, location, riskLevel)
            if(ongoingDanger != "Ferdig") {    //simple way to remove the warnings that have passed. no param at endpoint for this.
                warnings.add(warningCard)
            }
        }
    }
}