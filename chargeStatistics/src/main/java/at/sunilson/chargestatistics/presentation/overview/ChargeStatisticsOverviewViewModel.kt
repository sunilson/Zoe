package at.sunilson.chargestatistics.presentation.overview

import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel

data class ChargeStatisticsOverviewState(val loading: Boolean = false)
sealed class ChargeStatisticsOverviewEvent


class ChargeStatisticsOverviewViewModel :
    UniDirectionalViewModel<ChargeStatisticsOverviewState, ChargeStatisticsOverviewEvent>(
        ChargeStatisticsOverviewState()
    ) {

}