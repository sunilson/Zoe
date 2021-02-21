package at.sunilson.statistics.data

import android.content.SharedPreferences
import at.sunilson.authentication.data.AuthSharedPrefConstants
import at.sunilson.statistics.domain.StatisticsRepository
import com.github.kittinunf.result.coroutines.SuspendableResult
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StatisticsRepositoryImpl @Inject constructor(
    private val statisticsService: VehicleStatisticsService,
    private val sharedPreferences: SharedPreferences
) : StatisticsRepository {

    override suspend fun getChargeHistory(vin: String): SuspendableResult<Unit, Exception> {
        TODO("Not yet implemented")
    }

    override suspend fun getChargeStatistics(vin: String) = SuspendableResult.of<Unit, Exception> {
        val formatter = DateTimeFormatter.ofPattern("YYYYMM").withZone(ZoneId.systemDefault())
        val end = LocalDate.now().minusDays(1)
        val start = LocalDate.now().minusMonths(2)

        statisticsService.getChargeStatistics(
            kamereonAccountID,
            vin,
            formatter.format(start),
            formatter.format(end)
        )
        Unit
    }

    override suspend fun getHVACHistory(vin: String) = SuspendableResult.of<Unit, Exception> {
        val formatter = DateTimeFormatter.ofPattern("YYYYMMdd").withZone(ZoneId.systemDefault())
        val end = LocalDate.now().minusDays(1)
        val start = LocalDate.now().minusDays(7)

        statisticsService.getChargeHistory(
            kamereonAccountID,
            vin,
            formatter.format(start),
            formatter.format(end)
        )
        Unit
    }

    private val kamereonAccountID: String
        get() = requireNotNull(
            sharedPreferences.getString(
                AuthSharedPrefConstants.KAMEREON_ACCOUNT_ID,
                null
            )
        )
}
