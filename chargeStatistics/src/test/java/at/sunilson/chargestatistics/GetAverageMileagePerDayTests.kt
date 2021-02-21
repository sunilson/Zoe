package at.sunilson.chargestatistics

import at.sunilson.chargestatistics.domain.GetAverageMileagePerDay
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.di.NumberFormatModule
import at.sunilson.testcore.BaseUnitTest
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.LocalDate
import java.time.ZoneOffset

class GetAverageMileagePerDayTests : BaseUnitTest() {

    private lateinit var useCase: GetAverageMileagePerDay

    @BeforeEach
    fun before() {
        useCase = GetAverageMileagePerDay(NumberFormatModule.provideGermanNumberFormatter())
        useCase.dispatcher = Dispatchers.Main
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun testCorrectResult(params: List<ChargeTrackingPoint>, expectedResult: String?) =
        runBlockingTest {
            val result = useCase(params).get()
            assertEquals(expectedResult, result?.value)
        }

    companion object {
        @JvmStatic
        fun testData() = listOf(
            Arguments.of(
                listOf<ChargeTrackingPoint>(),
                null
            ),
            Arguments.of(
                listOf(
                    mockPoint(10, LocalDate.parse("1993-12-20")),
                    mockPoint(20, LocalDate.parse("1993-12-20"))
                ),
                "10 km"
            ),
            Arguments.of(
                listOf(
                    mockPoint(10, LocalDate.parse("1993-12-20")),
                    mockPoint(20, LocalDate.parse("1993-12-21"))
                ),
                "5 km"
            ),
            Arguments.of(
                listOf(
                    mockPoint(10, LocalDate.parse("2000-01-01")),
                    mockPoint(2000, LocalDate.parse("2001-01-01")),
                    mockPoint(5000, LocalDate.parse("2001-10-10")),
                    mockPoint(8000, LocalDate.parse("2010-04-06"))
                ),
                "2,13 km"
            )
        )

        private fun mockPoint(mileage: Int, date: LocalDate) = ChargeTrackingPoint(
            "1",
            date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            Vehicle.BatteryStatus(
                1,
                1,
                1,
                1,
                false,
                Vehicle.BatteryStatus.ChargeState.WATING_FOR_PLANNED_CHARGE,
                1f,
                1
            ),
            mileage,
            null
        )
    }
}
