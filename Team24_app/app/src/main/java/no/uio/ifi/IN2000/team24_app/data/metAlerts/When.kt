package no.uio.ifi.IN2000.team24_app.data.metAlerts

import com.google.gson.annotations.SerializedName

@Serializable
data class When (

  @SerializedName("interval" ) var interval : ArrayList<String> = arrayListOf()

)