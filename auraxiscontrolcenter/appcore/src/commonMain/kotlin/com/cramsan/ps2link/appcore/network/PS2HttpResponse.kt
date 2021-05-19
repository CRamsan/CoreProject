package com.cramsan.ps2link.appcore.network

import com.cramsan.framework.assert.assert
import io.ktor.client.statement.HttpResponse

class PS2HttpResponse<Body> private constructor(
    val body: Body?,
    val rawResponse: HttpResponse? = null,
    val throwable: Throwable? = null
) {
    val code = rawResponse?.status?.value ?: 200
    val isSuccessful = throwable == null && code in 200..299

    companion object {
        fun <Body> success(body: Body, rawResponse: HttpResponse? = null): PS2HttpResponse<Body> = PS2HttpResponse(body, rawResponse)

        fun <Body> failure(rawResponse: HttpResponse?, throwable: Throwable?): PS2HttpResponse<Body> {
            assert(rawResponse != null || throwable != null, "PS2HttpResponse", "A rawResponse or throwable is needed.")
            return PS2HttpResponse(null, rawResponse, throwable)
        }

        fun <Orig, Result> process(response: PS2HttpResponse<Orig>, process: (Orig) -> Result): PS2HttpResponse<Result> {
            if (!response.isSuccessful) {
                return response.toFailure()
            }

            return try {
                response.toSuccess(process(response.requireBody()))
            } catch (throwable: Throwable) {
                response.toFailure(throwable)
            }
        }
    }
}

fun <Orig, Result> PS2HttpResponse<Orig>.toFailure() = toFailure<Orig, Result>(throwable)

fun <Orig, Result> PS2HttpResponse<Orig>.toFailure(throwable: Throwable?) = PS2HttpResponse.failure<Result>(rawResponse, throwable)

fun <Orig, Result> PS2HttpResponse<Orig>.toSuccess(body: Result) = PS2HttpResponse.success(body, rawResponse)

fun <Body> PS2HttpResponse<Body>.requireBody(): Body {
    assert(isSuccessful, "PS2HttpResponse", "Request needs to be successful")
    return requireNotNull(body)
}

fun <Orig, Unit> PS2HttpResponse<Orig>.onSuccess(process: (Orig) -> Unit): PS2HttpResponse<Orig> {
    return try {
        if (isSuccessful) {
            process(requireBody())
        }
        this
    } catch (throwable: Throwable) {
        PS2HttpResponse.failure(null, throwable)
    }
}

fun <Orig, Result> PS2HttpResponse<Orig>.process(process: (Orig) -> Result): PS2HttpResponse<Result> {
    return PS2HttpResponse.process(this, process)
}

fun <Orig, Result> List<PS2HttpResponse<Orig>>.processList(process: (Orig) -> Result): PS2HttpResponse<List<Result>> {
    return try {
        if (all { it.isSuccessful }) {
            val asdasd = PS2HttpResponse.success(map { process(it.requireBody()) })
            asdasd
        } else {
            PS2HttpResponse.failure(null, null)
        }
    } catch (throwable: Throwable) {
        PS2HttpResponse.failure(null, throwable)
    }
}
