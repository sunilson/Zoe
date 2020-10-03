package at.sunilson.chargestatistics.presentation.statistics

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.StatisticsFragmentBinding
import at.sunilson.chargestatistics.domain.entities.ChartData
import at.sunilson.chargestatistics.presentation.overview.ChargeStatisticsOverviewFragment
import at.sunilson.chargestatistics.presentation.statistics.epoxy.barChart
import at.sunilson.chargestatistics.presentation.statistics.epoxy.chartExplanation
import at.sunilson.chargestatistics.presentation.statistics.epoxy.lineChart
import at.sunilson.presentationcore.ViewpagerFragmentParentWithHeaderAnimation
import at.sunilson.presentationcore.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
internal class StatisticsFragment private constructor() : Fragment(R.layout.statistics_fragment) {

    private val vin: String
        get() = requireNotNull(requireArguments().getString("vin"))

    private val viewModel by viewModels<StatisticsViewModel>()
    private val binding by viewBinding(StatisticsFragmentBinding::bind)

    override fun onResume() {
        super.onResume()
        (parentFragment as? ViewpagerFragmentParentWithHeaderAnimation)?.childBecameActive(binding.recyclerView)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadChartEntries(vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        binding.manageButton.setOnClickListener {
            (parentFragment as? ChargeStatisticsOverviewFragment)?.switchToPosition(3)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                binding.noChargesText.isVisible = state.entriesList.isEmpty()
                binding.manageButton.isVisible = state.entriesList.isEmpty()
                renderList(state.entriesList)
            }
        }
    }

    private fun renderList(entriesList: List<ChartData<*>>) {
        binding.recyclerView.withModels {
            chartExplanation { id("chartExplanation") }
            entriesList.forEach { lineChartData ->
                when (lineChartData) {
                    is ChartData.Line -> lineChart {
                        id(lineChartData.id)
                        data(lineChartData)
                    }
                    is ChartData.Bar -> barChart {
                        id(lineChartData.id)
                        data(lineChartData)
                    }
                }

            }
        }
    }

    companion object {
        fun newInstance(vin: String) = StatisticsFragment().apply {
            arguments = bundleOf("vin" to vin)
        }
    }
}