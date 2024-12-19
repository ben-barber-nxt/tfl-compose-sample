package uk.co.next.tflsample.viewmodels.component.stop_point.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import uk.co.next.repositories.core.DomainResult
import uk.co.next.stoppoint.usecases.DomainStopPoint
import uk.co.next.stoppoint.usecases.SubscribeToStopPointUseCase
import uk.co.next.tflsample.viewmodels.ComponentViewModel
import uk.co.next.tflsample.viewmodels.ComponentViewModelImpl

interface StopPointDetailsComponentViewModel : ComponentViewModel<StopPointDetailsComponentViewModelImpl.UiState>

class StopPointDetailsComponentViewModelImpl(
    private val subscribeToStopPointUseCase: SubscribeToStopPointUseCase,
    private val stopId: String,
) :
    ComponentViewModelImpl<StopPointDetailsComponentViewModelImpl.UiState>(),
    StopPointDetailsComponentViewModel {

    init {
        println("SPDCVM: $stopId")

        viewModelScope.launch {
            subscribeToStopPointUseCase(stopId)
                .collectLatest {
                    when (it) {
                        is DomainResult.Loading -> updateUiState { oldState ->
                            oldState.copy(
                                loading = true,
                                stopPoint = it.data,
                                error = null,
                            )
                        }
                        is DomainResult.Error -> updateUiState { oldState ->
                            oldState.copy(
                                loading = false,
                                stopPoint = null,
                                error = it.error
                            )
                        }
                        is DomainResult.Success -> updateUiState { oldState ->
                            oldState.copy(
                                loading = false,
                                stopPoint = it.data,
                                error = null,
                            )
                        }
                    }
                }
        }
    }

    override fun initialState() = UiState()

    data class UiState(
        val loading: Boolean = false,
        val stopPoint: DomainStopPoint? = null,
        val error: Throwable? = null,
    )
}