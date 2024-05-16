package no.uio.ifi.IN2000.team24_app.data.metAlerts

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

/**
 * This class is responsible for fetching the metalerts data from the metalerts API.
 * It uses the Ktor HTTP client to make a GET request to the API, and then parses the response
 * into a MetAlerts object.
 *
 * @param TAG: The tag used for logging
 * @param metAlerts: The MetAlerts object that is fetched from the API
 * @param testSource: A boolean that determines whether to use the test endpoint or the real endpoint
 */
class MetAlertsDataSource(
    private val TAG: String = "MetAlertsDataSource",
    private var metAlerts: MetAlerts? = null,
    private val testSource:Boolean = false
) {
    /**
     * This function fetches the metalerts data from the API.
     * It uses the Ktor HTTP client to make a GET request to the API, and then parses the response
     * into a MetAlerts object.
     *
     * @param latitude: The latitude of the location to fetch metalerts data for
     * @param Longitude: The longitude of the location to fetch metalerts data for
     * @return The MetAlerts object that is fetched from the API on success, or null on failure
     * @throws Exception if the request fails
     * @see MetAlerts
     */
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

