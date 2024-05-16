package no.uio.ifi.IN2000.team24_app.ui

import android.annotation.SuppressLint
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
import androidx.navigation.compose.currentBackStackEntryAsState
import no.uio.ifi.IN2000.team24_app.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale


/**
 * This file contains functions that are used in multiple screens in the app.
 */

/**
 * This function returns a list of the name of next six days in a short format
 * for example man for Mandag. The language it returns it in is norwegian which is determined
 * by the locale variable.
 */
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

/**
 * this function returns the current date in the format dd.MMMM yyyy in Norwegian
 * @see LocalDate
 */
fun date(): String? {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd. MMMM yyyy", Locale("no", "NO"))
        val formattedDate = currentDate.format(formatter)
        return (formattedDate)

    }

/**
 * This function returns the current day in Norwegian
 */
fun day(): String? {
        val currentDate = LocalDate.now()
        val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale("no", "NO"))
        return (dayOfWeek)

    }

    /**
     * This function returns the resource ID of the background image determined by the time
     * of the day
     */
    fun BackgroundImage(): Int {
        return when (LocalTime.now().hour){
            in 6 until 12 -> R.drawable.weather_morning// 6am to 12 pm
            in 12 until 18 -> R.drawable.weather_day //12 pm to 6 pm
            in 18 until 22 -> R.drawable.weather_noon // 6pm to 10pm
            else -> R.drawable.weather_night// 10pm to 6 am
        }

    }


/**
 * function to get the background color based on the time of the day, so that it corresponds with the background image
 * @see BackgroundImage
 */
fun backgroundColour(): Color {
        return when (LocalTime.now().hour)  {
            in 6 until 12 -> Color(android.graphics.Color.parseColor("#123A44"))
            in 12 until 18 -> Color(android.graphics.Color.parseColor("#24552E"))
            in  18 until 22 -> Color(android.graphics.Color.parseColor("#354779"))
            else -> Color(android.graphics.Color.parseColor("#000d48"))
        }
    }

    // function to return a color based on sky
/**
 * function to get the color of the sky based on the time of the day
 */
fun skyColour(): Color {
        return when (LocalTime.now().hour) {
            in 6 until 12 -> Color(android.graphics.Color.parseColor("#98C5E2"))
            in 12 until 18 -> Color(android.graphics.Color.parseColor("#0C91D9"))
            in  18 until 22 -> Color(android.graphics.Color.parseColor("#5E80CD"))
            else -> Color(android.graphics.Color.parseColor("#0c0a4c"))
        }
    }

    /**
     * function to get the color of the text based on the time of the day
     */
    fun textColour(): Color {
        return when (LocalTime.now().hour) {
            in 6 until 22 -> Color.Black
            else -> Color.White
        }
    }

/**
 * This function takes an icon name and size as arguments and then displays the icon
 * in a composable UI
 */
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

    //This function returns the resourceID for a given icon name.
/**
 * This function takes an icon or image name as an argument and returns the resource ID
 * @param iconName the name of the icon or image
 * @return the resource ID of the icon or image
 * @see Icon
 */
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

/**
 * This function returns an arrow icon with a color based on the time of the day
 */
@Composable
    fun arrow(size: Int) : Any {
        return when (LocalTime.now().hour) {
            in 6 until 22 -> Icon("arrow", size)
            else -> Icon("arrow_white", size)
        }

    }

/**
 * This function returns a navigation bar that allows users to navigate between three different screens.
 * @param navController the navController that is used to navigate between screens
 * @see NavController
 */
    @Composable
    fun NavBar(navController: NavController){
        //get the current back stack entry associated with the navController
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        //get the route assosiated with the current back stack entry
        val currentRoute = navBackStackEntry?.destination?.route

        var isClicked by remember { mutableStateOf(false) }

        Row(modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColour()),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier
                .clickable {
                    if (currentRoute != "CategoriesScreen") { //navigates to Categoriesscreen if not already on it
                        navController.navigate("CategoriesScreen")
                    }
                }
                .padding(8.dp)

            ) {
                Icon("quiz_white",60)
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Box(modifier = Modifier
                .clickable {
                    if (currentRoute != "HomeScreen") { //navigates to homescreen if not already on it
                        navController.navigate("HomeScreen")
                    }
                }
                .padding(8.dp)
            ) {
                Icon("home_white",60)
            }

            Spacer(modifier = Modifier.padding(8.dp))

            Box(modifier = Modifier
                .clickable {
                    if (currentRoute != "StoreScreen") { //navigates to storescreen if not already on it
                        navController.navigate("StoreScreen")
                    }
                }
                .padding(8.dp)

            ) {
                Icon("clothing_store_white",60)
            }
        }

    }