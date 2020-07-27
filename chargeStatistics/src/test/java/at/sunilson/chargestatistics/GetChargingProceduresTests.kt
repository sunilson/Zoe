package at.sunilson.chargestatistics

import at.sunilson.chargestatistics.domain.GetChargingProcedures
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargetracking.domain.GetAllChargeTrackingPoints
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.testcore.BaseUnitTest
import at.sunilson.vehiclecore.domain.entities.Vehicle
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.Instant
import java.time.ZoneId

class GetChargingProceduresTests : BaseUnitTest() {

    private lateinit var useCase: GetChargingProcedures

    @MockK
    private lateinit var getAllChargeTrackingPoints: GetAllChargeTrackingPoints

    @BeforeEach
    fun before() {
        useCase = GetChargingProcedures(getAllChargeTrackingPoints)
        useCase.dispatcher = Dispatchers.Main
    }

    @ParameterizedTest
    @MethodSource("data")
    fun `Charges are correctly extracted`(
        chargePoints: List<ChargeTrackingPoint>,
        result: List<ChargingProcedure>
    ) = runBlockingTest {
        every { getAllChargeTrackingPoints(any()) } returns flowOf(chargePoints)
        assertEquals(result, useCase("123").first())
    }


    companion object {

        private fun batteryStatus(level: Int, energy: Int) = Vehicle.BatteryStatus(
            level,
            0,
            0,
            energy,
            true,
            Vehicle.BatteryStatus.ChargeState.CHARGING,
            0f,
            0
        )


        @JvmStatic
        fun data() = listOf(
            Arguments.of(
                listOf<ChargeTrackingPoint>(),
                listOf<ChargingProcedure>()
            ),
            Arguments.of(
                listOf(
                    ChargeTrackingPoint("123", 0L, batteryStatus(10, 10)),
                    ChargeTrackingPoint("123", 1L, batteryStatus(10, 10)),
                    ChargeTrackingPoint("123", 2L, batteryStatus(10, 10)),
                    ChargeTrackingPoint("123", 3L, batteryStatus(20, 15)),
                    ChargeTrackingPoint("123", 4L, batteryStatus(40, 20)),
                    ChargeTrackingPoint("123", 5L, batteryStatus(80, 43)),
                    ChargeTrackingPoint("123", 6L, batteryStatus(70, 35)),
                    ChargeTrackingPoint("123", 60L, batteryStatus(50, 20)),
                    ChargeTrackingPoint("123", 61L, batteryStatus(30, 10)),
                    ChargeTrackingPoint("123", 62L, batteryStatus(99, 50)),
                    ChargeTrackingPoint("123", 63L, batteryStatus(10, 10)),
                    ChargeTrackingPoint("123", 64L, batteryStatus(10, 10)),
                    ChargeTrackingPoint("123", 65L, batteryStatus(12, 12)),
                    ChargeTrackingPoint("123", 1256L, batteryStatus(13, 14))
                ),
                listOf(
                    ChargingProcedure(
                        70,
                        33,
                        Instant.ofEpochMilli(2L).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        Instant.ofEpochMilli(5L).atZone(ZoneId.systemDefault()).toLocalDateTime()
                    ),
                    ChargingProcedure(
                        69,
                        40,
                        Instant.ofEpochMilli(61L).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        Instant.ofEpochMilli(62L).atZone(ZoneId.systemDefault()).toLocalDateTime()
                    ),
                    ChargingProcedure(
                        3,
                        4,
                        Instant.ofEpochMilli(64L).atZone(ZoneId.systemDefault()).toLocalDateTime(),
                        Instant.ofEpochMilli(1256L).atZone(ZoneId.systemDefault()).toLocalDateTime()
                    )
                )
            )
        )
    }
}