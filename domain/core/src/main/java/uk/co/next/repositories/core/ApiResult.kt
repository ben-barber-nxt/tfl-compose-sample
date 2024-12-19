package uk.co.next.repositories.core

sealed interface ApiResult<T> {
    data class Success<T>(val data: T) : ApiResult<T>
    data class Error<T>(val error: Throwable) : ApiResult<T>
    data class Loading<T>(val data: T? = null) : ApiResult<T>
}