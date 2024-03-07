package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta (

  @SerialName("updated_at" ) var updatedAt : String? = null,
  @SerialName("units"      ) var units     : Units?  = Units()

)