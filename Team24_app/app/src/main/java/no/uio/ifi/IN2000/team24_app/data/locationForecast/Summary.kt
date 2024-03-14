package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary (
  @SerialName("details" ) var details : SummaryDetails = SummaryDetails()
  @SerialName("symbol_code" ) var symbolCode : String? = null

)