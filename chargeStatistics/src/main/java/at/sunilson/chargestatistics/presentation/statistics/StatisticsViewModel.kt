package at.sunilson.chargestatistics.presentation.statistics

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.sunilson.chargestatistics.domain.GetStatticsChartEntries
import at.sunilson.chargestatistics.domain.entities.Statistic
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container

internal data class StatisticsState(val entriesList: List<Statistic> = listOf())
internal sealed class StatisticsSideEffects

internal class StatisticsViewModel @ViewModelInject constructor(private val getStatticsChartEntries: GetStatticsChartEntries) :
    ViewModel(), ContainerHost<StatisticsState, StatisticsSideEffects> {

    override val container = container<StatisticsState, StatisticsSideEffects>(StatisticsState())

    private var chartJob: Job? = null

    fun viewCreated(vin: String) {
        chartJob?.cancel()
        chartJob = viewModelScope.launch {
            getStatticsChartEntries(vin).collect {
                intent { reduce { state.copy(entriesList = it) } }
            }
        }
    }
}