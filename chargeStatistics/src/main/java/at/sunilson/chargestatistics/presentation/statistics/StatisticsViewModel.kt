package at.sunilson.chargestatistics.presentation.statistics

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.viewModelScope
import at.sunilson.chargestatistics.domain.GetStatsticsChartEntries
import at.sunilson.chargestatistics.domain.entities.Statistic
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

internal data class StatisticsState(val entriesList: List<Statistic> = listOf())
internal sealed class StatisticsEvents

internal class StatisticsViewModel @ViewModelInject constructor(private val getStatsticsChartEntries: GetStatsticsChartEntries) :
    UniDirectionalViewModel<StatisticsState, StatisticsEvents>(StatisticsState()) {

    private var chartJob: Job? = null

    fun loadChartEntries(vin: String) {
        chartJob?.cancel()
        chartJob = viewModelScope.launch {
            getStatsticsChartEntries(vin).collect {
                setState { copy(entriesList = it) }
            }
        }
    }
}