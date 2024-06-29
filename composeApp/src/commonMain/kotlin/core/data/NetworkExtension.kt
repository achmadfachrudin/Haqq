package core.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.request
import io.ktor.serialization.JsonConvertException
import kotlinx.serialization.SerializationException

suspend inline fun <reified T> HttpClient.safeRequest(block: HttpRequestBuilder.() -> Unit): ApiResponse<T> {
    var responseCode = 0

    return try {
        val response = request { block() }
        responseCode = response.status.value
        ApiResponse.Success(response.body())
    } catch (exception: ClientRequestException) {
        val statusCode = exception.response.status.value
        val statusDesc = exception.response.status.description

        ApiResponse.Error(
            code = statusCode,
            body = exception.response.body(),
            message = "[Error-$statusCode-$statusDesc](${exception.message})",
        )
    } catch (exception: JsonConvertException) {
        val statusDesc = "JsonConvert"

        ApiResponse.Error(
            code = responseCode,
            body = exception.stackTraceToString(),
            message = "[Error-$responseCode-$statusDesc]${exception.message}",
        )
    } catch (exception: SerializationException) {
        val statusDesc = "Serialization"

        ApiResponse.Error(
            code = responseCode,
            body = exception.stackTraceToString(),
            message = "[Error-$responseCode-$statusDesc]${exception.message}",
        )
    } catch (exception: Exception) {
        val statusDesc = "Generic"

        ApiResponse.Error(
            code = responseCode,
            body = exception.stackTraceToString(),
            message = "[Error-$responseCode-$statusDesc](${exception.message})",
        )
    }
}
