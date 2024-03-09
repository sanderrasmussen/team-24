package no.uio.ifi.IN2000.team24_app.data.metAlerts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Geometry (

  @SerialName("coordinates" ) var coordinates : ArrayList<ArrayList<ArrayList<Double>>> = arrayListOf(),
  @SerialName("type"        ) var type        : String?                                 = null

)