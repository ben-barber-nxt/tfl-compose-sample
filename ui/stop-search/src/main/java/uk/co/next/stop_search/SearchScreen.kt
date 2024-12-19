package uk.co.next.stop_search

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import uk.co.next.next.tflsample.viewmodels.page.search.SearchPageViewModel
import uk.co.next.repositories.stoppoint.search.usecase.DomainStopPointSearchResult

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchPageViewModel = koinViewModel<SearchPageViewModel>(),
    onStopPointClicked: (DomainStopPointSearchResult) -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    val apiResult by viewModel.uiState.collectAsState(SearchPageViewModel.UiState())

    SearchScreen(
        modifier = modifier,
        apiResult = apiResult,
        sharedTransitionScope = sharedTransitionScope,
        animatedContentScope = animatedContentScope,
        onQueryChanged = {
            viewModel.searchComponentViewModel.onQueryChanged(it)
        },
        onStopPointClicked = onStopPointClicked,
    )
}

@Composable
@OptIn(ExperimentalFoundationApi::class, ExperimentalSharedTransitionApi::class)
internal fun SearchScreen(
    modifier: Modifier,
    apiResult: SearchPageViewModel.UiState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onQueryChanged: (String) -> Unit,
    onStopPointClicked: (DomainStopPointSearchResult) -> Unit,
) {

    Scaffold(modifier = modifier) { innerPadding ->
        LazyColumn(
            contentPadding = PaddingValues(
                start = innerPadding.calculateStartPadding(LocalLayoutDirection.current) + 16.dp,
                end = innerPadding.calculateEndPadding(LocalLayoutDirection.current) + 16.dp,
                top = innerPadding.calculateTopPadding() + 16.dp,
                bottom = innerPadding.calculateBottomPadding() + 16.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            apiResult.searchComponentUiState.error?.message?.let { errorMessage ->
                item {
                    Text(errorMessage)
                }
            }

            if (apiResult.searchComponentUiState.error == null) {
                stickyHeader {

                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(top = 16.dp),
                        value = apiResult.searchComponentUiState.query,
                        onValueChange = onQueryChanged,
                    )
                }

                if (apiResult.searchComponentUiState.loading) {
                    item {
                        CircularProgressIndicator()
                    }
                } else {
                    items(items = apiResult.searchComponentUiState.data) {
                        with(sharedTransitionScope) {
                            StopPointSearchResult(
                                modifier = Modifier.sharedBounds(
                                    rememberSharedContentState("background-${it.id}"),
                                    animatedVisibilityScope = animatedContentScope,
                                ).fillMaxWidth(),
                                stopPoint = it,
                                sharedTransitionScope = sharedTransitionScope,
                                animatedContentScope = animatedContentScope,
                                onClick = { onStopPointClicked(it) },
                            )
                        }
                    }
                }
            }

        }
    }
}

//@Preview
//@Composable
//private fun SearchScreenErrorPreview() {
//    MaterialTheme {
//        SearchScreen(
//            modifier = Modifier.fillMaxSize(),
//            apiResult = SearchPageViewModel.UiState(
//                SearchComponentViewModelUiState(
//                    error = Exception("An error has occurrrrrrred")
//                )
//            ),
//            onQueryChanged = { },
//            onStopPointClicked = { },
//        )
//    }
//}