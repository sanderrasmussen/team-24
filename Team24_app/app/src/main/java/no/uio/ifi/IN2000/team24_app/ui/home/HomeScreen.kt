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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    homevm: HomeScreenViewModel = viewModel(),
){
    val TAG = "HomeScreen"
    homevm.getCurrentWeather(LocalContext.current) //this line needs to be here!
    homevm.getRelevantAlerts(LocalContext.current)
    val currentWeatherState : ArrayList<WeatherDetails>? by homevm.currentWeatherState.collectAsState()
    val next6DaysWeatherState:ArrayList<WeatherDetails?>? by homevm.next6DaysState.collectAsState()
    val alertsUiState = homevm.alerts.collectAsState()
    val showAlerts = remember {mutableStateOf(
        //alertsUiState.value.alerts.isNotEmpty()
        false       //would rather start with this closed - this is to avoid showing on every recomposition, specifically for screen rotates
    )}

    LocationPermissionCard()

    if(showAlerts.value){
        AlertCardCarousel(alertsUiState.value, showAlerts = showAlerts)
    }

    val blue = Color(android.graphics.Color.parseColor("#DCF6FF"))
    val white = Color.White
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

    var showToday by remember { mutableStateOf(true) }
    var boldToday by remember { mutableStateOf(true) }
    var boldNextSixDays by remember { mutableStateOf(false) }

    Column(
        //added these two to center the content
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxSize()
            .background(blue)
            .padding(16.dp)
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
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                val formattedDay = day()
                Text(
                    text = formattedDay ?: "",
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal
                )
            }
            Spacer(modifier = Modifier.width(16.dp))


            Column() {
                CurrentWeatherInfo(
                    currentTemperature = currentWeatherDetails?.air_temperature,
                    currentWeatherIcon = currentWeatherDetails?.next_1_hours_symbol_code
                )}

        }
        SatisfactionBar(satisfaction) // change to progress = satisfaction

        Player(character = character, modifier = Modifier.fillMaxSize(0.5f))
        Spacer(modifier = Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {

            Column {
                val context = LocalContext.current
                Button(
                    onClick = {
                    if(alertsUiState.value.alerts.isNotEmpty()){
                        showAlerts.value = true
                    }else{
                        Toast.makeText(context, "Ingen farevarsler for din posisjon", Toast.LENGTH_SHORT).show()
                    }
                },
                    ){
                    Icon(iconName ="icon_warning_generic_orange", modifier = Modifier.size(24.dp))
                }
                Inventory(homevm.characterState)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .clip(shape = RoundedCornerShape(24.dp))
                .background(color = white)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {//this is the row with the today/next 6 days - selection
                    Text(
                        text = "I dag",
                        color = if (boldToday) Color.Black else Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = if (boldToday) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.clickable {
                            showToday = true
                            boldToday = true
                            boldNextSixDays = false
                        }
                    )


                    Text(
                        text = "Neste 6 dager",
                        color = if (boldNextSixDays) Color.Black else Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = if (boldNextSixDays) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.clickable {
                            showToday = false
                            boldToday = false
                            boldNextSixDays = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (showToday) {
                    currentWeatherState?.let { WeatherCardsToday(currentHour, it) }
                } else {
                    if (currentWeatherDetails != null) {
                        WeatherCardsNextSixDays(next6DaysWeatherState = next6DaysWeatherState)
                    }
                }
                NavBar()
            }
        }
    }


    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
}



@RequiresApi(Build.VERSION_CODES.O)
fun getNextSixDays(): List<String> {
    val currentDay = LocalDate.now()
    val formatter = TextStyle.SHORT
    val locale = Locale("no", "NO")
    val days = mutableListOf<String>()

    for (i in 0 until 6) {
        val dayOfWeek = currentDay.plusDays(i.toLong()).dayOfWeek.getDisplayName(formatter, locale)
        days.add(dayOfWeek)
    }
    return days
}

@RequiresApi(Build.VERSION_CODES.O)
fun date(): String? {
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale("no", "NO"))
    val formattedDate = currentDate.format(formatter)
    return (formattedDate)

}
@RequiresApi(Build.VERSION_CODES.O)
fun day(): String? {
    val currentDate = LocalDate.now()
    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("no", "NO"))
    return (dayOfWeek)

}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherCardsNextSixDays(next6DaysWeatherState: ArrayList<WeatherDetails?>?) {
    val days = getNextSixDays()
    val scrollState = rememberScrollState()
    val today = day()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        days.forEach { day ->
            if (today != null && next6DaysWeatherState != null) {
                val index = days.indexOf(day)
                val weatherDetails = next6DaysWeatherState[index]
                if (weatherDetails != null) {
                    WeatherCard(
                        highlighted = index==0,
                        weatherDetail = weatherDetails,
                        titleOverride = day
                    )
                }
            }
        }
    }
}
@Composable
fun CurrentWeatherInfo(
    currentTemperature: Double?,
    currentWeatherIcon: String?
) {

    if (currentTemperature == null  || currentWeatherIcon.isNullOrEmpty()) {
        Text(
            text = "Nåværende temperatur: Ukjent",
            color = Color.Black,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(8.dp)
        )
    } else {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                iconName = currentWeatherIcon,

            )
            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$currentTemperature°C",
                color = Color.Black,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}


