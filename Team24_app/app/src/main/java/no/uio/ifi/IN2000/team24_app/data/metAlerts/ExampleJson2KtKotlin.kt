package no.uio.ifi.IN2000.team24_app.data.metAlerts

import com.google.gson.annotations.SerializedName

@Serializable
data class ExampleJson2KtKotlin (

  @SerializedName("features"   ) var features   : ArrayList<Features> = arrayListOf(),
  @SerializedName("lang"       ) var lang       : String?             = null,
  @SerializedName("lastChange" ) var lastChange : String?             = null,
  @SerializedName("type"       ) var type       : String?             = null

)