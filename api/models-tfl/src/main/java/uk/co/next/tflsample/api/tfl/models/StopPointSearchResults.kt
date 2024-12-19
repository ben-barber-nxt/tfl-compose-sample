package uk.co.next.tflsample.api.tfl.models

import kotlinx.serialization.Serializable

@Serializable
data class StopPointSearchResults(
    val query: String,
    val from: Long = 0,
    val page: Long = 1,
    val pageSize: Long = 1,
    val provider: String? = null,
    val total: Long,
    val matches: List<StopPointSearchMatch>,
    val maxScore: Long = 0,
)

