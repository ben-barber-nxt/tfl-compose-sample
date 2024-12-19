package uk.co.next.tflsample.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

interface PageViewModel<PageUiState> : KoinComponent {

    val uiState: Flow<PageUiState>
}

abstract class PageViewModelImpl<PageUiState>(childComponents: List<ComponentViewModel<*>>) : ViewModel(), PageViewModel<PageUiState> {

    init {
        childComponents.forEach {
            addCloseable(it)
        }
    }

    protected abstract fun initialState(): PageUiState

    protected open fun updateUiState(reducer: (previousUiState: PageUiState) -> PageUiState) {
        _uiState.update(reducer)
    }

    private val _uiState = MutableStateFlow(initialState())
    override val uiState : Flow<PageUiState> = _uiState.asStateFlow()

}