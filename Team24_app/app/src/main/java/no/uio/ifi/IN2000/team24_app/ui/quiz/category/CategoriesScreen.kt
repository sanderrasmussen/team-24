package no.uio.ifi.IN2000.team24_app.ui.quiz.category

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CategoriesScreen(

    categoriesScreenViewModel: CategoriesScreenViewModel = viewModel(),
    onNavigateToCategoryScreen: (String) -> Unit

) {

    //
    val categoriesUiState: CategoriesUiState by categoriesScreenViewModel.categoriesUiState.collectAsState()

    Column(

        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        // text displaying title of screen (categories)
        Text(

            text = "Kategorier:",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold

        )

        // spacer between title and category alternatives
        Spacer(modifier = Modifier.height(32.dp))

        // lazy column containing category alternatives
        LazyColumn(

            modifier = Modifier.heightIn(220.dp, 440.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)

        ) {

            // puts all category option items in lazy column format
            items(categoriesUiState.categories.size) { optionIndex ->

                val currentCategory = categoriesUiState.categories[optionIndex]
                val locked = categoriesScreenViewModel.loadCategoryLockedValue(currentCategory)

                // start button navigating to question screen
                Button(

                    onClick = if (!locked) {

                        { onNavigateToCategoryScreen(currentCategory.category) }

                    } else {

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

            }

        }

    }

}