package no.uio.ifi.IN2000.team24_app.data.location

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationTracker(
    private val context: Context,

    ){
    private val TAG:String ="LocationTracker"

    private val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    //var userLocation
    //if this returns null, remember to create a default
    suspend fun getLocation(): Location?{

        //first, check if we have permission to access the coarse location. no need to try fine location, the forecast isn't that granular anyway
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED




        val locationManager = context.getSystemService(
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
        Log.d(TAG, "had coarsePermissions: ${hasAccessCoarseLocationPermission}")

        var location : Location? = null
        fusedLocationClient.lastLocation.addOnSuccessListener {
            Log.d(TAG, "in onSuccess w/ it: $it")
            location = it
        }
        fusedLocationClient.lastLocation.addOnFailureListener{
            Log.d(TAG, "in onFailure")

        }
        return location
    }
}