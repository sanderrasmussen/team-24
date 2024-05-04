package no.uio.ifi.IN2000.team24_app.ui

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import no.uio.ifi.IN2000.team24_app.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

class Components {
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
    fun BackgroundImage(): Int {
        return when (LocalTime.now().hour){
            in 6 until 12 -> R.drawable.weather_morning// 6am to 12 pm
            in 12 until 18 -> R.drawable.weather_day //12 pm to 6 pm
            in 18 until 22 -> R.drawable.weather_noon // 6pm to 10pm
            else -> R.drawable.weather_night// 10pm to 6 am
        }

    }

    @Composable
    fun Icon(iconName: String?, size: Int) {
        // if iconName is empty, return
        if (iconName.isNullOrEmpty()) {
            return
        }
        // get the resourceId of the iconName
        val resourceId = getDrawableResourceId(iconName)

        // if resourceId available, paint the image
        if (resourceId != 0) {
            Image(
                painter = painterResource(id = resourceId),
                contentDescription = "$iconName",
                modifier = Modifier.size(size.dp)
            )
        }
    }

    @SuppressLint("DiscouragedApi")
    @Composable
    fun getDrawableResourceId(iconName: String): Int {
        val context = LocalContext.current
        val packageName = context.packageName

        // get resourceId based on icon name
        return context.resources.getIdentifier(
            iconName,
            "drawable",
            packageName
        )
    }

    //TODO make an aesthetically pleasing navBar with icons for homescreen, storescreen and quizscreen
    @Composable
    fun NavBar(navController: NavController){
        var isClicked by remember { mutableStateOf(false) }

        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier
                .clickable { isClicked = true }
            ) {
                Icon("quiz", 40)}

            Spacer(modifier = Modifier.padding(8.dp))
            Box(modifier = Modifier
                .clickable {  navController.navigate("HomeScreen")  }
            ) {
                Icon("home", 40)
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Box(modifier = Modifier
                .clickable {  navController.navigate("StoreScreen")  }
            ) {
                Icon("clothing_store", 40)
            }
        }
        //Spacer(modifier=Modifier.padding(8.dp))
    }


}