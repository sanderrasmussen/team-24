package no.uio.ifi.IN2000.team24_app.ui.components.alerts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import no.uio.ifi.IN2000.team24_app.data.metAlerts.VarselKort
import no.uio.ifi.IN2000.team24_app.ui.Icon

@Composable
fun AlertCard(card: VarselKort, modifier: Modifier = Modifier){

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(horizontal = 5.dp)
    ) {
        Icon(card.kortImageUrl, 50)
        Text(text = "Fare ${card.farePaagar} i ${card.lokasjon}\nnivå: ${card.fareNiva}")
    }
}