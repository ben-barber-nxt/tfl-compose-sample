package uk.co.next.tflsample.viewmodels.components

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.co.next.repositories.core.DomainResult
import uk.co.next.repositories.stoppoint.search.usecase.DomainStopPointSearchResult
import uk.co.next.repositories.stoppoint.search.usecase.StopPointSearchUseCase
import uk.co.next.tflsample.viewmodels.ComponentViewModel
import uk.co.next.tflsample.viewmodels.ComponentViewModelImpl

interface SearchComponentViewModel : ComponentViewModel<SearchComponentViewModelUiState> {

    fun onQueryChanged(newValue: String)
}

class SearchComponentViewModelImpl(
    private val stopPointSearch: StopPointSearchUseCase,
): ComponentViewModelImpl<SearchComponentViewModelUiState>(), SearchComponentViewModel {

    private val query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            query.collect {
                updateUiState { oldValue ->
                    oldValue.copy(
                        query = it
                    )
                }
            }
        }

        viewModelScope.launch {
            query
                .onEach {
                    println(it)
                }
                .filter { it.length >= 3 }
                .debounce(100L)
                .distinctUntilChanged()
                .flatMapLatest {
                    stopPointSearch(it)
                }
                .collect {
                    println("SCVM: $it")
                    when (it) {
                        is DomainResult.Loading -> updateUiState { oldState ->
                            oldState.copy(
                                loading = true,
                                data = it.data ?: emptyList(),
                                error = null
                            )
                        }

                        is DomainResult.Success -> updateUiState { oldState ->
                            oldState.copy(loading = false, data = it.data, error = null)
                        }

                        is DomainResult.Error -> updateUiState { oldState ->
                            oldState.copy(loading = false, data = emptyList(), error = it.error)
                        }
                    }
                }
        }
    }

    override fun initialState() = SearchComponentViewModelUiState()

    override fun onQueryChanged(newValue: String) {
        query.update { newValue }
    }
}


data class SearchComponentViewModelUiState(
    val query: String = "",
    val loading: Boolean = false,
    val data: List<DomainStopPointSearchResult> = emptyList(),
    val error: Throwable? = null,
)
