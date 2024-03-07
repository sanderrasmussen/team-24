package no.uio.ifi.IN2000.team24_app.data.locationForecast


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Timeseries (

  @SerialName("time" ) var time : String? = null,
  @SerialName("data" ) var data : Data?   = Data()

)