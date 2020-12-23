package at.sunilson.chargestatistics.presentation.chargeEntries

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.ChargeEntriesFragmentBinding
import at.sunilson.chargestatistics.domain.entities.ChargingProcedure
import at.sunilson.chargestatistics.presentation.overview.ChargeStatisticsOverviewFragment
import at.sunilson.core.extensions.isSameDay
import at.sunilson.presentationcore.ViewpagerFragmentParentWithHeaderAnimation
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.formatPattern
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class ChargeEntriesFragment private constructor() :
    Fragment(R.layout.charge_entries_fragment) {

    private val binding by viewBinding(ChargeEntriesFragmentBinding::bind)
    private val viewModel by viewModels<ChargeEntriesViewModel>()
    private val adapter = ChargeEntriesAdapter()

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

        binding.recyclerView.adapter = adapter

        binding.manageButton.setOnClickListener {
            (parentFragment as? ChargeStatisticsOverviewFragment)?.switchToPosition(3)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest {
                adapter.loadStateFlow.collectLatest {
                    val notLoadingAndEmpty =
                        it.refresh is LoadState.NotLoading && adapter.itemCount == 0
                    binding.manageButton.isVisible = notLoadingAndEmpty
                    binding.noChargesText.isVisible = notLoadingAndEmpty
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                adapter.submitData(state.chargingProcedures)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? ViewpagerFragmentParentWithHeaderAnimation)?.childBecameActive(binding.recyclerView)
    }

    companion object {
        fun newInstance(vin: String) = ChargeEntriesFragment().apply {
            arguments = bundleOf("vin" to vin)
        }
    }
}