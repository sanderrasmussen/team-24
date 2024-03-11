package no.uio.ifi.IN2000.team24_app.data.metAlerts

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

@Polymorphic
@Serializable
sealed class Geometry(){
}

@Serializable
@SerialName("Polygon")
data class Polygon (

  @SerialName("coordinates" ) var coordinates : ArrayList<ArrayList<ArrayList<Double>>> = arrayListOf(),
  //@SerialName("type"        ) override var type        : String?                                 = null

): Geometry()

@Serializable
@SerialName("MultiPolygon")
data class MultiPolygon(
  @SerialName("coordinates" ) var coordinates : ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> = arrayListOf(),
  //@SerialName("type"        ) override var type        : String?                                 = null

): Geometry()