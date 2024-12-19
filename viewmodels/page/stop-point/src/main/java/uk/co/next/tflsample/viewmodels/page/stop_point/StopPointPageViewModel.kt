package uk.co.next.tflsample.viewmodels.page.stop_point

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf
import uk.co.next.tflsample.viewmodels.PageViewModelImpl
import uk.co.next.tflsample.viewmodels.component.line_status.LineStatusComponentViewModel
import uk.co.next.tflsample.viewmodels.component.line_status.LineStatusComponentViewModelImpl
import uk.co.next.tflsample.viewmodels.component.stop_point.ui.StopPointDetailsComponentViewModel
import uk.co.next.tflsample.viewmodels.component.stop_point.ui.StopPointDetailsComponentViewModelImpl

class StopPointPageViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val lineStatusComponentViewModel: LineStatusComponentViewModel,
) : PageViewModelImpl<StopPointPageViewModel.UiState>(listOf(lineStatusComponentViewModel)) {
    private val stopPointDetailsComponentViewModel: StopPointDetailsComponentViewModel by inject<StopPointDetailsComponentViewModel> {
        parametersOf(savedStateHandle.get<String>("id"))
    }

    init {
        addCloseable(stopPointDetailsComponentViewModel)

        viewModelScope.launch {
            stopPointDetailsComponentViewModel.uiState.collectLatest { detailsState ->
                updateUiState { oldState ->
                    oldState.copy(
                        details = detailsState
                    )
                }

                detailsState.stopPoint?.modesAndLines
                    ?.flatMap { it.lines.map { it.lineName } }
                    ?.takeIf { !detailsState.loading && detailsState.error == null }
                    ?.let {
                        lineStatusComponentViewModel.loadStatusForLines(it)
                    }
            }
        }

        viewModelScope.launch {
            lineStatusComponentViewModel.uiState.collectLatest {

                println(it)

                updateUiState { oldState ->
                    oldState.copy(
                        lineStatuses = it
                    )
                }
            }
        }
    }

    override fun initialState() = UiState()

    data class UiState(
        val details: StopPointDetailsComponentViewModelImpl.UiState
            = StopPointDetailsComponentViewModelImpl.UiState(),
        val lineStatuses: LineStatusComponentViewModelImpl.UiState
            = LineStatusComponentViewModelImpl.UiState(),
    )
}