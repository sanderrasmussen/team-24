package no.uio.ifi.IN2000.team24_app.data.metAlerts


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetAlerts (

  @SerialName("features"   ) var features   : ArrayList<Features> = arrayListOf(),
  @SerialName("lang"       ) var lang       : String?             = null,
  @SerialName("lastChange" ) var lastChange : String?             = null,
  @SerialName("type"       ) var type       : String?             = null

)