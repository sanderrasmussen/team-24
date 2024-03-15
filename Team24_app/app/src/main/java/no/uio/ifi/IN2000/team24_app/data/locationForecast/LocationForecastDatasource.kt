package no.uio.ifi.IN2000.team24_app.data.locationForecast

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class LocationForecastDatasource (
    private val TAG:String ="LocationForecastDatasource"
){

    suspend fun getLocationForecastData(lat:Double, lon: Double): LocationForecast?{
        val client = HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    prettyPrint = true
                    encodeDefaults = true
                })
            }
        }

        var forecast: LocationForecast? = null;



        try {
   val URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
            val response: HttpResponse =
                client.get(URL)
            Log.d(TAG, response.status.toString())
            if (response.status.isSuccess()) {
                   val content: LocationForecast = response.body();
                forecast = content;


            }

        }
        catch(e: Exception){    //TODO better exception handling
          e.printStackTrace()
        }

     finally {
        client.close()
    }
        return forecast
    }




}