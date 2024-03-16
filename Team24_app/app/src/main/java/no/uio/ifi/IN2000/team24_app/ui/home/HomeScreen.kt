package no.uio.ifi.IN2000.team24_app.ui.home


import android.annotation.SuppressLint
import android.os.Build
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homevm: HomeScreenViewModel = viewModel(),
    //navController: NavController,
    isNetworkAvailable: Boolean
){
    homevm.getCurrentWeather(LocalContext.current) //this line needs to be here!

    val weatherState : ArrayList<WeatherDetails>? by homevm.weatherState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }


    if (!isNetworkAvailable) {
        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.showSnackbar("No internet connection")
            }
        }
    } else {
        ActualHomeScreen(weatherState = weatherState)
    }
}


@RequiresApi(Build.VERSION_CODES.O)
fun getNextSevenDays(): List<String> {
    val currentDay = LocalDate.now()
    val formatter = TextStyle.SHORT
    val locale = Locale("no", "NO")
    val days = mutableListOf<String>()

    for (i in 0 until 7) {
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
fun getCurrentHour(): Int {
    return LocalTime.now().hour
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ActualHomeScreen(
    weatherState: ArrayList<WeatherDetails>?,
) {
    val blue = Color(android.graphics.Color.parseColor("#DCF6FF"))
    val white = Color.White
    val currentHour = getCurrentHour()

    print(weatherState)

    val currentWeatherDetails = weatherState?.firstOrNull()

    var showToday by remember { mutableStateOf(true) }
    var boldToday by remember { mutableStateOf(true) }
    var boldNextSevenDays by remember { mutableStateOf(false) }

    Column(
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

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
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
                            boldNextSevenDays = false
                        }
                    )


                    Text(
                        text = "Neste 7 dager",
                        color = if (boldNextSevenDays) Color.Black else Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = if (boldNextSevenDays) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.clickable {
                            showToday = false
                            boldToday = false
                            boldNextSevenDays = true
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (showToday) {
                    weatherState?.let { WeatherCardsToday(currentHour, it) }
                } else {
                    NextSevenDays()
                }

                navBar()


            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NextSevenDays() {
    val days = getNextSevenDays()
    val scrollState = rememberScrollState()
    val today= day()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        days.forEach { day ->
            if (today != null) {
                WeatherCardNextDay(day = day)
            }
        }
    }
}

@Composable
fun CurrentWeatherInfo(
    currentTemperature: Double?,
    currentWeatherIcon: String?
) {

    if (currentTemperature == null || currentWeatherIcon.isNullOrEmpty()) {
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun WeatherCardNextDay(
    day: String,
) {

    val currentDate = LocalDate.now()
    val formatter = TextStyle.SHORT
    val locale = Locale("no", "NO")
    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(formatter, locale)

    val blue = Color(android.graphics.Color.parseColor("#ADD8E6"))
    val yellow = Color(android.graphics.Color.parseColor("#FFFAA0"))
    val backgroundColor = if (day == dayOfWeek) yellow else blue



    Card(
        modifier = Modifier
            .padding(10.dp)
            .width(65.dp)
            .height(120.dp),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Text(
            text = day,
            modifier = Modifier.padding(16.dp),
            color = Color.Black
        )
    }
}

@Composable
fun WeatherCardsToday(currentHour: Int, weatherDetails: List<WeatherDetails>) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        weatherDetails.forEachIndexed { index, weatherDetail ->
            val hourToShow = (currentHour + index) % 24
            WeatherCardToday(
                hour = hourToShow,
                currentHour = currentHour,
                weatherDetail = weatherDetail
            )
        }
    }
}


@Composable
fun WeatherCardToday(
    hour: Int,
    currentHour: Int,
    weatherDetail: WeatherDetails
) {
    val blue = Color(android.graphics.Color.parseColor("#ADD8E6"))
    val yellow = Color(android.graphics.Color.parseColor("#FFFAA0"))
    val backgroundColor = if (hour == currentHour) yellow else blue
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
                text = "kl. ${if (hour < 10) "0$hour" else hour}",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            Icon(weatherDetail.next_1_hours_symbol_code)
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
            contentDescription = null,
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
fun navBar(){
    var isClicked by remember { mutableStateOf(false) }
    Spacer(modifier=Modifier.padding(8.dp))
    Row(modifier = Modifier.padding(8.dp)
        .fillMaxWidth()
        .background(Color.White),
        horizontalArrangement = Arrangement.SpaceEvenly){
        Box(modifier = Modifier
            .clickable { isClicked = true }
        ){
        Icon("quiz")}
        Spacer(modifier= Modifier.padding(8.dp))
        Box(modifier = Modifier
            .clickable { isClicked = true }
            ){
        Icon("home")}
        Spacer(modifier= Modifier.padding(8.dp))
        Box(modifier = Modifier
            .clickable { isClicked = true }
            ){
        Icon("settings")}
    }
    //Spacer(modifier=Modifier.padding(8.dp))

}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val isNetworkAvailable = true
    HomeScreen(homevm = HomeScreenViewModel(), isNetworkAvailable = isNetworkAvailable)
}
