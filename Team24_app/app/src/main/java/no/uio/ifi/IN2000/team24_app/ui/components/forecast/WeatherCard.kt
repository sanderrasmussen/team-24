package no.uio.ifi.IN2000.team24_app.ui.components.forecast

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import no.uio.ifi.IN2000.team24_app.ui.Icon
import no.uio.ifi.IN2000.team24_app.ui.backgroundColour

/**
 * This composable is a card that displays the weather details for a specific time.
 * It displays the time, the weather icon and the temperature.
 * @param weatherDetail the weather details to display
 * @param onClick the action to perform when the card is clicked
 * @param modifier the modifier to apply to the card
 * @param titleOverride if this is non-zero, the title(weatherDetails.time) will be overridden.
 * @see WeatherDetails
 * @see WeatherCardsNextSixDays
 * @see WeatherCardsToday
 */
@Composable
fun WeatherCard(
    weatherDetail: WeatherDetails,
    onClick : () -> Unit,
    modifier : Modifier = Modifier,
    titleOverride: String? = null,   //if this is non-zero, the title(weatherDetails.time) will be overridden.
) {

    Card(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 4.dp)
            .height(150.dp),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColour()
        ),
        onClick = { onClick() },
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleOverride ?: "kl. ${weatherDetail.time}",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            //this if-else is a hotfix, but this is what it does
            //for the next couple of days from call, the api returns a valid symbol code for the next 1 hour.
            //however, for long term forecasts(more than 2 days), this info is not available, so we use the next_6_hours_symbol_code
            if (weatherDetail.next_1_hours_symbol_code != null) {
                Icon(weatherDetail.next_1_hours_symbol_code, 50)
            } else {
                Icon(weatherDetail.next_6_hours_symbol_code, 50)
            }
            Text(
                text = "${weatherDetail.air_temperature}°C",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
    Spacer(modifier = Modifier.padding(10.dp))
}