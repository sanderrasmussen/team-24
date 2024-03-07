package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Properties (

  @SerialName("meta"       ) var meta       : Meta?                 = Meta(),
  @SerialName("timeseries" ) var timeseries : ArrayList<Timeseries> = arrayListOf()

)