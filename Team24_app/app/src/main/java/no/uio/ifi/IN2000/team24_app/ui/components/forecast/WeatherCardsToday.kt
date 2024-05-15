package no.uio.ifi.IN2000.team24_app.ui.components.forecast

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.IN2000.team24_app.ui.home.HomeScreenViewModel

/**
 * This composable displays the weather cards for today. It displays details from the current hour to 23, hourly.
 * It uses the [WeatherCard] composable to display the weather details for each hour.
 * @param vm the [HomeScreenViewModel] to get the weather details from
 * @see WeatherCard
 */
@Composable
fun WeatherCardsToday(vm: HomeScreenViewModel) {
    val weatherDetails by vm.currentWeatherState.collectAsState()
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(2.dp),

        ) {
        weatherDetails.forEachIndexed { index, weatherDetail ->
            WeatherCard(
                weatherDetail = weatherDetail,
                onClick = { vm.updateWeatherDetails(weatherDetail) },
            )
        }
    }
}