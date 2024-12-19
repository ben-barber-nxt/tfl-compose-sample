package uk.co.next.repositories.core

sealed interface DomainResult<T> {
    data class Success<T>(val data: T) : DomainResult<T>
    data class Error<T>(val error: Throwable) : DomainResult<T>
    data class Loading<T>(val data: T? = null) : DomainResult<T>
}