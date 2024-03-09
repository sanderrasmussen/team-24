package com.example.examplepackage no.uio.ifi.IN2000.team24_app.data.metAlerts
import com.google.gson.annotations.SerializedName

@Serializable
data class Resources (

  @SerializedName("description" ) var description : String? = null,
  @SerializedName("mimeType"    ) var mimeType    : String? = null,
  @SerializedName("uri"         ) var uri         : String? = null

)