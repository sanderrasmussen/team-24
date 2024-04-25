package no.uio.ifi.IN2000.team24_app.ui.settings

import android.content.Context
import no.uio.ifi.IN2000.team24_app.ui.theme.DarkColorScheme
import no.uio.ifi.IN2000.team24_app.ui.theme.LightColorScheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import no.uio.ifi.IN2000.team24_app.ui.home.Icon


object Translations {
    val texts: Map<String, String> = mapOf(
        "screen_name" to "Settings",
        "dark_mode" to "Dark Mode",
        "language" to "Language",
        "temperature" to "Temperature",
        "text_size" to "Text Size",

    )
}
object TranslationsNo {
    val texts: Map<String, String> = mapOf(
        "screen_name" to "Innstillinger",
        "dark_mode" to "Mørk modus",
        "language" to "Språk",
        "temperature" to "Temperatur",
        "text_size" to "Tekststørrelse",

    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    var selectedTextSize by remember { mutableStateOf(18.sp) }
    var isNorwegian by remember{ mutableStateOf(false)}
    val texts = if (isNorwegian) TranslationsNo.texts else Translations.texts
    var isDarkMode by remember { mutableStateOf(false) }


    val context = LocalContext.current


    val sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
    selectedTextSize = remember {
        val textSize = sharedPreferences.getInt("textSize", 18)
        textSize.sp
    }
    isNorwegian = remember {
        sharedPreferences.getBoolean("isNorwegian", false)
    }
    isDarkMode = remember {
        sharedPreferences.getBoolean("isDarkMode", false)
    }


    val colorScheme = if (isDarkMode) DarkColorScheme else LightColorScheme

    Scaffold(
        modifier = Modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.primaryContainer,
                    titleContentColor = colorScheme.primary
                ),
                title = {
                    texts["screen_name"]?.let {
                        Text(
                            text = it,
                            color = colorScheme.primary
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .background(color = colorScheme.background),
            verticalArrangement = Arrangement.SpaceEvenly,)
            {
                    DarkMode(colorScheme, texts, selectedTextSize, isDarkMode) {
                        isDarkMode = it
                    }

                    Language(colorScheme,texts, selectedTextSize) { isNorwegian = it }


                    CelorFah(colorScheme,texts, selectedTextSize)


                    TextSize(colorScheme,texts, selectedTextSize) { newSize ->
                        selectedTextSize = newSize
                    }

                    NavBar(navController)

            }
    }
    DisposableEffect(Unit) {
        onDispose {
            with(sharedPreferences.edit()) {
                putInt("textSize", selectedTextSize.value.toInt())
                putBoolean("isNorwegian", isNorwegian)
                putBoolean("isDarkMode", isDarkMode)
                apply()
            }
        }
    }
}



/*
    @Composable
    fun SwitchComponent(
        isChecked: Boolean,
        onCheckedChange: (Boolean) -> Unit
    ) {
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }
*/
@Composable
fun DarkMode(
    color: ColorScheme,
    texts: Map<String, String>,
    selectedTextSize: TextUnit,
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ) {
        texts["dark_mode"]?.let {
            Text(
                text = it,
                color = color.primary,
                fontSize = selectedTextSize
            )
        }
        Switch(
            checked = isDarkMode,
            onCheckedChange = onDarkModeToggle
        )
    }
}

@Composable
fun Language(color: ColorScheme,texts : Map <String, String>, selectedTextSize: TextUnit, onLanguageSelected: (Boolean) -> Unit) {
    var norwegian by remember { mutableStateOf(false) }
    var english by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom


    ) {
        texts["language"]?.let {
            Text(
                text = it,
                color = color.primary,
                fontSize = selectedTextSize
            )
        }

        Text(
            text = "En",
            color = if (english) color.primary else Color.Gray,
            fontSize = selectedTextSize,
            fontWeight = if (english) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                onLanguageSelected(false)
                norwegian = false
                english = true
            }
        )
        Text(
            text = "No",
            color = if (norwegian) color.primary else Color.Gray,
            fontSize = selectedTextSize,
            fontWeight = if (norwegian) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                onLanguageSelected(true)
                norwegian = true
                english = false
            }
        )
    }
}


@Composable
fun CelorFah(color: ColorScheme,texts : Map <String, String>, selectedTextSize: TextUnit) {
    var celsius by remember { mutableStateOf(true) }
    var fahrenheit by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom

    ) {
        texts["temperature"]?.let {
            Text(
                text = it,
                color = color.primary,
                fontSize = selectedTextSize
            )
        }

        Text(
            text = "C°",
            color = if (celsius) color.primary else Color.Gray,
            fontSize = selectedTextSize,
            fontWeight = if (celsius) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                celsius = true
                fahrenheit = false
            }
        )

        Text(
            text = "F°",
            color = if (fahrenheit) color.primary else Color.Gray,
            fontSize = selectedTextSize,
            fontWeight = if (fahrenheit) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                celsius = false
                fahrenheit = true
            }
        )
    }

}

@Composable
fun TextSize(color: ColorScheme,texts : Map <String, String>,selectedTextSize: TextUnit, onTextSizeSelected: (TextUnit) -> Unit) {
    var small by remember { mutableStateOf(false) }
    var medium by remember { mutableStateOf(false) }
    var large by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom

    ) {
        texts["text_size"]?.let {
            Text(
                text = it,
                color = color.primary,
                fontSize = selectedTextSize
            )
        }

        Text(
            text = "Abc",
            color = if (small) color.primary else Color.Gray,
            fontSize = 16.sp,
            fontWeight = if (small) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                onTextSizeSelected(16.sp)
                small = true
                medium = false
                large = false
            }
        )

        Text(
            text = "Abc",
            color = if (medium) color.primary else Color.Gray,
            fontSize = 18.sp,
            fontWeight = if (medium) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                onTextSizeSelected(18.sp)
                small = false
                medium = true
                large = false
            }
        )
        Text(
            text = "Abc",
            color = if (large) color.primary else Color.Gray,
            fontSize = 20.sp,
            fontWeight = if (large) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                onTextSizeSelected(20.sp)
                small = false
                medium = false
                large = true
            }
        )
    }
}
@Composable
fun NavBar(navController: NavController){
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
            Icon("quiz")
        }

        Spacer(modifier = Modifier.padding(8.dp))
        Box(modifier = Modifier
            .clickable { navController.popBackStack() }
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

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview() {
    val navController = rememberNavController()
    SettingsScreen(navController)
}


