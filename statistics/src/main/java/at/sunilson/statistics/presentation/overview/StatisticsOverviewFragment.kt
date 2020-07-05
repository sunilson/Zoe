package at.sunilson.statistics.presentation.overview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import at.sunilson.statistics.R
import at.sunilson.statistics.domain.StatisticsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticsOverviewFragment : Fragment(R.layout.fragment_statistics_overview) {

    @Inject
    lateinit var repository: StatisticsRepository

    private val args: StatisticsOverviewFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            repository.getChargeStatistics(args.vin).fold(
                {},
                {}
            )

            repository.getHVACHistory(args.vin).fold(
                {},
                {}
            )
        }
    }

}