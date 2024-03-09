package no.uio.ifi.IN2000.team24_app.data.metAlerts

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

class MetAlertsDataSource {

    suspend fun getMetAlertData(): MetAlerts?{
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

        var alert: MetAlerts? = null;



        try {
            val URL = "https://api.met.no/weatherapi/metalerts/2.0/current.json"
            val response: HttpResponse =
                client.get(URL)
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

fun main ()= runBlocking {
    val alert:MetAlertsDataSource= MetAlertsDataSource()
    print(alert.getMetAlertData())

}