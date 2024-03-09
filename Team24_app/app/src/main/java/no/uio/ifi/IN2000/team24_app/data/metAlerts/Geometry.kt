package no.uio.ifi.IN2000.team24_app.data.metAlerts

import com.google.gson.annotations.SerializedName

@Serializable
data class Geometry (

  @SerializedName("coordinates" ) var coordinates : ArrayList<ArrayList<ArrayList<Double>>> = arrayListOf(),
  @SerializedName("type"        ) var type        : String?                                 = null

)