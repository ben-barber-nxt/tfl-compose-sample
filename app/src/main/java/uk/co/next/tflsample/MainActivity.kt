package uk.co.next.tflsample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinNavViewModel
import org.koin.core.component.KoinComponent
import uk.co.next.stop_search.SearchScreen
import uk.co.next.tflsample.ui.navigation.Home
import uk.co.next.tflsample.ui.navigation.StopPoint
import uk.co.next.tflsample.viewmodels.page.stop_point.StopPointPageViewModel
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
                                StopPointScreen(
                                    backStackEntry = backStackEntry,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedContentScope = this@composable,
                                    navController = navController,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

    @Composable
    @OptIn(
        ExperimentalMaterial3Api::class,
        ExperimentalLayoutApi::class,
        ExperimentalLayoutApi::class,
        ExperimentalFoundationApi::class,
        ExperimentalSharedTransitionApi::class
    )
    private fun StopPointScreen(
        backStackEntry: NavBackStackEntry,
        sharedTransitionScope: SharedTransitionScope,
        animatedContentScope: AnimatedContentScope,
        navController: NavHostController
    ) = with(sharedTransitionScope) {
        val route = backStackEntry.toRoute<StopPoint>()
        val viewModel = koinNavViewModel<StopPointPageViewModel>()
        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
            rememberTopAppBarState()
        )
        val uiState by viewModel.uiState.collectAsState(
            StopPointPageViewModel.UiState()
        )
        val loading = uiState.details.loading
        val stopPointDetails = uiState.details.stopPoint
        val error = uiState.details.error != null
        val status = uiState.lineStatuses.disruptions
        TFLSampleTheme {
            Scaffold(
                modifier = Modifier.Companion
                    .sharedBounds(
                        sharedTransitionScope.rememberSharedContentState(key = "background-${route.id}"),
                        animatedVisibilityScope = animatedContentScope,
                    )
                    .nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    CenterAlignedTopAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        title = {
                            Text(
                                modifier = Modifier.Companion
                                    .sharedBounds(
                                        sharedTransitionScope.rememberSharedContentState(key = "text-${route.id}"),
                                        animatedVisibilityScope = animatedContentScope
                                    ),
                                text = uiState.details.stopPoint?.name ?: route.name,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        scrollBehavior = scrollBehavior,
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    navController.popBackStack()
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            ) {
                if (loading) {
                    CircularProgressIndicator()
                } else if (error) {
                    Text(
                        modifier = Modifier.padding(it),
                        text = "An error has occurred",
                    )
                } else if (stopPointDetails == null) {
                    Text(
                        modifier = Modifier.padding(it),
                        text = "Stop Point Details is empty"
                    )
                } else {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            top = it.calculateTopPadding() + 16.dp,
                            start = it.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                            end = it.calculateEndPadding(LocalLayoutDirection.current) + 16.dp,
                            bottom = it.calculateBottomPadding() + 16.dp,
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        item(key = "modes") {
                            Text(
                                text = "Modes",
                                style = MaterialTheme.typography.titleLarge,
                            )
                        }

                        items(
                            stopPointDetails.modesAndLines,
                            key = { it.modeId },
                        ) {

                            Column(
                                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    modifier = Modifier.Companion.sharedBounds(
                                        sharedTransitionScope.rememberSharedContentState(key = "mode-${uiState.details.stopPoint!!.id}-${it.modeId}"),
                                        animatedVisibilityScope = animatedContentScope
                                    ),
                                    text = it.modeName,
                                    style = MaterialTheme.typography.titleMedium,
                                )
                                FlowRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    it.lines.fastForEach {
                                        Card {
                                            Text(
                                                modifier = Modifier.padding(8.dp),
                                                text = it.lineName
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        item(key = "status") {
                            AnimatedVisibility(status.isNotEmpty()) {
                            Text(
                                text = "Disruptions",
                                style = MaterialTheme.typography.titleLarge,
                            )

                                LazyRow {
                                    items(items = status, key = { "$it" }) {
                                        Card(
                                            modifier = Modifier.width(256.dp),
                                        ) {
                                            Column {
                                                Text(it.lineId)
                                                Text(it.statusSeverityDescription)
                                                Text(it.reason)
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        item(key = "info") {
                            AnimatedVisibility(stopPointDetails.properties.isEmpty()) {
                                Text(
                                    text = "Info",
                                    style = MaterialTheme.typography.titleLarge,
                                )
                            }
                        }

                        items(stopPointDetails.properties, key = { it.key }) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                ) {
                                    if (it.key != it.category) {
                                        Text(
                                            text = it.key,
                                        )
                                    }
                                    Text(
                                        text = it.category,
                                    )
                                }

                                Text(
                                    modifier = Modifier.weight(1f),
                                    text = it.value,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
