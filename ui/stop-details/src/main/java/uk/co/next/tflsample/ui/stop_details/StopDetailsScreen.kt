package uk.co.next.tflsample.ui.stop_details

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import org.koin.compose.viewmodel.koinNavViewModel
import uk.co.next.stoppoint.usecases.DomainAdditionalProperties
import uk.co.next.stoppoint.usecases.DomainStopPoint
import uk.co.next.tflsample.viewmodels.component.line_status.Status
import uk.co.next.tflsample.viewmodels.page.stop_point.StopPointPageViewModel


@OptIn(
    ExperimentalSharedTransitionApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
fun StopPointScreen(
    stopId: String,
    stopName: String,
    viewModel : StopPointPageViewModel = koinNavViewModel<StopPointPageViewModel>(),
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onUpClicked: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState(
        StopPointPageViewModel.UiState()
    )
    val loading = uiState.details.loading
    val stopPointDetails = uiState.details.stopPoint
    val error = uiState.details.error != null
    val status = uiState.lineStatuses.disruptions

    StopPointScreen(
        sharedTransitionScope = sharedTransitionScope,
        stopId = stopId,
        animatedContentScope = animatedContentScope,
        uiState = uiState,
        stopName = stopName,
        onUpClicked = onUpClicked,
        loading = loading,
        error = error,
        stopPointDetails = stopPointDetails,
        status = status
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalLayoutApi::class,
    ExperimentalSharedTransitionApi::class,
)
@Composable
private fun StopPointScreen(
    sharedTransitionScope: SharedTransitionScope,
    stopId: String,
    animatedContentScope: AnimatedContentScope,
    uiState: StopPointPageViewModel.UiState,
    stopName: String,
    onUpClicked: () -> Unit,
    loading: Boolean,
    error: Boolean,
    stopPointDetails: DomainStopPoint?,
    status: List<Status>
) = with(sharedTransitionScope) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.Companion
            .sharedBounds(
                sharedTransitionScope.rememberSharedContentState(key = "background-${stopId}"),
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
                                sharedTransitionScope.rememberSharedContentState(key = "text-${stopId}"),
                                animatedVisibilityScope = animatedContentScope
                            ),
                        text = uiState.details.stopPoint?.name ?: stopName,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = onUpClicked
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
                                DisruptionCard(it)
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
                    PropertyRow(it)
                }
            }
        }
    }
}

@Composable
private fun DisruptionCard(it: Status) {
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

@Composable
private fun PropertyRow(properties: DomainAdditionalProperties) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            if (properties.key != properties.category) {
                Text(
                    text = properties.key,
                )
            }
            Text(
                text = properties.category,
            )
        }

        Text(
            modifier = Modifier.weight(1f),
            text = properties.value,
        )
    }
}
