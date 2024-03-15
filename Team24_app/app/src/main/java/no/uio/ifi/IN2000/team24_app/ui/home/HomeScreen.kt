package no.uio.ifi.IN2000.team24_app.ui.home


import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import no.uio.ifi.IN2000.team24_app.data.locationForecast.WeatherDetails


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(
    homevm: HomeScreenViewModel = viewModel(),
    //navController: NavController,
    isNetworkAvailable: Boolean
){
    homevm.getCurrentWeather(LocalContext.current)   //this line needs to be here!

    val weatherState : ArrayList<WeatherDetails>? by homevm.weatherState.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Render the UI based on weatherState and network availability
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

    var showToday by remember { mutableStateOf(true) }
    var boldToday by remember { mutableStateOf(true) }
    var boldNextSevenDays by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(blue)
            .padding(16.dp)
    ) {
        val formattedDate = date()
        Text(
            text = formattedDate ?: "",
            modifier = Modifier.padding(bottom = 8.dp),
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

                Spacer(modifier = Modifier.height(16.dp))

                if (showToday) {
                    weatherState?.let { WeatherCardsToday(currentHour, it) }
                } else {
                    NextSevenDays()
                }
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
        // Itererer gjennom alle værdetaljer
        for (index in 0 until 24) {
            val hourToShow = (currentHour + index) % 24
            // Finn værdetaljene for det gjeldende timetallet hvis de er tilgjengelige
            val weatherDetail = weatherDetails.getOrNull(index)
            if (weatherDetail != null) {
                WeatherCardToday(
                    hour = hourToShow,
                    currentHour = currentHour,
                    weatherDetail = weatherDetail
                )
            }
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
    val height = if (hour == currentHour) 120.dp else 118.dp

    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(height),
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "kl. ${if (hour < 10) "0$hour" else hour}",
                color = Color.Black,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            // Display weather details here
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

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    val isNetworkAvailable = true // Set network availability status for preview
    HomeScreen(homevm = HomeScreenViewModel(), isNetworkAvailable = isNetworkAvailable)
}
