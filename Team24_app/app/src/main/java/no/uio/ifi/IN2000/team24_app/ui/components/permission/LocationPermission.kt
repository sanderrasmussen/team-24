package no.uio.ifi.IN2000.team24_app.ui.components.permission

import android.Manifest
import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(
    context: Context,
    snackState: SnackbarHostState,
    permissionAction: (PermissionAction) -> Unit
){
    val TAG = "LocationPermission"

    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_COARSE_LOCATION)
    val permissionIsGranted by remember { mutableStateOf(locationPermissionState.status.isGranted) }
    Log.d(TAG, "Location permission is granted: $permissionIsGranted")
    if(permissionIsGranted) {
        permissionAction(PermissionAction.OnPermissionGranted)
        Log.d(TAG, "Location permission already granted")
        return
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted : Boolean ->
        if (isGranted) {
            Log.d(TAG, "Location permission granted")
            permissionAction(PermissionAction.OnPermissionGranted)
        } else {
            Log.d(TAG, "Location permission denied")
            permissionAction(PermissionAction.OnPermissionDenied)
        }
    }

    val showRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )


    if(showRationale){
        Log.d(TAG, "Showing permission rationale")
        LaunchedEffect(showRationale) {

            val snackbarResult = snackState.showSnackbar(
                message = "For å kunne vise deg været trenger vi tilgang til din posisjon.",
                actionLabel = "Gi tilgang",
                duration = SnackbarDuration.Indefinite

            )
            when (snackbarResult) {
                SnackbarResult.Dismissed -> {
                    Log.d(TAG, "User dismissed permission rationale")
                    //User denied the permission, do nothing
                    permissionAction(PermissionAction.OnPermissionDenied)
                }
                SnackbarResult.ActionPerformed -> {
                    Log.d(TAG, "User granted permission with rationale. Launching permission request..")
                    launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
                }
            }
        }
    } else{
        Log.d(TAG, "Launching permission request..")
        SideEffect {
            launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}

sealed class PermissionAction {
    object OnPermissionGranted : PermissionAction()
    object OnPermissionDenied : PermissionAction()
}