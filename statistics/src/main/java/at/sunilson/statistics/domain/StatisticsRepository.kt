package at.sunilson.statistics.domain

import com.github.kittinunf.result.coroutines.SuspendableResult

interface StatisticsRepository {
    suspend fun getHVACHistory(vin: String): SuspendableResult<Unit, Exception>
}