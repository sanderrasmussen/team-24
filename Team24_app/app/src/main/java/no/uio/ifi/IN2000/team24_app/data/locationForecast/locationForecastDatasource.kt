package no.uio.ifi.IN2000.team24_app.data.locationForecast

import android.location.Location
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

class locationForecastDatasource {

    suspend fun getLocationForecastData(lat:Double, lon: Double): location_forecast?{
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

        var forecast: location_forecast? = null;



        try {
   var URL = "https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=${lat}&lon=${lon}"
            val response: HttpResponse =
                client.get(URL)
            println(response.status)
            if (response.status.isSuccess()) {
                   val content: location_forecast = response.body();
                forecast = content;


            }

            println(forecast)
        }
        catch(e: Exception){
          e.printStackTrace()
        }

     finally {
        client.close()
    }
        return forecast

    }




}