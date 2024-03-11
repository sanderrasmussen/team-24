package no.uio.ifi.IN2000.team24_app.data.location

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient

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
        //TODO actually get location
    }
}