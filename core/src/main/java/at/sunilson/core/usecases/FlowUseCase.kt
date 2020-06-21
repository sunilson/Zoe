package at.sunilson.core.usecases

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<out Result, in Params>() {

    var dispatcher: CoroutineDispatcher = Dispatchers.IO

    abstract fun run(params: Params): Flow<Result>
    operator fun invoke(params: Params): Flow<Result> {
        return run(params).flowOn(dispatcher)
    }
}
