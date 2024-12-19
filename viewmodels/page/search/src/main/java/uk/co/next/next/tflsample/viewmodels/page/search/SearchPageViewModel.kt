package uk.co.next.next.tflsample.viewmodels.page.search

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import uk.co.next.tflsample.viewmodels.PageViewModelImpl
import uk.co.next.tflsample.viewmodels.components.SearchComponentViewModel
import uk.co.next.tflsample.viewmodels.components.SearchComponentViewModelUiState

class SearchPageViewModel(
    val searchComponentViewModel: SearchComponentViewModel,
) : PageViewModelImpl<SearchPageViewModel.UiState>(
    listOf(
        searchComponentViewModel,
    )
) {

    init {
        viewModelScope.launch {
            searchComponentViewModel.uiState.collect {
                updateUiState { previousUiState ->
                    previousUiState.copy(
                        searchComponentUiState = it
                    )
                }
            }
        }
    }

    override fun initialState() = UiState()

    data class UiState(
        val searchComponentUiState: SearchComponentViewModelUiState = SearchComponentViewModelUiState()
    )
}