package uk.co.next.tflsample.viewmodels.component.line_status

import androidx.lifecycle.viewModelScope
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.co.next.tflsample.api.tfl.models.ApiLine
import uk.co.next.tflsample.viewmodels.ComponentViewModel
import uk.co.next.tflsample.viewmodels.ComponentViewModelImpl

interface LineStatusComponentViewModel :
    ComponentViewModel<LineStatusComponentViewModelImpl.UiState> {

        fun loadStatusForLines(lineIds: List<String>)
    }

class LineStatusComponentViewModelImpl(
    private val httpClient: HttpClient,
) : ComponentViewModelImpl<LineStatusComponentViewModelImpl.UiState>(),
    LineStatusComponentViewModel {

    override fun loadStatusForLines(lineIds: List<String>) {
        viewModelScope.launch(Dispatchers.IO) {
            updateUiState {
                it.copy(loading = true, disruptions = emptyList(), error = null)
            }

            runCatching {
                val apiLineStatuses =
                    httpClient.get("https://api.tfl.gov.uk/Line/${lineIds.joinToString(",")}/Status")
                        .body<List<ApiLine>>()

                val result = apiLineStatuses.flatMap { line ->
                    if (line.lineStatuses.count() == 1 && line.lineStatuses.first().statusSeverityDescription == "Good Service") {
                        listOf()
                    } else {
                        line.lineStatuses
                            .map {
                                Status(
                                    lineId = it.lineId ?: line.id,
                                    statusSeverity = it.statusSeverity,
                                    statusSeverityDescription = it.statusSeverityDescription,
                                    reason = it.reason.orEmpty(),
                                )
                            }
                    }
                }

                updateUiState { oldState ->
                    oldState.copy(
                        loading = false,
                        error = null,
                        disruptions = result,
                    )
                }
            }.onFailure { error ->
                updateUiState {
                    it.copy(
                        loading = false,
                        error = error,
                        disruptions = emptyList()
                    )
                }
            }
        }
    }

    override fun initialState() = UiState()

    data class UiState(
        val loading: Boolean = false,
        val error: Throwable? = null,
        val disruptions: List<Status> = emptyList()
    )
}

data class Status(
    val lineId: String,
    val statusSeverity: Int,
    val statusSeverityDescription: String,
    val reason: String,
)
