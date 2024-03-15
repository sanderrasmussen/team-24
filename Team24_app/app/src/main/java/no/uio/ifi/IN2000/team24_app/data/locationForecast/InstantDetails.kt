package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class InstantDetails(
    @SerialName("air_pressure_at_sea_level" ) var air_pressure_at_sea_level : Double? = null,
    @SerialName("air_temperature" ) var air_temperature : Double? = null,
    @SerialName("cloud_area_fraction" ) var cloud_area_fraction : Double? = null,
    @SerialName("relative_humidity" ) var relative_humidity : Double? = null,
    @SerialName("wind_from_direction" ) var wind_from_direction : Double? = null,
    @SerialName("wind_speed" ) var wind_speed : Double? = null

)