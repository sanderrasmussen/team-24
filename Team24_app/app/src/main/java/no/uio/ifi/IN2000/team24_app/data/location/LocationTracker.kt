package no.uio.ifi.IN2000.team24_app.data.location

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationTracker(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val application: Application
){
    //if this returns null, remember to create a default
    suspend fun getLocation(): Location?{

        //first, check if we have permission to access the coarse location. no need to try fine location, the forecast isn't that granular anyway
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED


        val locationManager = application.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        //check if we have some way of accessing gps: cellular or network
        val isGpsEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        //if we can't get gps or coarse location, give up (handle error on received null in middleman-class
        if(!isGpsEnabled && !hasAccessCoarseLocationPermission){
            return null
        }

        //yeah, this looks a bit complex, took me a while to wrap my head around the thing but i trust it now
        //this allows for suspension of the coroutine while the lastlocation-api responds
        return suspendCancellableCoroutine { continuation->
            fusedLocationClient.lastLocation.apply {
                if(isComplete){ //if the lastLocation() is complete
                    if(isSuccessful){
                        continuation.resume(result) //if the task was a success we return the result of lastLocation(the location object)
                    }else{
                        continuation.resume(null)   //otherwise we go back with a null to be handled elsewhere
                    }
                    return@suspendCancellableCoroutine  //like a goto - specifies which point to return to. An ordinary return would return to apply{}, but we don't need listeners as we were already successfull
                }
                //waiting because the lastLocation()-call wasn't complete yet
                addOnSuccessListener { continuation.resume(it) }    //if it was a success, resume processing with the response(location object)
                addOnFailureListener{ continuation.resume(null) }   //again otherwise, null
                addOnCanceledListener { continuation.cancel() } //if the api-call is canceled externally, we cancel this coroutine
            }
        }
    }
}