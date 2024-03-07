package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class location_forecast (

  @SerialName("type"       ) var type       : String?     = null,
  @SerialName("geometry"   ) var geometry   : Geometry?   = Geometry(),
  @SerialName("properties" ) var properties : Properties? = Properties()

)