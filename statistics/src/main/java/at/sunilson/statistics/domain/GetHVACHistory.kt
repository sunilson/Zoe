package at.sunilson.statistics.domain

import at.sunilson.core.usecases.AsyncUseCase
import com.github.kittinunf.result.coroutines.SuspendableResult
import javax.inject.Inject

class GetHVACHistory @Inject constructor(private val repository: StatisticsRepository) :
    AsyncUseCase<Unit, String>() {

    override suspend fun run(params: String) = SuspendableResult.of<Unit, Exception> {
        repository.getHVACHistory(params)
        Unit
    }
}
