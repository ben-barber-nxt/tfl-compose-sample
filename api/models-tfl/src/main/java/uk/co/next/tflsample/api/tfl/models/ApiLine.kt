package uk.co.next.tflsample.api.tfl.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiLine(
    val id: String,
    val name: String,
    val modeName: String,
    val created: String,
    val modified: String,
    val lineStatuses: List<ApiLineStatus>
)

@Serializable
data class ApiLineStatus(
    val id: Int,
    val lineId: String? = null,
    val statusSeverity: Int,
    val statusSeverityDescription: String,
    val reason: String? = null,
    val created: String,
    val validityPeriod: List<ApiValidityPeriod> = emptyList(),
    val disruption: ApiDisruption? = null
)

@Serializable
data class ApiValidityPeriod(
    val fromDate: String,
    val toDate: String,
    val isNow: Boolean,
)

@Serializable
data class ApiDisruption(
    val category: String,
    val categoryDescription: String,
    val description: String,
    val created: String? = null,
    val closureText: String? = null,
)
