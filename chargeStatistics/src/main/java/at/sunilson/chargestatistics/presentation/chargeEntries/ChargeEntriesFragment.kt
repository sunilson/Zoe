package at.sunilson.chargestatistics.presentation.chargeEntries

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.ChargeEntriesFragmentBinding
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargestatistics.presentation.overview.ChargeStatisticsOverviewFragment
import at.sunilson.core.extensions.isSameDay
import at.sunilson.core.extensions.isSameMonth
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.formatPattern
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ChargeEntriesFragment private constructor() :
    Fragment(R.layout.charge_entries_fragment) {

    private val binding by viewBinding(ChargeEntriesFragmentBinding::bind)
    private val viewModel by viewModels<ChargeEntriesViewModel>()

    private val vin: String
        get() = requireNotNull(requireArguments().getString("vin"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadChargeProcedures(vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        binding.recyclerView.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f)).apply {
            addDuration = 300L
            removeDuration = 300L
        }

        binding.manageButton.setOnClickListener {
            (parentFragment as? ChargeStatisticsOverviewFragment)?.switchToPosition(3)
        }

        Insetter.builder().applySystemWindowInsetsToPadding(Side.TOP).applyToView(binding.root)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.manageButton.isVisible = state.chargingProcedures.isEmpty()
                binding.noChargesText.isVisible = state.chargingProcedures.isEmpty()

                binding.recyclerView.withModels {
                    var lastProcedure: ChargingProcedure? = null
                    state.chargingProcedures.forEach { chargeProcedure ->
                        chargeProcedureEntry {
                            id(chargeProcedure.startTime.toEpochSecond())
                            if (lastProcedure?.startTime?.isSameDay(chargeProcedure.startTime) != true) {
                                sectionHeader(chargeProcedure.startTime.formatPattern("dd.MM.YYYY"))
                            }
                            chargingProcedure(chargeProcedure)
                        }

                        lastProcedure = chargeProcedure
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(vin: String) = ChargeEntriesFragment().apply {
            arguments = bundleOf("vin" to vin)
        }
    }
}