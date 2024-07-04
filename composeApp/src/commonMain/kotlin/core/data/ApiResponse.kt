package core.data

sealed class ApiResponse<out T> {
    /**
     * Represents successful network responses (2xx).
     * @param body Response body
     */
    data class Success<T>(
        val body: T,
    ) : ApiResponse<T>()

    /**
     * Represents server errors.
     * @param code HTTP Status code
     * @param body Response body or throwable
     * @param message Custom error message
     */
    data class Error<Nothing>(
        val code: Int? = null,
        val body: String,
        val message: String,
    ) : ApiResponse<Nothing>()
}
