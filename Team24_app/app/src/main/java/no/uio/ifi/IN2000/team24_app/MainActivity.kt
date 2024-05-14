package no.uio.ifi.IN2000.team24_app

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import no.uio.ifi.IN2000.team24_app.ui.home.HomeScreen
import no.uio.ifi.IN2000.team24_app.ui.store.StoreScreen
import no.uio.ifi.IN2000.team24_app.ui.theme.Team24_appTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Team24_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Controller()
                }
            }
        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun Controller() {
        val navController = rememberNavController()
        val context = LocalContext.current
        val isNetworkAvailable = isNetworkAvailable(context)

        NavHost(navController = navController, startDestination = "HomeScreen") {
            composable("HomeScreen") {
                HomeScreen(navController, isNetworkAvailable)
            }
            composable("StoreScreen") {
                StoreScreen(navController)
            }
        }

    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting

    }

}


