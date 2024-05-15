package no.uio.ifi.IN2000.team24_app

import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import no.uio.ifi.IN2000.team24_app.ui.home.HomeScreen
import no.uio.ifi.IN2000.team24_app.ui.store.StoreScreen
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoriesScreen
import no.uio.ifi.IN2000.team24_app.ui.quiz.category.CategoryScreen
import no.uio.ifi.IN2000.team24_app.ui.quiz.question.QuestionResultScreen
import no.uio.ifi.IN2000.team24_app.ui.quiz.question.QuestionScreen
import no.uio.ifi.IN2000.team24_app.ui.theme.Team24_appTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Team24_appTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Controller()
                }
            }
        }

    }


    @Composable
    fun Controller() {
        val navController = rememberNavController()
        val context = LocalContext.current
        val isNetworkAvailable = isNetworkAvailable(context)

        NavHost(navController = navController, startDestination = "HomeScreen") {
            composable("HomeScreen") {
                HomeScreen(navController, isNetworkAvailable)
            }
            composable("StoreScreen") {
                StoreScreen(navController)
            }

            // navigation logic for categories screen
            composable("CategoriesScreen") {

                CategoriesScreen(

                    navController = navController,
                    onNavigateToCategoryScreen = { categoryName ->
                        navController.navigate("CategoryScreen/$categoryName")

                    }

                )

            }

            // navigation logic for category screen
            composable(
                route = "CategoryScreen/{categoryName}",
                arguments = listOf(navArgument("categoryName") { NavType.StringType })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName").orEmpty()
                CategoryScreen(
                    onBackPressed = { navController.popBackStack("CategoriesScreen", inclusive = true) },
                    categoryName = categoryName,
                    onNavigateToQuestionScreen = { questions ->

                        if (questions != null) {
                            if (questions.isNotBlank()) {
                                val index = 0
                                val coinsWon = 0
                                navController.navigate("QuestionScreen/$categoryName/$questions/$index/$coinsWon")
                            } else {
                                println("Error: Questions is empty!")
                            }
                        }
                    }
                )
            }

            // navigation logic for question screen
            composable(
                route = "QuestionScreen/{categoryName}/{questions}/{index}/{coinsWon}",
                arguments = listOf(
                    navArgument("categoryName") { NavType.StringType },
                    navArgument("questions") { NavType.StringType },
                    navArgument("index") { NavType.IntType },
                    navArgument("coinsWon") { NavType.IntType }
                )
            ) { backStackEntry ->

                val categoryName = backStackEntry.arguments?.getString("categoryName").orEmpty()
                val questions = backStackEntry.arguments?.getString("questions").orEmpty()
                val indexState = remember { mutableIntStateOf(backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0) }
                val coinsWonState = remember { mutableIntStateOf(backStackEntry.arguments?.getString("coinsWon")?.toIntOrNull() ?: 0) }

                // Oppdater index fra indexState
                val index = indexState.intValue
                val coinsWon= coinsWonState.intValue
                println("Index State Value: $index")

                QuestionScreen(
                    onBackPressed = { navController.popBackStack("CategoriesScreen", inclusive = true) },
                    categoryName = categoryName,
                    questions = questions,
                    index = index,
                    coinsWon = coinsWon,
                    onNavigateToNextQuestionScreen = { nextIndex, newCoinsWon ->
                        // Oppdater indexState med nextIndex
                        indexState.value = nextIndex
                        if (newCoinsWon != null) {
                            coinsWonState.value = newCoinsWon
                        }
                        println("Navigating to next question with index: $nextIndex and coinsWon: $newCoinsWon")
                        navController.navigate("QuestionScreen/$categoryName/$questions/$nextIndex/$newCoinsWon")
                    },
                    onNavigateToResultQuestionScreen = { newCoinsWon ->
                        println("Navigating to resultscreen with coinsWon: $newCoinsWon")
                        navController.navigate("QuestionResultScreen/$categoryName/$questions/$newCoinsWon")
                    }
                )
            }


            // navigation logic for question result screen
            composable(

                route = "QuestionResultScreen/{categoryName}/{questions}/{coinsWon}",
                arguments = listOf(navArgument("categoryName") { NavType.StringType },
                    navArgument("questions") { NavType.StringType },
                    navArgument("coinsWon") { NavType.IntType })

            ) {

                    backStackEntry ->

                val categoryName = backStackEntry.arguments?.getString("categoryName").orEmpty()
                val questions = backStackEntry.arguments?.getString("questions").orEmpty()
                val coinsWon = backStackEntry.arguments?.getString("coinsWon")?.toIntOrNull() ?: 0
                QuestionResultScreen(

                    onBackPressed = { navController.popBackStack("CategoriesScreen", inclusive = true) },
                    categoryName = categoryName,
                    questions = questions,
                    coinsWon = coinsWon

                )

            }

        }

    }


    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnectedOrConnecting

    }
}