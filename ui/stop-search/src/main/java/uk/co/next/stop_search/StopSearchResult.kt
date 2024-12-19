package uk.co.next.stop_search

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import uk.co.next.repositories.stoppoint.search.usecase.DomainStopPointSearchResult

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun StopPointSearchResult(
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    stopPoint: DomainStopPointSearchResult,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    with(sharedTransitionScope) {
        Card(
            modifier = modifier,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.padding(8.dp),
                ) {
                    Text(
                        modifier = Modifier.sharedBounds(
                            rememberSharedContentState(key = "text-${stopPoint.id}"),
                            animatedVisibilityScope = animatedContentScope,
                        ),
                        text = stopPoint.name
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        stopPoint.modes.fastForEach {
                            Text(
                                modifier = Modifier.sharedBounds(
                                    rememberSharedContentState(key = "mode-${stopPoint.id}-$it"),
                                    animatedVisibilityScope = animatedContentScope,
                                ),
                                text = it,
                            )
                        }
                    }
                }

                Text(stopPoint.zone.orEmpty())
            }
        }
    }
}

//@Preview
//@Composable
//private fun StopPointSearchResultPreview() {
//    MaterialTheme {
//        StopPointSearchResult(
//            modifier = Modifier.fillMaxWidth(),
//            stopPoint = DomainStopPointSearchResult(
//                id = "id",
//                name = "name",
//                zone = "1",
//                modes = listOf("mode1", "mode2", "mode3"),
//            ),
//            onClick = { },
//            animatedContentScope = rememberCoroutineScope()
//        )
//    }
//}
