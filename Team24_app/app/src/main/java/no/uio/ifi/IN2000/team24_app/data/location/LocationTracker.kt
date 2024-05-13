package no.uio.ifi.IN2000.team24_app.data.location

import android.Manifest
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class LocationTracker(
    val context: Context,

    ){
    private val TAG:String ="LocationTracker"

    //var userLocation
    //if this returns null, remember to create a default
    fun getLocation() : Task<Location?> {
        //first, check if we have permission to access the coarse location. no need to try fine location, the forecast isn't that granular anyway

        var userLocation : Location? = null
        val cancellationToken : CancellationToken = CancellationTokenSource().token
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            //this should be null, return null as missing permission
            return fusedLocationClient.lastLocation
        }
        val eventListener = fusedLocationClient.getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cancellationToken)
            .addOnSuccessListener { location->
                Log.d(TAG, "in onSuccessListener w/ lotation: $location")
                if(location==null){
                    Log.e(TAG, "successListener for getCurrentLocation recieved null")
                }
            }
            .addOnFailureListener { e->
                Log.e(TAG, "failed to get location: ${e.message}")
            }
        Log.d(TAG, "locationTracker done")
        return eventListener
    }
}