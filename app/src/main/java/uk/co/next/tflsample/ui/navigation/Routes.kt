package uk.co.next.tflsample.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object Home

@Serializable
data class StopPoint(
    val id: String,
    val name: String,
)