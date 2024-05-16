package no.uio.ifi.IN2000.team24_app.ui.components.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.IN2000.team24_app.data.metAlerts.WarningCard
import no.uio.ifi.IN2000.team24_app.ui.Icon

/**
 * Composable function that creates a card for an alert
 * @param card the [WarningCard] to be displayed
 * @param modifier the modifier for the card
 * @see WarningCard
 */
@Composable
fun AlertCard(card: WarningCard, modifier: Modifier = Modifier){

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 5.dp)
    ) {
        Icon(card.imageUrl, 50)
        Text(text = "Fare ${card.ongoingDanger} i ${card.location}\nnivå: ${card.dangerLevel}")
    }
}