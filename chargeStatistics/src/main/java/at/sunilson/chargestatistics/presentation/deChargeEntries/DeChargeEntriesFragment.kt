package at.sunilson.chargestatistics.presentation.deChargeEntries

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.DeChargeEntriesFragmentBinding
import at.sunilson.chargestatistics.domain.entities.DeChargingProcedure
import at.sunilson.chargestatistics.presentation.overview.ChargeStatisticsOverviewFragment
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
internal class DeChargeEntriesFragment private constructor() :
    Fragment(R.layout.de_charge_entries_fragment) {

    private val binding by viewBinding(DeChargeEntriesFragmentBinding::bind)
    private val viewModel by viewModels<DeChargeEntriesViewModel>()

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

        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.TOP)
            .applyToView(binding.root)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.manageButton.isVisible = state.deChargingProcedures.isEmpty()
                binding.noChargesText.isVisible = state.deChargingProcedures.isEmpty()

                binding.recyclerView.withModels {
                    var lastProcedure: DeChargingProcedure? = null
                    state.deChargingProcedures.forEach { chargeProcedure ->
                        deChargeProcedureEntry {
                            id(chargeProcedure.startTime.toEpochSecond())
                            chargingProcedure(chargeProcedure)
                            if (lastProcedure?.startTime?.isSameMonth(chargeProcedure.startTime) != true) {
                                sectionHeader(chargeProcedure.startTime.formatPattern("MM.YYYY"))
                            }
                            onItemClick { }
                        }

                        lastProcedure = chargeProcedure
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(vin: String) = DeChargeEntriesFragment().apply {
            arguments = bundleOf("vin" to vin)
        }
    }
}