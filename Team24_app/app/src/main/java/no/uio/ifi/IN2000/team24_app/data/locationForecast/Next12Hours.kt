package no.uio.ifi.IN2000.team24_app.data.locationForecast


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Next12Hours (

  @SerialName("summary" ) var summary : Summary? = Summary(),
  @SerialName("details" ) var details : Details? = Details()

)