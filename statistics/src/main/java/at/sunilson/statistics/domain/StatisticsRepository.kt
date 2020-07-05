package at.sunilson.statistics.domain

import com.github.kittinunf.result.coroutines.SuspendableResult

interface StatisticsRepository {
    suspend fun getHVACHistory(vin: String): SuspendableResult<Unit, Exception>
    suspend fun getChargeHistory(vin: String): SuspendableResult<Unit, Exception>
    suspend fun getChargeStatistics(vin: String): SuspendableResult<Unit, Exception>
}