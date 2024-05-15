package no.uio.ifi.IN2000.team24_app.data.locationForecast


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

/**
 * Datasource for fetching location forecast data from the met.no API
 *
 */
class mockLocationForecastDatasource (
    private val TAG:String ="LocationForecastDatasource",
    private var forecast: LocationForecast? = null
){
    /**
     * Fetches location forecast data from the met.no API
     *
     * @param lat: Latitude of the location
     * @param lon: Longitude of the location
     * @return LocationForecast: The location forecast data
     * @throws ApiAccessException: If there is a server error on the API side
     * @see ApiAccessException
     */
    @Throws(ApiAccessException::class)
    suspend fun getLocationForecastData(lat:Double, lon: Double): LocationForecast?{
        if(forecast != null){
            return forecast
        }
        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    encodeDefaults = true
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
        var forecastResponse: LocationForecast? = null;
        try {
            //Log.d(TAG, "Getting location forecast data")
            val response: HttpResponse =
                client.get("weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}")
            //Log.d(TAG, response.status.toString())
            if (response.status.isSuccess()) {
                val content: LocationForecast = response.body();
                forecastResponse = content;
            }
            else if(response.status.value in 500..599){
                throw ApiAccessException("Server error")
            }
            else{
                throw Exception("Failed to get location forecast data") //TODO better
            }
        }
        catch(e: ApiAccessException){
            throw e
        }
        catch(e: Exception){
            e.printStackTrace()
            throw e
        }
        finally {
            client.close()
        }
        forecast = forecastResponse
        return forecast
    }
}
/**
 * Exception class for API access errors
 *
 * @param msg: The error message
 */
class ApiAccessException(msg:String) : Exception() {
    override val message: String = msg
}