package no.uio.ifi.IN2000.team24_app.data.metAlerts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Resources (

  @SerialName("description" ) var description : String? = null,
  @SerialName("mimeType"    ) var mimeType    : String? = null,
  @SerialName("uri"         ) var uri         : String? = null

)