package no.uio.ifi.IN2000.team24_app.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task


class LocationTracker(
    val context: Context,

    ){
    private val TAG:String ="LocationTracker"

    private fun showGPSNotEnabledDialog(context: Context) {
        Toast.makeText(
            context,
            "NO GPS PROVIDER ENABLED!",
            Toast.LENGTH_SHORT
        ).show()
    }

    //if this returns null, remember to create a default
    /**
     * This method is used to get the current location of the user. It will return a Task<Location?>, which can be used to get the location of the user.
     * This method will NOT request the permission to access the location, this should be done before calling this method.
     * @return Task<Location?>: a task that will return the location of the user on success or null on failure. The null/failure should be handled by the caller.
     */
    fun getLocation() : Task<Location?> {
        //first, check if we have permission to access the coarse location. no need to try fine location, the forecast isn't that granular anyway
        //the way control flow is set up, this will always be granted. This method is only called AFTER the permission is granted
        val cancellationToken : CancellationToken = CancellationTokenSource().token     //used to cancel the request, i think this happens on navigate or recompose(when caller is destroyed)
        val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        //this makes the compiler happy - I know that the permission is granted, but the compiler doesn't
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
            Log.e(TAG, "no location permission (THIS SHOULDN'T EVER HAPPEN, IF YOU READ THIS LOG ASYNC HAS BECOME DESYNC)")
            //this should be null, return null as missing permission
            return fusedLocationClient.lastLocation
        }

            val locationManager : LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                showGPSNotEnabledDialog(context)
                Log.e(TAG, "no location provider enabled!")
                //this will probably still be null, handled on the outside
                return fusedLocationClient.lastLocation
            }

        /**
         * This is the event listener that will be returned to the caller.
         *Note that successListener may return a null-value due to the nature of the fusedLocationClient, which is why the caller should handle this.
         */
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