package no.uio.ifi.IN2000.team24_app.ui.components.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.ui.home.SatisfactionUiState

@Composable
fun SatisfactionBar(satisfactionUiState: SatisfactionUiState){
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        Image(
            modifier = Modifier.padding(horizontal = 4.dp),
            painter = painterResource(id = satisfactionUiState.unsatisfiedIcon),
            contentDescription = "unsatisfied"
        )
        LinearProgressIndicator(
            progress = { satisfactionUiState.fillPercent },
            modifier = Modifier
                .height(15.dp)
                .clip(CircleShape),
            color = satisfactionUiState.color
        )
        Image(
            modifier = Modifier.padding(horizontal = 4.dp),
            painter = painterResource(id = R.drawable.happy), contentDescription = "satisfied"
        )   //todo custom icon, can still be hardcoded
    }
}