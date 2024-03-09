package no.uio.ifi.IN2000.team24_app.data.metAlerts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class When (

  @SerialName("interval" ) var interval : ArrayList<String> = arrayListOf()

)