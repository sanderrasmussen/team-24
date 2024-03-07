package no.uio.ifi.IN2000.team24_app.data.locationForecast


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Details (

  @SerialName("precipitation_amount" ) var precipitationAmount : Double? = null

)