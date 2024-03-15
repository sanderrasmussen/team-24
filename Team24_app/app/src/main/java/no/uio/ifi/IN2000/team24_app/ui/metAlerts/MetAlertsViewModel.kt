package no.uio.ifi.IN2000.team24_app.ui.metAlerts


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import no.uio.ifi.IN2000.team24_app.data.metAlerts.MetAlerts

data class MetAlertsUIState (val metAlertsInfo: List <MetAlerts>)
class MetAlertsViewModel : ViewModel() {

    //val metAlertsRepository : Repository = null

    var metAlertsUIState  by mutableStateOf(MetAlertsUIState(listOf()))
}