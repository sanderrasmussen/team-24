package no.uio.ifi.IN2000.team24_app.ui.components.character

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.ui.home.HomeScreenViewModel

/**
 * This composable displays a satisfaction bar, which shows the user's satisfaction with the current weather.
 * @param vm is the [HomeScreenViewModel] needed to get the satisfaction state.
 * @see HomeScreenViewModel
 */
@Composable
fun SatisfactionBar(vm: HomeScreenViewModel){
    val satisfaction by vm.satisfaction.collectAsState()

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(20.dp)
    ) {
        Image(
            modifier = Modifier.padding(horizontal = 4.dp)
                .size(30.dp),
            painter = painterResource(id = satisfaction.unsatisfiedIcon),
            contentDescription = "unsatisfied"
        )
        LinearProgressIndicator(
            progress = { satisfaction.fillPercent },
            modifier = Modifier
                .height(15.dp)
                .clip(CircleShape),
            color = satisfaction.color
        )
        Image(
            modifier = Modifier.padding(horizontal = 4.dp)
                .size(30.dp),
            painter = painterResource(id = R.drawable.happy),
            contentDescription = "satisfied"
        )
    }
}