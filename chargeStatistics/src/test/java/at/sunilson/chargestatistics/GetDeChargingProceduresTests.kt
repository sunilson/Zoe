package at.sunilson.chargestatistics

import at.sunilson.chargestatistics.domain.ExtractDeChargingProcedures
import at.sunilson.chargestatistics.domain.GetDeChargingProcedures
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
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

class GetDeChargingProceduresTests : BaseUnitTest() {
    private lateinit var extractUseCase: ExtractDeChargingProcedures
    private lateinit var useCase: GetDeChargingProcedures

    @MockK
    private lateinit var getAllChargeTrackingPoints: GetAllChargeTrackingPoints

    @BeforeEach
    fun before() {
        extractUseCase = ExtractDeChargingProcedures()
        useCase = GetDeChargingProcedures(getAllChargeTrackingPoints, extractUseCase)
        useCase.dispatcher = Dispatchers.Main
        extractUseCase.dispatcher = Dispatchers.Main
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
        private fun batteryStatus(level: Int, energy: Int, charging: Boolean) =
            Vehicle.BatteryStatus(
                level,
                0,
                0,
                energy,
                true,
                if (charging) Vehicle.BatteryStatus.ChargeState.CHARGING else Vehicle.BatteryStatus.ChargeState.NOT_CHARGING,
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
                    ChargeTrackingPoint("1", 0L, batteryStatus(10, 10, false), 0),
                    ChargeTrackingPoint("1", 1L, batteryStatus(10, 10, false), 0),
                    ChargeTrackingPoint("1", 2L, batteryStatus(10, 10, true), 0),
                    ChargeTrackingPoint("1", 3L, batteryStatus(8, 8, true), 5),
                    ChargeTrackingPoint("1", 4L, batteryStatus(6, 7, false), 10),
                    ChargeTrackingPoint("1", 5L, batteryStatus(10, 10, false), 10)
                ),
                listOf(
                    DeChargingProcedure(
                        10,
                        6,
                        3,
                        Instant.ofEpochMilli(2L).atZone(ZoneId.systemDefault()),
                        Instant.ofEpochMilli(4L).atZone(ZoneId.systemDefault()),
                        10
                    )
                )
            )
        )
    }
}