package no.uio.ifi.IN2000.team24_app.ui.home


import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun HomeScreen(
    navController: NavController,
    homevm: HomeScreenViewModel = viewModel(),
    isNetworkAvailable: Boolean
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    if (!isNetworkAvailable) {
        LaunchedEffect(Unit) {
            scope.launch {
                snackbarHostState.showSnackbar("No internet connection")
            }
        }

    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun weather(){
    val currentDate = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale("no", "NO"))
    val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("no", "NO"))
    val formattedDate = currentDate.format(formatter)


}
@Composable
fun WeatherCards() {
    var selectedCardIndex by remember { mutableStateOf(-1) }
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(8.dp)

    ) {
        repeat(12) { index ->
            WeatherCard(
                index = index,
                onClick = { clickedIndex ->
                    selectedCardIndex = if (selectedCardIndex == clickedIndex) {
                        -1
                    } else {
                        clickedIndex
                    }
                },
                isSelected = index == selectedCardIndex
            )
        }
    }
}

@Composable
fun WeatherCard(
    index: Int,
    onClick: (Int) -> Unit,
    isSelected: Boolean
) {
    val blue = Color(android.graphics.Color.parseColor("#ADD8E6"))
    val yellow = Color(android.graphics.Color.parseColor("#FFFAA0"))
    val backgroundColor = if (isSelected) yellow else blue
    val elevation = if (isSelected) 8.dp else 0.dp
    val height= if (isSelected) 120.dp else 118.dp

    Card(
        modifier = Modifier
            .padding(10.dp)
            .height(height)
            .shadow(elevation)
            .clickable {
                onClick(index)
            },
        shape = RoundedCornerShape(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor
        )
    )  {
        Text(
            text = "kl. ${if (index < 10) "0$index" else index}",
            modifier = Modifier.padding(16.dp),
            color = Color.Black
        )
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(){
    WeatherCards()
}