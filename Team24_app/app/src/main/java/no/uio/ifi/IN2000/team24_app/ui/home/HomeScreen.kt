package no.uio.ifi.IN2000.team24_app.ui.home


import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import java.time.LocalDate
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.data.character.Inventory
import no.uio.ifi.IN2000.team24_app.data.character.Player
import no.uio.ifi.IN2000.team24_app.data.metAlerts.VarselKort
import androidx.compose.foundation.layout.*
import no.uio.ifi.IN2000.team24_app.ui.BackgroundImage
import no.uio.ifi.IN2000.team24_app.ui.Icon
import no.uio.ifi.IN2000.team24_app.ui.NavBar
import no.uio.ifi.IN2000.team24_app.ui.date
import no.uio.ifi.IN2000.team24_app.ui.day
import no.uio.ifi.IN2000.team24_app.ui.getNextSixDays

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    isNetworkAvailable: Boolean,
    homevm: HomeScreenViewModel = viewModel(),

){
    val TAG = "HomeScreen"
    homevm.getCurrentWeather(LocalContext.current) //this line needs to be here!
    homevm.getRelevantAlerts(LocalContext.current)
    val currentWeatherState : ArrayList<WeatherDetails>? by homevm.currentWeatherState.collectAsState()
    val next6DaysWeatherState:ArrayList<WeatherDetails?>? by homevm.next6DaysState.collectAsState()

    val showWeatherDetailCard = remember { mutableStateOf(null as WeatherDetails?)}
    val alertsUiState = homevm.alerts.collectAsState()
    val balance by homevm.balance.collectAsState()


    Log.d(TAG, "next6DaysWeatherState: $next6DaysWeatherState")
    val showAlerts = remember {mutableStateOf(
        //alertsUiState.value.alerts.isNotEmpty()
        false       //would rather start with this closed - this is to avoid showing on every recomposition, specifically for screen rotates
    )}
    if(showAlerts.value){
        AlertCardCarousel(alertsUiState.value, showAlerts = showAlerts)
    }

    if(showWeatherDetailCard.value != null){
        WeatherDetailCard(showWeatherDetailCard)
    }
    val blue = Color(android.graphics.Color.parseColor("#DCF6FF"))

    Log.d(TAG, "next6DaysWeatherState: $next6DaysWeatherState")

    LocationPermissionCard()

    val white = Color.White
    val gray = Color(android.graphics.Color.parseColor("#cfd0d2"))
    val currentHour = LocalTime.now().hour

    val character by homevm.characterState.collectAsState()

    //when character is updated, the satisfaction should also update.
    LaunchedEffect(character) {
        homevm.updateSatisfaction(characterTemp = character.findAppropriateTemp())
    }
    //this one is mostly to accommodate the late load of temp, but also to update the satisfaction when the temp changes (every hour in theory)
    LaunchedEffect(currentWeatherState) {
        homevm.updateSatisfaction(characterTemp = character.findAppropriateTemp())
    }

    val satisfaction by homevm.satisfaction.collectAsState()

    val currentWeatherDetails = currentWeatherState?.firstOrNull()

    val currentTemp = currentWeatherDetails?.next_1_hours_symbol_code
    val isRaining = currentTemp?.contains("rain", ignoreCase = true)
    val isSnowing = currentTemp?.contains("snow", ignoreCase = true)
    val isSleet = currentTemp?.contains("sleet", ignoreCase = true)

    var showToday by remember { mutableStateOf(true) }
    var boldToday by remember { mutableStateOf(true) }
    var boldNextSixDays by remember { mutableStateOf(false) }

    val topImage= when {
        isRaining == true -> R.drawable.raindrops
        isSnowing == true -> R.drawable.snowbackdrop
        isSleet == true -> R.drawable.sleetbackdrop
        else -> null
    }

    val imageName = BackgroundImage()

    //For better visibility the colour of the text for currentweather also changes
    val textColour = when {
        currentHour in 6 until 22 -> Color.Black
        else -> Color.White
    }

    val scope= rememberCoroutineScope()
    val snackbarHostState = remember{SnackbarHostState()}


    if(!isNetworkAvailable){
        LaunchedEffect(Unit){
            scope.launch{
                snackbarHostState.showSnackbar("No internet connection")
            }
        }

    }


        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            Image(
                painter = (painterResource(id = imageName)),
                contentDescription = "Background Image based on time of the day",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )

            if (topImage != null) {
                Image(
                    painter = painterResource(id = topImage),
                    contentDescription = "Top image based on rain or snow",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.matchParentSize()
                )
            }


            Column(
                //added these two to center the content
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,

                modifier = Modifier

                    .fillMaxSize()
                    .padding(10.dp)
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Column(
                        modifier = Modifier.padding(18.dp)
                    ) {
                        val formattedDate = date()
                        Text(
                            text = formattedDate ?: "",
                            modifier = Modifier.padding(end = 8.dp),
                            color = textColour,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )

                        val formattedDay = day()
                        Text(
                            text = formattedDay ?: "",
                            color = textColour,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Normal
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))


                    Column() {
                        CurrentWeatherInfo(
                            textColour,
                            currentTemperature = currentWeatherDetails?.air_temperature,
                            currentWeatherIcon = currentWeatherDetails?.next_1_hours_symbol_code
                        )
                    }

                }

                SatisfactionBar(satisfaction) // change to progress = satisfaction

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.coin),
                        contentDescription = "currency",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(
                        text = "${balance}",
                        color = textColour,
                        fontSize = 30.sp
                    )
                }


               Box(
                   modifier = Modifier.fillMaxWidth(),
                   contentAlignment = Alignment.Center,
                   )
               {
                    Player(character = character, modifier = Modifier.fillMaxSize(0.5f))
                    Column (
                       modifier = Modifier.align(CenterEnd),
                        horizontalAlignment = End,

                    ) {//the column with the inventory and the alert button
                        val context = LocalContext.current
                        Button(
                            onClick = {
                                if (alertsUiState.value.alerts.isNotEmpty()) {
                                    showAlerts.value = true
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Ingen farevarsler for din posisjon",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                        ) {
                            Icon(
                                iconName = "icon_warning_generic_orange",
                                24
                            )
                        }
                        Inventory(homevm.characterState)
                    }

                }
                Spacer(modifier = Modifier.weight(1f))


                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(shape = RoundedCornerShape(24.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "I dag",
                                color = if (boldToday) white else gray,
                                fontSize = 20.sp,
                                fontWeight = if (boldToday) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.clickable {
                                    showToday = true
                                    boldToday = true
                                    boldNextSixDays = false
                                }
                            )


                            Text(
                                text = "Neste 6 dager",
                                color = if (boldNextSixDays) white else gray,
                                fontSize = 20.sp,
                                fontWeight = if (boldNextSixDays) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.clickable {
                                    showToday = false
                                    boldToday = false
                                    boldNextSixDays = true
                                }
                            )
                        }
                        if (showToday) {
                            currentWeatherState?.let { WeatherCardsToday(currentHour= currentHour, weatherDetails = it, detailState = showWeatherDetailCard) }
                        } else {
                            if (currentWeatherDetails != null) {
                                WeatherCardsNextSixDays(currentHour, next6DaysWeatherState = next6DaysWeatherState, detailState = showWeatherDetailCard)
                            }


                        }
                    }

                }

            }
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)

            ) {
                NavBar(navController)
            }
        }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherCardsNextSixDays( currentHour: Int, next6DaysWeatherState: ArrayList<WeatherDetails?>?,detailState: MutableState<WeatherDetails?>) {
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
                val weatherDetails = next6DaysWeatherState[index]
                if (weatherDetails != null) {
                    WeatherCard(
                        currentHour,
                        weatherDetail = weatherDetails,
                        titleOverride = day,
                        onClick = { detailState.value = weatherDetails })
                }
            }
        }
    }
}
@Composable
fun CurrentWeatherInfo(
    textColour: Color,
    currentTemperature: Double?,
    currentWeatherIcon: String?
) {

    if (currentTemperature == null  || currentWeatherIcon.isNullOrEmpty()) {
        Text(
            text = "Nåværende temperatur: Ukjent",
            color = textColour,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                iconName = currentWeatherIcon, 70

            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$currentTemperature°C",
                color = textColour,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun WeatherCardsToday(currentHour: Int, weatherDetails: List<WeatherDetails>, detailState: MutableState<WeatherDetails?>) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(2.dp),

    ) {
        weatherDetails.forEachIndexed { index, weatherDetail ->
            WeatherCard(
                currentHour = currentHour,
                weatherDetail = weatherDetail,
                onClick = { detailState.value = weatherDetail },
            )
        }
    }
}


@Composable
fun WeatherCard(
    currentHour : Int,
    weatherDetail: WeatherDetails,
    onClick : () -> Unit,
    modifier : Modifier = Modifier,
    titleOverride: String? = null,   //if this is non-zero, the title(weatherDetails.time) will be overridden.
) {
    val cardColour = when (currentHour)  {
        in 6 until 12 -> Color(android.graphics.Color.parseColor("#123A44"))
        in 12 until 18 -> Color(android.graphics.Color.parseColor("#24552E"))
        in  18 until 22 -> Color(android.graphics.Color.parseColor("#354779"))
        else -> Color(android.graphics.Color.parseColor("#000d48"))
    }
    val yellow = Color(android.graphics.Color.parseColor("#FFFAA0"))
    val backgroundColor = cardColour
   // val height = if (hour == currentHour) 120.dp else 118.dp

    Card(
        modifier = modifier
            .padding(vertical = 16.dp, horizontal = 4.dp)
            .height(150.dp),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
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
                text = titleOverride ?: "kl. ${ weatherDetail.time}",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            //this if-else is a hotfix, but this is what it does
            //for the next couple of days from call, the api returns a valid symbol code for the next 1 hour.
            //however, for long term forecasts(more than 2 days), this info is not available, so we use the next_6_hours_symbol_code
            if(weatherDetail.next_1_hours_symbol_code != null) {
                Icon(weatherDetail.next_1_hours_symbol_code, 50)
            }else{
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
            painter = painterResource(id = satisfactionUiState.unsatisfiedIcon), contentDescription = "unsatisfied")
        LinearProgressIndicator(
            progress = { satisfactionUiState.fillPercent },
            modifier = Modifier
                .height(15.dp)
                .clip(CircleShape),
            color = satisfactionUiState.color
        )
        Image(
            modifier = Modifier.padding(horizontal = 4.dp),
            painter = painterResource(id = R.drawable.happy), contentDescription = "satisfied")   //todo custom icon, can still be hardcoded
    }
}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun LocationPermissionCard(){
    val locationPermissionState = rememberPermissionState(permission = android.Manifest.permission.ACCESS_COARSE_LOCATION)
    val showCard = remember{ mutableStateOf(!locationPermissionState.status.isGranted)}

    fun permissionDenied() {
        showCard.value=false
    }

    if(showCard.value){
        AlertDialog(
            title= { Text(text = "Requires location permission") },
            icon = {Icons.Default.LocationOn},
            onDismissRequest = { permissionDenied() },
            confirmButton = {
                Button(onClick = {
                    locationPermissionState.launchPermissionRequest()
                    showCard.value=false
                }) {
                    Text(text = "grant location permission")
                }
            },
            dismissButton = {
                Button(onClick = { permissionDenied()}) {
                    Text(text = "Refuse location permissions")
                }
            }
        )
    }
}

@Composable
fun AlertCardCarousel(alertsUi : AlertsUiState, showAlerts: MutableState<Boolean>, modifier: Modifier = Modifier) {
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


    if (showAlerts.value) {
        Dialog(
            onDismissRequest = { showAlerts.value = false },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    //verticalArrangement = Arrangement.Center,
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
                            onClick = { showAlerts.value = false },
                            modifier = Modifier
                                .width(24.dp)
                                .height(24.dp)
                        ) {
                            androidx.compose.material3.Icon(
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
                            Row(    //the row for the alert cards and the navigation buttons
                                modifier = Modifier.padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Button(
                                    onClick = {
                                        index = (index + -1) % alerts.size
                                        if (index < 0) index = alerts.size - 1
                                    },
                                ) {
                                    androidx.compose.material3.Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = "forrige varsel",
                                    )
                                }
                                //there are multiple alerts
                                LazyRow(
                                    state = scrollState,
                                    horizontalArrangement = Arrangement.Center,
                                    modifier = Modifier
                                        .height(130.dp)
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
                                    androidx.compose.material3.Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                        contentDescription = "neste varsel"
                                    )
                                }
                            }
                            Row ( //the "scroll-bar", except each dot is clickable :). only really makes sense to show a scroll bar if there are multiple elements.
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Bottom,
                                modifier = Modifier.height(16.dp)
                            ){
                                alerts.forEachIndexed { j, card ->
                                    Button(
                                        colors = ButtonDefaults.buttonColors(if (j == index) Color.Black else Color.Gray),
                                        onClick = {index = j},
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




@Composable
fun AlertCard(card:VarselKort, modifier: Modifier = Modifier){

    Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier.padding(horizontal = 5.dp)
        ) {
            Icon(card.kortImageUrl, 50)
            Text(text = "Fare ${card.farePaagar} i ${card.lokasjon}\nnivå: ${card.fareNiva}")
        }
}

private fun windDirection(degrees: Double?): String {
    if(degrees == null) return "ukjent"
    val directions = arrayOf("N", "NØ", "Ø", "SØ", "S", "SV", "V", "NV")
    val index = ((degrees + 22.5) / 45).toInt() % 8
    return directions[index]
}

/*
alright these params are a mess, but basically:
- weatherDetailState is the state that holds the weatherDetails object that is to be displayed in the card. if this is null, the card will not be displayed.
- modifier is the modifier for the card itself
- dayStr is the string for the day to display(e.g. "man." , "tir."...) IF the card is for one of the next 6 days. if this is null, the card is for a time today.
 */
@Composable
fun WeatherDetailCard(weatherDetailState : MutableState<WeatherDetails?>, modifier: Modifier = Modifier, dayStr:String? = null,){
    //TODO the units are hardcoded as string-values, but could well change from the API.
    //TODO the endpoint does take this into account, but it is discarded in the repo??. needs to be passed to viewModel?
    Dialog(
        onDismissRequest = { weatherDetailState.value = null },
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),

    ) {
        if(weatherDetailState.value != null) {
            val weatherDetail : WeatherDetails = weatherDetailState.value!!
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    ,
                shape = RoundedCornerShape(16.dp)

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(6.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { weatherDetailState.value = null },
                            modifier = Modifier
                                .padding(4.dp)
                                .width(24.dp)
                                .height(24.dp)
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = "lukk dialog",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    if(dayStr==null) {  //this is for today
                        Icon(iconName = weatherDetail.next_1_hours_symbol_code, size=50)
                        Text(text = "detaljer for klokken ${weatherDetail.time}", fontSize = 24.sp)
                        Text(text ="temperatur: ${weatherDetail.air_temperature}°C", fontSize = 18.sp)
                        Text(text="nedbørsmengde: ${weatherDetail.next_1_hours_precipitation_amount}mm", fontSize = 18.sp)
                        Text(text="skyer: ${weatherDetail.cloud_area_fraction}% dekning", fontSize = 18.sp)
                        Text(text = "vindstyrke: ${weatherDetail.wind_speed}m/s", fontSize = 18.sp)
                        Text(text = "vindretning: ${windDirection(weatherDetail.wind_from_direction)}", fontSize = 18.sp)


                    }else { //this is one of the next6days-cards
                        Text(text = "detaljer for $dayStr", fontSize = 24.sp)
                        //! here's the bitch abt this - to get hour by hour we need a new method from the repo, but i have no fucking idea whats up with the repo... still waiting for some refactor

                    }
                }
            }
        }
    }

}

/*
@Preview(showSystemUi = true)
@Composable
fun AlertCardPreview(){
    val card = VarselKort("pågår", "icon_warning_avalanches_yellow", "Oslo", "2; yellow; Moderate")
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        AlertCard(card)
    }
}
*/


@Preview(showSystemUi = true)
@Composable
fun AlertCarouselPreview(){
    val cards = listOf(
        VarselKort("pågår", "icon_warning_avalanches_yellow", "Agder, deler av Østlandet og Rogaland", "Gult Nivå"),
        VarselKort("pågår", "icon_warning_avalanches_yellow", "Agder, deler av Østlandet og Rogaland", "Gult Nivå"),
    )
    val alertsUi = AlertsUiState(cards)
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        AlertCardCarousel(alertsUi, remember { mutableStateOf(true) })
    }
}

/*
@Preview()
@Composable
fun WeatherCardPreview(){
    val weatherDetail = WeatherDetails(
        time = "12:00",
        air_temperature = 12.0,
        next_1_hours_symbol_code = "clearsky_day",
        next_6_hours_symbol_code = "clearsky_day"
    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()

    ){
        WeatherCard(false, weatherDetail)
    }
}
*/


/*
@Preview(showSystemUi = true)
@Composable
fun WeatherDetailCardPreview() {
    val weatherDetail = WeatherDetails(
        time = "12:00",
        air_pressure_at_sea_level = 1006.5,
        air_temperature = 1.6,
        cloud_area_fraction = 1.5,
        relative_humidity = 69.6,
        wind_from_direction = 48.0,
        wind_speed = 3.5,
        next_1_hours_symbol_code = "clearsky_day",
        next_1_hours_precipitation_amount = 0.0,
        next_6_hours_symbol_code = "clearsky_night",
        next_6_hours_precipitation_amount = 0.2,
        next_12_hours_symbol_code = "clearsky_day",
        next_12_hours_precipitation_amount = 0.5
    )

    val weatherDetailState: MutableState<WeatherDetails?> =
        remember { mutableStateOf(weatherDetail) }
}
*/

/*
@Preview()
@Composable
fun WeatherCardsTodayPreview(){
    val weatherDetails = listOf(
        WeatherDetails(
            time = "12:00",
            air_temperature = 12.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "13:00",
            air_temperature = 13.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "14:00",
            air_temperature = 14.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "15:00",
            air_temperature = 15.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "16:00",
            air_temperature = 16.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "17:00",
            air_temperature = 17.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "18:00",
            air_temperature = 18.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "19:00",
            air_temperature = 19.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "20:00",
            air_temperature = 20.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ),
        WeatherDetails(
            time = "21:00",
            air_temperature = 21.0,
            next_1_hours_symbol_code = "clearsky_day",
            next_6_hours_symbol_code = "clearsky_day"
        ))
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ){
        val showState = remember { mutableStateOf(null as WeatherDetails?)}
        WeatherCardsToday(12, weatherDetails, showState)
    }
}

*/