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

/**
 * This composable requests the location permission from the user.
 * @param context is the context of the activity
 * @param snackState is the state of the snackbar host, needed for the rationale popup.
 * @param permissionAction is the action to take when the permission is granted or denied
 * @see PermissionAction
 */

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermission(
    context: Context,
    snackState: SnackbarHostState,
    permissionAction: (PermissionAction) -> Unit
){
    val TAG = "LocationPermission"

    val locationPermissionState =
        rememberPermissionState(permission = Manifest.permission.ACCESS_COARSE_LOCATION) //first, check if permissions have already been granted.
    val permissionIsGranted by remember { mutableStateOf(locationPermissionState.status.isGranted) }
    Log.d(TAG, "Location permission is granted: $permissionIsGranted")
    if(permissionIsGranted) {//if it was already granted, we simply call the success action.
        permissionAction(PermissionAction.OnPermissionGranted)
        Log.d(TAG, "Location permission already granted")
        return
    }
    // we didn't have permission :( time to work
    //first, make the launcher we use later to invoke the callback, and call the supplied action.
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
    //check if we should show the rationale. Google documentation (google docs i guess?)
    // says that we should show the rationale if the user has previously denied the permission. this is up to their spec
    val showRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )


    if(showRationale){
        //showing rationale basically boils down to this: we show a snackbar with a message and an action button.
        //if the user clicks the action button, we launch the permission request.
        //if the user dismisses the snackbar, well, luckily we have a permission denied-action.
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
        //didn't even need to show the rationale, just launch the permission request right away.
        Log.d(TAG, "Launching permission request..")
        SideEffect {
            launcher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
    }
}

/**
 * This sealed class represents the action to take when the location permission is granted or denied.
 * @see LocationPermission
 */
sealed class PermissionAction {
    object OnPermissionGranted : PermissionAction()
    object OnPermissionDenied : PermissionAction()
}