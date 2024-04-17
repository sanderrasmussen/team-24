package no.uio.ifi.IN2000.team24_app.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.text.font.FontWeight


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(){

    Scaffold(
        modifier = Modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text(
                        "Settings"
                    )
                },

                )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(25.dp),

            content = {
                item {

                    DarkMode()

                    Language()


                }

            }
        )
    }
}

@Composable
fun SwitchComponent() {
    var isChecked by remember { mutableStateOf(false) }
    Switch(
        checked = isChecked,
        onCheckedChange = { isChecked = it },
    )
}

@Composable
fun DarkMode(){
    Spacer(modifier = Modifier.width(16.dp))
    Row(
        modifier =  Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom
    ){
        Text(text= "Dark Mode",
            color= Color.Black,
            fontSize= 25.sp)

        SwitchComponent()
    }

}

@Composable
fun Language(){
    var norwegian by remember { mutableStateOf(true) }
    var english by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Bottom


    ) {
        Text(text= "Language",
            color= Color.Black,
            fontSize= 25.sp)

        Text(
            text = "No",
            color = if (norwegian) Color.Black else Color.Gray,
            fontSize = if (norwegian) 20.sp else 18.sp,
            fontWeight = if (norwegian) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                norwegian = true
                english = false
            }
        )

        Text(
            text = "En",
            color = if (english) Color.Black else Color.Gray,
            fontSize = if (english) 20.sp else 18.sp,
            fontWeight = if (english) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.clickable {
                norwegian = false
                english = true
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SettingScreenPreview (){
    SettingsScreen()
}



