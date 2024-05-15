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
import no.uio.ifi.IN2000.team24_app.ui.day
import no.uio.ifi.IN2000.team24_app.ui.getNextSixDays
import no.uio.ifi.IN2000.team24_app.ui.home.HomeScreenViewModel

/**
 * This composable displays the weather cards for the next six days.
 * It uses the [WeatherCard] composable to display the weather details for each day.
 * @param vm the viewmodel to get the weather details from
 * @see WeatherCard
 */
@Composable
fun WeatherCardsNextSixDays(vm: HomeScreenViewModel) {
    val next6DaysWeatherState by vm.next6DaysState.collectAsState()
    val days = getNextSixDays()
    val scrollState = rememberScrollState()
    val today = day()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        days.forEach { day ->
            if (today != null && next6DaysWeatherState != null) {
                val index = days.indexOf(day)
                val weatherDetails = next6DaysWeatherState!![index]
                if (weatherDetails != null) {
                    WeatherCard(
                        weatherDetail = weatherDetails,
                        titleOverride = day,
                        onClick = {
                            vm.updateWeatherDetails(
                                weatherDetails = weatherDetails,
                                dayStr = day
                            )
                        })
                }
            }
        }
    }
}