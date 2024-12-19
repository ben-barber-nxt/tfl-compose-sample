package uk.co.next.stoppoint.usecases

data class DomainStopPoint(
    val id: String,
    val modesAndLines: List<DomainModesAndLines>,
    val name: String,
    val properties: List<DomainAdditionalProperties>
)

data class DomainAdditionalProperties(
    val category: String,
    val key: String,
    val value: String,
)

data class DomainModesAndLines(
    val modeId: String,
    val modeName: String,
    val lines: List<DomainLine>
)

data class DomainLine(
    val lineName: String,
    val uri: String,
)