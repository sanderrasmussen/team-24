package no.uio.ifi.IN2000.team24_app.ui.home



import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalTime
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails
import kotlinx.coroutines.launch
import no.uio.ifi.IN2000.team24_app.R
import no.uio.ifi.IN2000.team24_app.ui.components.character.Inventory
import no.uio.ifi.IN2000.team24_app.ui.components.character.Player
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import no.uio.ifi.IN2000.team24_app.data.locationForecast.ApiAccessException
import no.uio.ifi.IN2000.team24_app.ui.BackgroundImage
import no.uio.ifi.IN2000.team24_app.ui.Icon
import no.uio.ifi.IN2000.team24_app.ui.NavBar
import no.uio.ifi.IN2000.team24_app.ui.components.alerts.AlertCardCarousel
import no.uio.ifi.IN2000.team24_app.ui.components.character.SatisfactionBar
import no.uio.ifi.IN2000.team24_app.ui.date
import no.uio.ifi.IN2000.team24_app.ui.components.forecast.WeatherCardsNextSixDays
import no.uio.ifi.IN2000.team24_app.ui.components.forecast.WeatherCardsToday
import no.uio.ifi.IN2000.team24_app.ui.components.forecast.WeatherDetailCard
import no.uio.ifi.IN2000.team24_app.ui.components.permission.LocationPermission
import no.uio.ifi.IN2000.team24_app.ui.components.permission.PermissionAction
import no.uio.ifi.IN2000.team24_app.ui.day

/**
 * HomeScreen is the main screen of the app. It displays the current weather, the weather for the next 6 days, and the alerts.
 * It also displays the character and the inventory.
 * @param navController the [NavController] used to navigate between screens
 * @param isNetworkAvailable whether or not the device has an active internet connection
 * @param homevm the associated [HomeScreenViewModel].
 * @param TAG String used for logging
 * @see NavController
 * @see HomeScreenViewModel
 */
@Composable
fun HomeScreen(
    navController: NavController,
    isNetworkAvailable: Boolean,
    homevm: HomeScreenViewModel = viewModel(),
    TAG: String = "HomeScreen"
){
    val snackbarHostState = remember{SnackbarHostState()}
    val context = LocalContext.current

    var locationToastShown by remember { mutableStateOf(false) }

    LocationPermission(
        LocalContext.current,
        snackbarHostState)
    { permissionAction ->
        when(permissionAction){
            is PermissionAction.OnPermissionGranted -> {
                Log.d(TAG, "Location permission granted")
                homevm.makeRequests(context)
            }
            is PermissionAction.OnPermissionDenied -> {
                Log.d(TAG, "Location permission denied")
                if (!locationToastShown) {
                    Toast.makeText(context, "Uten din posisjon brukes standard-posisjon: Oslo.", Toast.LENGTH_LONG).show()
                    locationToastShown = true}
                try {
                    homevm.makeRequestsWithoutLocation()
                }catch(e:ApiAccessException){
                    Toast.makeText(context, "MET-tjenestene er ikke tilgjengelige for øyeblikket - prøv igjen senere", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Log.d(TAG, "HomeScreen Composable")

    val currentWeatherState : ArrayList<WeatherDetails> by homevm.currentWeatherState.collectAsState()
    val alertsUiState = homevm.alerts.collectAsState()



    val showAlerts = remember {mutableStateOf(
        false       //would rather start with this closed - this is to avoid showing on every recomposition, specifically for screen rotates
    )}
    if(showAlerts.value){
        AlertCardCarousel(alertsUiState.value, showAlerts = showAlerts)
    }

    WeatherDetailCard(homevm)


    val white = Color.White
    val gray = Color(android.graphics.Color.parseColor("#cfd0d2"))
    val darkGray = Color(android.graphics.Color.parseColor("#767676"))

    val currentHour = LocalTime.now().hour

    val notSelectedColor = when(currentHour){
        in 12 until 18 -> darkGray
        else -> gray
    }



    val currentWeatherDetails = currentWeatherState.firstOrNull()

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


    if(!isNetworkAvailable){
        LaunchedEffect(Unit){
            scope.launch{
                snackbarHostState.showSnackbar("No internet connection")
            }
        }

    }

    Scaffold (
        snackbarHost = {
            SnackbarHost (
                hostState = snackbarHostState,
            )
        }
    ) { innerPadding ->


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
                    .padding(innerPadding)
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

                SatisfactionBar(homevm) // change to progress = satisfaction


                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                )
                {
                    val character by homevm.characterState.collectAsState()

                    //when character is updated, the satisfaction should also update.
                    LaunchedEffect(character) {
                        homevm.updateSatisfaction(characterTemp = character.findAppropriateTemp())
                    }
                    Player(character = character, modifier = Modifier.fillMaxSize(0.5f))
                    Column(
                        modifier = Modifier.align(CenterEnd),
                        horizontalAlignment = End,

                        ) {//the column with the inventory and the alert button
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
                        Inventory(homevm.characterState, currentWeatherDetails?.air_temperature?:0.0)
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
                                color = if (boldToday) white else notSelectedColor,
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
                                color = if (boldNextSixDays) white else notSelectedColor,
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
                            WeatherCardsToday(vm = homevm)
                        }else {
                            if (currentWeatherDetails != null) {
                                WeatherCardsNextSixDays(
                                    vm = homevm
                                )
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
}


/**
 * CurrentWeatherInfo displays the current weather icon and temperature.
 * @param textColour the color of the text
 * @param currentTemperature the current temperature
 * @param currentWeatherIcon the current weather icon
 */
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