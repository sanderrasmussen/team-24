package no.uio.ifi.IN2000.team24_app.ui.components.forecast

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import no.uio.ifi.IN2000.team24_app.ui.Icon
import no.uio.ifi.IN2000.team24_app.ui.backgroundColour
import no.uio.ifi.IN2000.team24_app.ui.home.HomeScreenViewModel
import no.uio.ifi.IN2000.team24_app.ui.home.WeatherDetailsUiState


/**
 * This function converts degrees to a wind direction. It has 8 directions as precision.
 * @param degrees the degrees to convert
 * @return the wind direction as a string
 */
private fun windDirection(degrees: Double?): String {
    if(degrees == null) return "ukjent"
    val directions = arrayOf("N", "NØ", "Ø", "SØ", "S", "SV", "V", "NV")
    val index = ((degrees + 22.5) / 45).toInt() % 8
    return directions[index]
}

    /**
    * This composable displays a card with detailed weather information.
    * @param vm is the viewmodel for the home screen
    * @param modifier is the modifier for the card itself
    */
@Composable
fun WeatherDetailCard(vm: HomeScreenViewModel, modifier: Modifier = Modifier,){
    //TODO the units are hardcoded as string-values, but could well change from the API.
    //TODO the endpoint does take this into account, but it is discarded in the repo??. needs to be passed to viewModel?
    val weatherDetailState by vm.weatherDetails.collectAsState()
    if (weatherDetailState.weatherDetails != null) {
        val weatherDetails: List<WeatherDetails> = weatherDetailState.weatherDetails!!
    Dialog(
        onDismissRequest = { vm.updateWeatherDetails(null) },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        ) {
            val dayStr = weatherDetailState.dayStr
            Card(
                modifier = modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = backgroundColour())

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { vm.updateWeatherDetails(null) },
                            modifier = Modifier
                                .padding(4.dp)
                                .width(24.dp)
                                .height(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "lukk dialog",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    if (dayStr == null) {  //this is for today
                        val weatherDetail = weatherDetails[0]
                        Icon(iconName = weatherDetail.next_1_hours_symbol_code, size = 65)
                        Text(
                            text = "Detaljer for kl. ${weatherDetail.time}",
                            fontSize = 26.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Temperatur: ${weatherDetail.air_temperature}°C",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Nedbørsmengde: ${weatherDetail.next_1_hours_precipitation_amount}mm",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Skyer: ${weatherDetail.cloud_area_fraction}% dekning",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Vindstyrke: ${weatherDetail.wind_speed}m/s",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Text(
                            text = "Vindretning: ${windDirection(weatherDetail.wind_from_direction)}",
                            fontSize = 22.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.padding(18.dp))


                    } else { //this is one of the next6days-cards
                        val scrollState = rememberScrollState()
                        LaunchedEffect(Unit) { scrollState.animateScrollTo(0) } //on first compose, scroll to top
                        Text(text = "Detaljer for $dayStr", fontSize = 26.sp, color = Color.White)
                        Column(
                            modifier = Modifier
                                .height(200.dp)
                                .verticalScroll(scrollState)
                        ) {
                            weatherDetails.forEach { hourlyDetail ->
                                Row(
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = "Kl. ${hourlyDetail.time}: ${hourlyDetail.air_temperature}°C  ",
                                        fontSize = 22.sp,
                                        color = Color.White
                                    )
                                    val symbolCode =
                                        if (hourlyDetail.next_1_hours_symbol_code != null) hourlyDetail.next_1_hours_symbol_code else hourlyDetail.next_6_hours_symbol_code
                                    Icon(iconName = symbolCode, size = 35)
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}