package no.uio.ifi.IN2000.team24_app.data.metAlerts

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.appendIfNameAbsent
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class MetAlertsDataSource(
    private val TAG: String = "MetAlertsDataSource",
    private var metAlerts: MetAlerts? = null
) {

    suspend fun getMetAlertData(latitude: Double, Longitude: Double): MetAlerts?{
        if(metAlerts != null){
            return metAlerts        //added caching to avoid recalling on the recompose bug
        }
        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(
                    Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    encodeDefaults = true
                        serializersModule = SerializersModule {
                            polymorphic(Geometry::class){
                                subclass(Polygon::class, Polygon.serializer())
                                subclass(MultiPolygon::class, MultiPolygon.serializer())
                            }
                        }
                })
                defaultRequest {
                    url("https://gw-uio.intark.uh-it.no/in2000/")
                    headers.appendIfNameAbsent(
                        "X-Gravitee-API-Key",
                        "bf1a1f0d-b408-466f-a742-027cddf58404"
                    )
                }
            }
        }

        var alert: MetAlerts? = null;
        try {
            //! THIS URL IS ONLY HERE TO TEST THE MULTIPOLYGON-PROBLEM
            //val TESTURL = "https://api.met.no/weatherapi/metalerts/2.0/test.json"
            Log.d(TAG, "Getting metalerts data")
            val response: HttpResponse =
                client.get("weatherapi/metalerts/2.0//all.json?lat=$latitude&lon=$Longitude")
            println(response.status)
            if (response.status.isSuccess()) {
                val content: MetAlerts = response.body();
                alert = content;
            }
        }
        catch(e: Exception){
            e.printStackTrace()
        }

        finally {
            client.close()
        }
        return alert
    }
}

