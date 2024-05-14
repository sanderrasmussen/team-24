package no.uio.ifi.IN2000.team24_app.ui.components.alerts

import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.ui.home.AlertsUiState
import no.uio.ifi.IN2000.team24_app.ui.home.HomeScreenViewModel

@Composable
fun AlertCardCarousel(vm: HomeScreenViewModel, modifier: Modifier = Modifier) {
    val alertsUi by vm.alerts.collectAsState()
    // val showAlerts = remember{ mutableStateOf(false)}
    //val alertsState by alertsFlow.collectAsState()
    val alerts = alertsUi.alerts
    Log.d("ALERTDEBUGcomponent", "AlertCardCarousel called w alerts: ${alerts.size}")

    var index by remember { mutableIntStateOf(0) }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(index) {
        coroutineScope.launch {
            scrollState.animateScrollToItem(index)
        }
    }

    if (alertsUi.show) {
        if(alerts.isEmpty()){
            Toast.makeText(
                LocalContext.current,
                "Ingen farevarsler for din posisjon",
                Toast.LENGTH_SHORT
            ).show()
            return
        }
        Dialog(
            onDismissRequest = { vm.showAlerts(false) },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(//the row for the close button
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                            .height(24.dp)
                    ) {
                        IconButton(
                            onClick = { vm.showAlerts(false) },
                            modifier = Modifier
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


                    if (alerts.size == 1) {
                        //there is only one alert
                        AlertCard(
                            card = alerts[0],
                        )
                    } else {
                        Row(
                            //the row for the alert cards and the navigation buttons
                            modifier = Modifier.padding(4.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Button(
                                onClick = {
                                    index = (index + -1) % alerts.size
                                    if (index < 0) index = alerts.size - 1
                                },
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "forrige varsel",
                                )
                            }
                            //there are multiple alerts
                            LazyRow(
                                state = scrollState,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier
                                    //.height(130.dp)
                            ) {
                                itemsIndexed(alerts) { i, card ->
                                    if (i == index) {
                                        AlertCard(
                                            card = card,
                                            modifier = Modifier
                                                .width(130.dp)
                                        )
                                    }
                                }
                            }
                            Button(onClick = {
                                index = (index + 1) % alerts.size
                                if (index < 0) index = alerts.size - 1
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "neste varsel"
                                )
                            }
                        }
                        Row( //the "scroll-bar", except each dot is clickable :). only really makes sense to show a scroll bar if there are multiple elements.
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.height(16.dp).fillMaxWidth()
                        ) {
                            alerts.forEachIndexed { j, card ->
                                Button(
                                    colors = ButtonDefaults.buttonColors(if (j == index) Color.Black else Color.Gray),
                                    onClick = { index = j },
                                    content = {},
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .width(12.dp)
                                        .height(12.dp)
                                        .clip(shape = CircleShape),
                                )
                            }
                        }

                    }

                }

            }

        }
            }
        }