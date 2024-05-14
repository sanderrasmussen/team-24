package no.uio.ifi.IN2000.team24_app.ui.quiz.category

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import no.uio.ifi.IN2000.team24_app.data.database.Category
import no.uio.ifi.IN2000.team24_app.ui.BackgroundImage
import no.uio.ifi.IN2000.team24_app.ui.GradientImage
import no.uio.ifi.IN2000.team24_app.ui.backgroundColour

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CategoriesScreen(

    onBackPressed: () -> Unit,
    categoriesScreenViewModel: CategoriesScreenViewModel = viewModel(),
    onNavigateToCategoryScreen: (String) -> Unit

) {

    // categories ui state from view model
    val categoriesUiState: CategoriesUiState by categoriesScreenViewModel.categoriesUiState.collectAsState()

    // image name variable with background image that reflects time of day
    val imageName = BackgroundImage()

    LaunchedEffect(categoriesScreenViewModel) {

        categoriesScreenViewModel.initialize()

    }

    Box(

        modifier = Modifier.fillMaxSize()

    ) {

        // background with background image
        Image(

            painter = (painterResource(id = imageName)),
            contentDescription = "Background Image based on time of the day",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.matchParentSize()

        )

        // content in column format
        Column(

            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

                // text displaying title of screen (categories)
                Text(

            // text displaying title of screen (categories)
            Text(

                text = "Kategorier:",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold

            )

            // spacer between title and category alternatives
            Spacer(modifier = Modifier.height(32.dp))

            LazyColumn(

                modifier = Modifier.heightIn(220.dp, 440.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)

            ) {

                // puts all category option items in lazy column format
                items(categoriesUiState.categories.size) { optionIndex ->

                    val currentCategory = categoriesUiState.categories[optionIndex]
                    val locked =
                        categoriesScreenViewModel.loadCategoryLockedValue(currentCategory)

                    // start button navigating to question screen
                    Button(

                        onClick = if (!locked) {

                            { onNavigateToCategoryScreen(currentCategory.category) }

                        } else {

                            { /* do nothing */ }
                        },

                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(

                            containerColor = backgroundColour()

                                { /* do nothing */ }
                            },

                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)

                        ) {

                            Text(

                                text = currentCategory.category

                            )

                            if (locked) {

                                Icon(

                                    imageVector = Icons.Filled.Lock,
                                    contentDescription = "Låst"

                                )

                            }

                        }

                    ) {

                        Text(

                            text = currentCategory.category

                        )

                        if (locked) {

                            Icon(

                                imageVector = Icons.Filled.Lock,
                                contentDescription = "Låst"

                            )

                        }

                    }

                }

            }

        }
    }