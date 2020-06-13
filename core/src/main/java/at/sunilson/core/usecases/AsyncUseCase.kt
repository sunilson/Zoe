package at.sunilson.core.usecases

import com.github.kittinunf.result.coroutines.SuspendableResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class AsyncUseCase<out Result, in Params>() {

    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    abstract suspend fun run(params: Params): SuspendableResult<Result, Exception>
    suspend operator fun invoke(params: Params): SuspendableResult<Result, Exception> {
        return withContext(dispatcher) { run(params) }
    }
}
