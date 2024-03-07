package no.uio.ifi.IN2000.team24_app.data.locationForecast

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Units (

  @SerialName("air_pressure_at_sea_level" ) var airPressureAtSeaLevel : String? = null,
  @SerialName("air_temperature"           ) var airTemperature        : String? = null,
  @SerialName("cloud_area_fraction"       ) var cloudAreaFraction     : String? = null,
  @SerialName("precipitation_amount"      ) var precipitationAmount   : String? = null,
  @SerialName("relative_humidity"         ) var relativeHumidity      : String? = null,
  @SerialName("wind_from_direction"       ) var windFromDirection     : String? = null,
  @SerialName("wind_speed"                ) var windSpeed             : String? = null

)