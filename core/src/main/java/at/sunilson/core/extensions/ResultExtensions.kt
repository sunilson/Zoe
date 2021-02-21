package at.sunilson.core.extensions

import com.github.kittinunf.result.coroutines.SuspendableResult

suspend fun <V, E : Exception> SuspendableResult<V, E>.doOnFailure(block: suspend (E) -> Unit) =
    when (this) {
        is SuspendableResult.Success<V, E> -> this
        is SuspendableResult.Failure<V, E> -> SuspendableResult.of<V, E> {
            block(this.error)
            throw this.error
        }
    }

suspend fun <V, E : Exception> SuspendableResult<V, E>.doFinally(block: suspend () -> Unit): SuspendableResult<V, E> {
    block()
    return this
}
