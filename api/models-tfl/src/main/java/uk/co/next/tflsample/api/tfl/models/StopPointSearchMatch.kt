package uk.co.next.tflsample.api.tfl.models

import kotlinx.serialization.Serializable

@Serializable
data class StopPointSearchMatch(
    val id: String,
    val icsId: String? = null,
    val url: String? = null,
    val name: String,
    val lat: Double,
    val lon: Double,
    val modes: List<String> = emptyList(),
    val zone: String? = null,
)