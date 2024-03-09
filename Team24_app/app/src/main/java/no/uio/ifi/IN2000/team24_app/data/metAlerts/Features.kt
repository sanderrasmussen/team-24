package no.uio.ifi.IN2000.team24_app.data.metAlerts

import com.google.gson.annotations.SerializedName

@Serializable
data class Features (

  @SerializedName("geometry"   ) var geometry   : Geometry?   = Geometry(),
  @SerializedName("properties" ) var properties : Properties? = Properties(),
  @SerializedName("type"       ) var type       : String?     = null,
  @SerializedName("when"       ) var when       : When?       = When()

)