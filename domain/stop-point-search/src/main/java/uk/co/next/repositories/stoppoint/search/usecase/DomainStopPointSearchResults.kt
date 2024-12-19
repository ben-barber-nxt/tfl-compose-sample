package uk.co.next.repositories.stoppoint.search.usecase

data class DomainStopPointSearchResult(
    val id: String,
    val name: String,
    val modes: List<String> = emptyList(),
    val zone: String? = null,
)