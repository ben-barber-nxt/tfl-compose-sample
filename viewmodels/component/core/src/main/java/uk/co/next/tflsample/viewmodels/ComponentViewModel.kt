package uk.co.next.tflsample.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.component.KoinComponent

interface ComponentViewModel<ComponentUiState> : KoinComponent, AutoCloseable {

    val uiState: Flow<ComponentUiState>
}

abstract class ComponentViewModelImpl<ComponentUiState> : ViewModel(), ComponentViewModel<ComponentUiState> {
    private val _uiState = MutableStateFlow(initialState())
    override val uiState : StateFlow<ComponentUiState> = _uiState.asStateFlow()

    protected abstract fun initialState(): ComponentUiState

    protected open fun updateUiState(reducer: (previousUiState: ComponentUiState) -> ComponentUiState) {
        _uiState.update(reducer)
    }

    override fun close() {
        onCleared()
    }
}