@Composable
fun WeatherCardsToday(currentHour: Int, weatherDetails: List<WeatherDetails>) {
    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(2.dp),

    ) {
        weatherDetails.forEachIndexed { index, weatherDetail ->
            val hourToShow = (currentHour + index) % 24
            WeatherCard(
                highlighted = hourToShow == currentHour,
                weatherDetail = weatherDetail
            )
        }
    }
}


@Composable
fun WeatherCard(
    highlighted:Boolean = false,
    weatherDetail: WeatherDetails,
    titleOverride: String? = null   //if this is non-zero, the title(weatherDetails.time) will be overridden. this is used in
) {
    val blue = Color(android.graphics.Color.parseColor("#ADD8E6"))
    val yellow = Color(android.graphics.Color.parseColor("#FFFAA0"))
    val backgroundColor = if (highlighted) yellow else blue
   // val height = if (hour == currentHour) 120.dp else 118.dp

    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(150.dp),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleOverride ?: "kl. ${ weatherDetail.time}",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            //this if-else is a hotfix, but this is what it does
            //for the next couple of days from call, the api returns a valid symbol code for the next 1 hour.
            //however, for long term forecasts(more than 2 days), this info is not available, so we use the next_6_hours_symbol_code
            if(weatherDetail.next_1_hours_symbol_code != null) {
                Icon(weatherDetail.next_1_hours_symbol_code)
            }else{
                Icon(weatherDetail.next_6_hours_symbol_code)
            }
            Text(
                text = "${weatherDetail.air_temperature}°C",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
@Composable
fun Icon(iconName: String?, modifier: Modifier = Modifier) {
    // Hvis iconName er null eller tom streng, vis standardikon
    if (iconName.isNullOrEmpty()) {
        return
    }
    // Få ressurs-IDen dynamisk ved å bruke navnet på ikonfilen
    val resourceId = getDrawableResourceId(iconName)

    // Tegn bildet hvis ressurs-IDen er gyldig
    if (resourceId != 0) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = iconName,  //bad description, but better than null. maybe pass desc. as parameter?
            modifier = modifier.size(50.dp) // Juster størrelsen etter behov
        )
    }
}

@SuppressLint("DiscouragedApi")
@Composable
fun getDrawableResourceId(iconName: String): Int {
    val context = LocalContext.current
    val packageName = context.packageName

    // Få ressurs-IDen for ikonet basert på navnet på ikonfilen
    return context.resources.getIdentifier(
        iconName,
        "drawable",
        packageName
    )
}
@Composable
fun NavBar(){
    var isClicked by remember { mutableStateOf(false) }
    Spacer(modifier=Modifier.padding(8.dp))
    Row(modifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly) {
        Box(modifier = Modifier
            .clickable { isClicked = true }
        ) {
            Icon("quiz")}

            Spacer(modifier = Modifier.padding(8.dp))
            Box(modifier = Modifier
                .clickable { isClicked = true }
            ) {
                Icon("home")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Box(modifier = Modifier
                .clickable { isClicked = true }
            ) {
                Icon("settings")
            }
        }
        //Spacer(modifier=Modifier.padding(8.dp))
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
                    Row(    //the row for the alert cards and the navigation buttons
                        modifier = Modifier.padding(4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ){
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
                        if (alerts.size == 1) {
                            //there is only one alert
                            AlertCard(
                                card = alerts[0],
                            )
                        } else {
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
                        Row ( //the "scroll-bar", except each dot is clickable :)
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



@Composable
fun AlertCard(card:VarselKort, modifier: Modifier = Modifier){
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
        ) {
            Icon(card.kortImageUrl)
            Text(text = "fare ${card.farePaagar} i ${card.lokasjon}")
            Text(text = "nivå: ${card.fareNiva}")
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

/*
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
*/


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

    ) {
        WeatherCardsToday(currentHour = 12, weatherDetails = weatherDetails)
    }
}

*/