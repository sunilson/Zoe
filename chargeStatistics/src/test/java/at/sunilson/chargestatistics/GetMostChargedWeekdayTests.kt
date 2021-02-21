package at.sunilson.chargestatistics

import at.sunilson.chargestatistics.domain.GetMostChargedWeekday
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.presentationcore.extensions.formatPattern
import at.sunilson.testcore.BaseUnitTest
import at.sunilson.vehiclecore.domain.entities.Vehicle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.ZoneOffset

class GetMostChargedWeekdayTests : BaseUnitTest() {

    private lateinit var useCase: GetMostChargedWeekday

    @BeforeEach
    fun before() {
        useCase = GetMostChargedWeekday()
        useCase.dispatcher = Dispatchers.Main
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun testCorrectResult(params: List<ChargeTrackingPoint>, expectedResult: String?) =
        runBlockingTest {
            val result = useCase(params).get()
            Assertions.assertEquals(expectedResult, result?.value)
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
                    mockPoint(true, LocalDate.parse("2020-10-04")),
                    mockPoint(true, LocalDate.parse("2020-10-04")),
                    mockPoint(true, LocalDate.parse("2020-10-02")),
                    mockPoint(false, LocalDate.parse("2020-10-03"))
                ),
                DayOfWeek.SUNDAY.formatPattern("EEEE")
            ),
            Arguments.of(
                listOf(
                    mockPoint(true, LocalDate.parse("2020-10-04")),
                    mockPoint(true, LocalDate.parse("2020-10-02")),
                    mockPoint(true, LocalDate.parse("2019-09-02")),
                    mockPoint(true, LocalDate.parse("2019-09-02")),
                    mockPoint(true, LocalDate.parse("2019-09-02")),
                    mockPoint(false, LocalDate.parse("2020-10-03"))
                ),
                DayOfWeek.SUNDAY.formatPattern("EEEE")
            ),
            Arguments.of(
                listOf(
                    mockPoint(true, LocalDate.parse("2020-10-04")),
                    mockPoint(true, LocalDate.parse("2020-10-04")),
                    mockPoint(true, LocalDate.parse("2020-10-02")),
                    mockPoint(true, LocalDate.parse("2019-09-02")),
                    mockPoint(true, LocalDate.parse("2019-09-09")),
                    mockPoint(false, LocalDate.parse("2020-10-03"))
                ),
                DayOfWeek.MONDAY.formatPattern("EEEE")
            )
        )

        private fun mockPoint(charging: Boolean, date: LocalDate) = ChargeTrackingPoint(
            "1",
            date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli(),
            Vehicle.BatteryStatus(
                1,
                1,
                1,
                1,
                false,
                if (charging) {
                    Vehicle.BatteryStatus.ChargeState.CHARGING
                } else {
                    Vehicle.BatteryStatus.ChargeState.NOT_CHARGING
                },
                1f,
                1
            ),
            1,
            null
        )
    }
}
