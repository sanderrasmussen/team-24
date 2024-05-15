package no.uio.ifi.IN2000.team24_app.data.metAlerts.metAlertsRepository

import android.annotation.SuppressLint
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Features
import no.uio.ifi.IN2000.team24_app.data.metAlerts.Geometry
import no.uio.ifi.IN2000.team24_app.data.metAlerts.MetAlertsDataSource
import no.uio.ifi.IN2000.team24_app.data.metAlerts.WarningCard
import java.text.SimpleDateFormat
import java.util.Date



class MetAlertsRepo (
    val dataSource: MetAlertsDataSource = MetAlertsDataSource()
) {
    /**
     * Returns the interval of the warning as a list of two strings; start and end time.
     * @param feature: The feature to get the interval from.
     * @return List of two strings; start and end time.
     */
    fun getInterval(feature: Features): List<String> {
        return feature.wen?.interval ?: emptyList()
    }
    /**
     * Returns the ongoing status of the warning, based on the interval of the warning supplied in the same format as [getInterval].
     * @param interval: The interval of the warning.
     * @return String representing the ongoing status of the warning. Either "Pågår", "Ventes" or "Ferdig".
     * @see getInterval
     */
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


    /**
     * Returns the danger level of the warning based on the awareness level of the warning.
     * @param awarenessLevel: The awareness level of the warning, in the format supplied by the MET-api.
     * @return String representing the danger level of the warning. Either "Gult nivå", "Oransje nivå" or "Rødt nivå".
     * @see getColour
     */
    fun getDangerLevelFromAwarenessLevel(awarenessLevel: String?): String? {
        val level = getColour(awarenessLevel)

        return when (level) {
            "yellow" -> "Gult nivå"
            "orange" -> "Oransje nivå"
            "red" -> "Rødt nivå"
            else -> null
        }
    }

    /**
     * Returns the colour of the warning based on the awareness level of the warning.
     * @param awarenessLevel: The awareness level of the warning, in the format supplied by the MET-api.
     * @return String representing the colour of the warning. Either "yellow", "orange" or "red".
     */
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

    /**
     * Returns the icon id of the warning based on the event of the warning.
     * @param event: The event of the warning, in the format supplied by the MET-api.
     * @return String representing the icon id of the warning. This, with an associated colour as postfix, is the drawable id of the icon.
     */
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


    /**
     * Returns a list of warning cards based on the latitude and longitude supplied. Only warnings that are ongoing or upcoming are included.
     * @param latitude: The latitude of the location to get the warnings for.
     * @param longitude: The longitude of the location to get the warnings for.
     * @return List of warning cards.
     * @see WarningCard
     */
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
        return warnings
    }

    /**
     * Creates a warning card based on the feature supplied and adds it to the list of warnings if it hasn't already passed.
     * @param feature: The feature to create the warning card from.
     * @param warnings: The list of warnings to add the warning card to.
     * @see WarningCard
     */
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