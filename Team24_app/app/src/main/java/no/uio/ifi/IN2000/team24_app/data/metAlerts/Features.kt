package no.uio.ifi.IN2000.team24_app.data.metAlerts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Features (

  @SerialName("geometry"   ) var geometry   : Geometry?   = Geometry(),
  @SerialName("properties" ) var properties : Properties? = Properties(),
  @SerialName("type"       ) var type       : String?     = null,
  @SerialName("when"       ) var wen       : When?       = When()

)