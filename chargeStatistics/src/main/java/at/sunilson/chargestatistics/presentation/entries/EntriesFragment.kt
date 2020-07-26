package at.sunilson.chargestatistics.presentation.entries

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.EntriesFragmentBinding
import at.sunilson.chargestatistics.presentation.overview.ChargeStatisticsOverviewFragment
import at.sunilson.presentationcore.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.ZoneOffset

@AndroidEntryPoint
internal class EntriesFragment private constructor() : Fragment(R.layout.entries_fragment) {

    private val binding by viewBinding(EntriesFragmentBinding::bind)
    private val viewModel by viewModels<EntriesViewModel>()

    private val vin: String
        get() = requireNotNull(requireArguments().getString("vin"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadChargeProcedures(vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        binding.manageButton.setOnClickListener {
            (parentFragment as? ChargeStatisticsOverviewFragment)?.switchToPosition(2)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.manageButton.isVisible = state.chargingProcedures.isEmpty()
                binding.noChargesText.isVisible = state.chargingProcedures.isEmpty()

                binding.recyclerView.withModels {
                    state.chargingProcedures.forEach { chargeProcedure ->
                        chargeProcedureEntry {
                            id(chargeProcedure.startTime.toInstant(ZoneOffset.UTC).toEpochMilli())
                            chargingProcedure(chargeProcedure)
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(vin: String) = EntriesFragment().apply {
            arguments = bundleOf("vin" to vin)
        }
    }
}