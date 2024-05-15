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
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

class MetAlertsDataSource(
    private val TAG: String = "MetAlertsDataSource",
    private var metAlerts: MetAlerts? = null,
    private val testSource:Boolean = false
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
                            polymorphic(Geometry::class) {
                                subclass(Polygon::class, Polygon.serializer())
                                subclass(MultiPolygon::class, MultiPolygon.serializer())
                            }
                        }
                    })
            }
                defaultRequest {
                    url("https://gw-uio.intark.uh-it.no/in2000/")
                    headers.appendIfNameAbsent(
                        "X-Gravitee-API-Key",
                        "bf1a1f0d-b408-466f-a742-027cddf58404"
                    )
                }
            }


        var alert: MetAlerts? = null;
        try {
            //the test-url allows for the use of a test-endpoint, for unit tests
            val URL = if (!testSource) "weatherapi/metalerts/2.0//all.json?lat=$latitude&lon=$Longitude" else "weatherapi/metalerts/2.0/example.json"
            //Log.d(TAG, "Getting metalerts data")
            val response: HttpResponse = client.get(URL)
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

