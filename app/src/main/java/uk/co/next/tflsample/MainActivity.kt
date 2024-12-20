package uk.co.next.tflsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.core.component.KoinComponent
import uk.co.next.stop_search.SearchScreen
import uk.co.next.tflsample.ui.navigation.Home
import uk.co.next.tflsample.ui.navigation.StopPoint
import uk.co.next.tflsample.ui.stop_details.StopPointScreen
import uk.co.next.ui.theme.TFLSampleTheme

class MainActivity : ComponentActivity(), KoinComponent {

    @OptIn(
        ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class,
        ExperimentalLayoutApi::class, ExperimentalLayoutApi::class, ExperimentalFoundationApi::class
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {

            TFLSampleTheme {
                Surface(
                    color = MaterialTheme.colorScheme.background
                ) {
                    SharedTransitionLayout {
                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Home,
                            popExitTransition = {
                                scaleOut(
                                    targetScale = 0f,
                                    transformOrigin = TransformOrigin(
                                        pivotFractionX = 0.5f,
                                        pivotFractionY = 0.5f
                                    )
                                )
                            },
                        ) {
                            composable<Home> {

                                SearchScreen(
                                    modifier = Modifier.fillMaxSize(),
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedContentScope = this@composable,
                                    onStopPointClicked = {
                                        navController.navigate(
                                            StopPoint(
                                                id = it.id,
                                                name = it.name
                                            )
                                        )
                                    }
                                )
                            }
                            composable<StopPoint> { backStackEntry ->
                                val route = backStackEntry.toRoute<StopPoint>()
                                StopPointScreen(
                                    stopId = route.id,
                                    stopName = route.name,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedContentScope = this@composable,
                                    onUpClicked = {
                                        navController.popBackStack()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}