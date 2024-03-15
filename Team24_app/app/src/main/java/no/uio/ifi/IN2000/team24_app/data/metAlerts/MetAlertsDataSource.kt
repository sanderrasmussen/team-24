package no.uio.ifi.IN2000.team24_app.data.metAlerts

import android.util.Log
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
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class MetAlertsDataSource(
    private val TAG:String = "MetAlertsDataSource"
) {

    suspend fun getMetAlertData(): MetAlerts?{
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
                }
                )
            }
        }

        var alert: MetAlerts? = null;



        try {
            //! THIS URL IS ONLY HERE TO TEST THE MULTIPOLYGON-PROBLEM
            //val TESTURL = "https://api.met.no/weatherapi/metalerts/2.0/test.json"
            val URL = "https://api.met.no/weatherapi/metalerts/2.0/current.json"
            val response: HttpResponse =
                client.get(URL)
            Log.d(TAG, response.status.toString())
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