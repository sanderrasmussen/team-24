package no.uio.ifi.IN2000.team24_app.data.locationForecast

import no.uio.ifi.IN2000.team24_app.data.locationForecast.Next12Hours
import no.uio.ifi.IN2000.team24_app.data.locationForecast.Next1Hours
import no.uio.ifi.IN2000.team24_app.data.locationForecast.Next6Hours
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Data (

  @SerialName("instant"       ) var instant     : Instant?     = Instant(),
  @SerialName("next_12_hours" ) var next12Hours : Next12Hours? = Next12Hours(),
  @SerialName("next_1_hours"  ) var next1Hours  : Next1Hours?  = Next1Hours(),
  @SerialName("next_6_hours"  ) var next6Hours  : Next6Hours?  = Next6Hours()

)