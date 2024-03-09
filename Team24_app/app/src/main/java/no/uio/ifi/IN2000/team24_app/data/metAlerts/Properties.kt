package no.uio.ifi.IN2000.team24_app.data.metAlerts

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Properties (

  @SerialName("area"                 ) var area                 : String?              = null,
  @SerialName("awarenessResponse"    ) var awarenessResponse    : String?              = null,
  @SerialName("awarenessSeriousness" ) var awarenessSeriousness : String?              = null,
  @SerialName("awareness_level"      ) var awarenessLevel       : String?              = null,
  @SerialName("awareness_type"       ) var awarenessType        : String?              = null,
  @SerialName("certainty"            ) var certainty            : String?              = null,
  @SerialName("consequences"         ) var consequences         : String?              = null,
  @SerialName("county"               ) var county               : ArrayList<String>    = arrayListOf(),
  @SerialName("description"          ) var description          : String?              = null,
  @SerialName("event"                ) var event                : String?              = null,
  @SerialName("eventAwarenessName"   ) var eventAwarenessName   : String?              = null,
  @SerialName("geographicDomain"     ) var geographicDomain     : String?              = null,
  @SerialName("id"                   ) var id                   : String?              = null,
  @SerialName("instruction"          ) var instruction          : String?              = null,
  @SerialName("resources"            ) var resources            : ArrayList<Resources> = arrayListOf(),
  @SerialName("riskMatrixColor"      ) var riskMatrixColor      : String?              = null,
  @SerialName("severity"             ) var severity             : String?              = null,
  @SerialName("title"                ) var title                : String?              = null,
  @SerialName("triggerLevel"         ) var triggerLevel         : String?              = null,
  @SerialName("type"                 ) var type                 : String?              = null

)