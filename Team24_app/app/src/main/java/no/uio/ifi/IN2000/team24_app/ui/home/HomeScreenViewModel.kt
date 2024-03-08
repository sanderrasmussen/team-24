package no.uio.ifi.IN2000.team24_app.ui.home

import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices


class HomeScreenViewModel(private val fusedLocationClient: FusedLocationProviderClient){

    private fun getPosition(){
        fusedLocationClient.lastLocation.addOnSuccessListener {location->
            //can in rare cases be null
        }
    }
}