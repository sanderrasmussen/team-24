package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Geometry (

  @SerialName("type"        ) var type        : String?           = null,
  @SerialName("coordinates" ) var coordinates : ArrayList<Double> = arrayListOf()

)