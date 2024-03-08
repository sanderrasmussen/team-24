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
    suspend fun getLocation(): Location?{

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager


        //TODO actually get location
    }
}