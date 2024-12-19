package uk.co.next.tflsample.api.tfl.models

import kotlinx.serialization.Serializable

@Serializable
data class ApiStopPoint(
    val naptanId: String,
    val modes: List<String>,
    val icsCode: String,
    val stopType: String,
    val stationNaptan: String? = null,
    val lines: List<ApiIdentifier>,
    val lineGroup: List<ApiLineGroup>,
    val lineModeGroups: List<ApiLineModeGroup>,
    val status: Boolean,
    val id: String,
    val commonName: String,
    val placeType: String,
    val additionalProperties: List<ApiAdditionalProperties>,
    val children: List<ApiStopPoint>,
    val lat: Double,
    val lon: Double,
)

@Serializable
data class ApiAdditionalProperties(
    val category: String,
    val key: String,
    val sourceSystemKey: String,
    val value: String,
)

@Serializable
data class ApiLineModeGroup(
    val modeName: String,
    val lineIdentifier: List<String>,
)

@Serializable
data class ApiIdentifier(
    val id: String,
    val name: String,
    val uri: String,
    val type: String,
    val routeType: String,
    val status: String,
)

@Serializable
data class ApiLineGroup(
    val stationAtcoCode: String? = null,
    val lineIdentifier: List<String>,
)