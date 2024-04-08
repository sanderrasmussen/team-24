package no.uio.ifi.IN2000.team24_app.ui.home


import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
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

    val currentWeatherState : ArrayList<WeatherDetails>? by homevm.currentWeatherState.collectAsState()
    val next6DaysWeatherState:ArrayList<WeatherDetails?>? by homevm.next6DaysState.collectAsState()
    val alertsUiState by homevm.alerts.collectAsState()

    Log.d(TAG, "next6DaysWeatherState: $next6DaysWeatherState")

    LocationPermissionCard()

    AlertCardCarousel(alerts = alertsUiState.alerts)


    val blue = Color(android.graphics.Color.parseColor("#DCF6FF"))
    val white = Color.White
    val currentHour = LocalTime.now().hour

    val character by homevm.characterState.collectAsState()
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

        Inventory(homevm.characterState)

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .clip(shape = RoundedCornerShape(24.dp))
                .background(color = white)

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
            .fillMaxWidth()
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
            .fillMaxWidth()
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
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titleOverride ?: "kl. ${ weatherDetail.time}",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))

            //this if-else is a hotfix, but this is what it does
            //for the next couple of days from call, the api returns a valid symbol code for the next 1 hour.
            //however, for long term forecasts(more than 2 days), this info is not available, so we use the next_6_hours_symbol_code
            if(weatherDetail.next_1_hours_symbol_code != null) {
                Icon(weatherDetail.next_1_hours_symbol_code)
            }else{
                Icon(weatherDetail.next_6_hours_symbol_code)
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "${weatherDetail.air_temperature}°C",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}
@Composable
fun Icon(iconName: String?) {
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
            modifier = Modifier.size(50.dp) // Juster størrelsen etter behov
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
    //fill should be hex calculated as `(1-progress) * red`, and `progress * green` (0 blue)
    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f) // 50% of screen size
            .height(20.dp)
        
    ) {
        Image(painter = painterResource(id = satisfactionUiState.unsatisfiedIcon), contentDescription = "unsatisfied")
        LinearProgressIndicator(
            progress = { satisfactionUiState.fillPercent },
            modifier = Modifier
                .fillMaxWidth()
                .height(15.dp)
                .clip(CircleShape),
            color = satisfactionUiState.color
        )
        Image(painter = painterResource(id = R.drawable.happy), contentDescription = "satisfied")   //todo custom icon, can still be hardcoded
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
fun AlertCardCarousel(alerts:List<VarselKort>) {
    var index by remember { mutableIntStateOf(0) }
    val showCard = remember { mutableStateOf(alerts.isNotEmpty()) }

    val scrollState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(index) {
        coroutineScope.launch {
            scrollState.animateScrollToItem(index)
        }
    }

    fun changeCard(changeBy: Int) {
        index = (index + changeBy) % alerts.size
        if (index < 0) index = alerts.size - 1
    }

    if (showCard.value) {
        Dialog(
            onDismissRequest = { showCard.value = false },
            properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true),
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    //verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(//the row for the close button
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .padding(0.dp)
                            .fillMaxWidth()
                    ) {
                        IconButton(
                            onClick = { showCard.value = false },
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

                    if (alerts.size == 1) {
                        //there is only one alert
                        AlertCard(
                            card = alerts[0],
                            changeCard = ::changeCard,
                            showButtons = false
                        )
                    } else {
                        //there are multiple alerts
                        LazyRow(
                            state = scrollState,
                            horizontalArrangement = Arrangement.spacedBy(0.dp),
                        ) {
                            itemsIndexed(alerts) { i, card ->
                                if (i == index) {
                                    AlertCard(
                                        card = card,
                                        changeCard = ::changeCard,
                                    )
                                }
                            }
                        }
                        Row (
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier.fillMaxHeight()

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
fun AlertCard(card:VarselKort, changeCard: (Int) ->Unit, showButtons : Boolean = true, modifier: Modifier = Modifier){
    Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(16.dp),

                ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if(showButtons){
                        Button(
                            onClick = { changeCard(-1) },
                        ) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "forrige varsel"
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                            Icon(card.kortImageUrl)
                            Text(text = "fare ${card.farePaagar} i ${card.lokasjon}")
                            //TODO update repo to deliver a more concise UI-friendly string
                            Text(text = "nivå: ${card.fareNiva.split(";")[2]}")
                    }
                    if(showButtons) {
                        Button(onClick = { changeCard(1) }) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "neste varsel"
                            )
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
fun AlertCardCarouselPreview(){
    val cards = listOf(
        VarselKort("1", "icon_warning_avalanches_yellow", "Oslo", "2; yellow; Moderate"),
        VarselKort("2", "icon_warning_avalanches_red", "Trondheim", "2; yellow; Moderate"),
        VarselKort("3", "icon_warning_avalanches_orange", "Bergen", "2; yellow; Moderate"),
        VarselKort("4", "icon_warning_avalanches_yellow", "Oslo", "2; yellow; Moderate"),
        VarselKort("5", "icon_warning_avalanches_yellow", "Oslo", "2; yellow; Moderate"),
        VarselKort("6", "icon_warning_avalanches_yellow", "Oslo", "2; yellow; Moderate"),
        VarselKort("7", "icon_warning_avalanches_yellow", "Oslo", "2; yellow; Moderate"),
        VarselKort("8", "icon_warning_avalanches_yellow", "Oslo", "2; yellow; Moderate"),
    )
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        AlertCardCarousel(cards)
    }
}

