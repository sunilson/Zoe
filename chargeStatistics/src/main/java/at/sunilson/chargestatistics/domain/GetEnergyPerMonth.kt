package at.sunilson.chargestatistics.domain

import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.chargetracking.domain.entities.ChargeTrackingPoint
import at.sunilson.core.usecases.AsyncUseCase
import at.sunilson.ktx.datetime.formatPattern
import com.github.kittinunf.result.coroutines.SuspendableResult
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import javax.inject.Inject

internal class GetEnergyPerMonth @Inject constructor(
    private val extractDeChargingProcedures: ExtractDeChargingProcedures
) : AsyncUseCase<Statistic.Chart.Bar?, List<ChargeTrackingPoint>>() {
    override suspend fun run(params: List<ChargeTrackingPoint>) =
        SuspendableResult.of<Statistic.Chart.Bar?, Exception> {
            val chargeProcedures = extractDeChargingProcedures(params).get()
            if (chargeProcedures.isEmpty()) return@of null

            val epochDay = LocalDate.ofEpochDay(0)
            val groupedByMonth = chargeProcedures.groupBy {
                ChronoUnit.MONTHS.between(epochDay, it.startTime)
            }

            Statistic.Chart.Bar(
                "energyPerMonth",
                groupedByMonth.map { (dateOfMonth, procecures) ->
                    BarEntry(
                        dateOfMonth.toFloat(),
                        procecures.sumBy { it.energyLevelDifference }.toFloat()
                    )
                },
                1f,
                1f,
                "Energie pro Monat",
                yValueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return "${value.toInt()} kWh"
                    }
                },
                xValueFormatter = object : ValueFormatter() {
                    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                        return LocalDate
                            .ofEpochDay(0)
                            .plusMonths(value.toLong())
                            .formatPattern("MM.YYYY")
                    }
                }
            )
        }
}
