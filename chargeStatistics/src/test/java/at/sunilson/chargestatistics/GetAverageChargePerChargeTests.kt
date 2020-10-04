package at.sunilson.chargestatistics

import at.sunilson.chargestatistics.domain.ExtractChargingProcedures
import at.sunilson.chargestatistics.domain.GetAverageChargePerCharge
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.core.di.NumberFormatModule
import at.sunilson.testcore.BaseUnitTest
import com.github.kittinunf.result.coroutines.SuspendableResult
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.time.ZonedDateTime

class GetAverageChargePerChargeTests : BaseUnitTest() {

    private lateinit var useCase: GetAverageChargePerCharge

    @MockK
    private lateinit var extractChargingProcedures: ExtractChargingProcedures

    @BeforeEach
    fun before() {
        useCase = GetAverageChargePerCharge(
            extractChargingProcedures,
            NumberFormatModule.provideGermanNumberFormatter()
        )
        useCase.dispatcher = Dispatchers.Main
    }

    @ParameterizedTest
    @MethodSource("testData")
    fun testCorrectResult(params: List<ChargingProcedure>, expectedResult: String?) =
        runBlockingTest {
            coEvery { extractChargingProcedures(any()) } returns SuspendableResult.of(params)
            val result = useCase(listOf()).get()
            Assertions.assertEquals(expectedResult, result?.value)
        }


    companion object {
        @JvmStatic
        fun testData() = listOf(
            Arguments.of(
                listOf<ChargingProcedure>(),
                null
            ),
            Arguments.of(
                listOf(mockProcedure(30)),
                "30 kWh"
            ),
            Arguments.of(
                listOf(
                    mockProcedure(40),
                    mockProcedure(30),
                    mockProcedure(50)
                ),
                "40 kWh"
            ),
        )

        private fun mockProcedure(difference: Int) = ChargingProcedure(
            0, 0, difference, ZonedDateTime.now(), ZonedDateTime.now()
        )
    }
}