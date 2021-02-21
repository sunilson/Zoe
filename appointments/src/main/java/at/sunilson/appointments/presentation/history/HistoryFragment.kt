package at.sunilson.appointments.presentation.history

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.appointments.R
import at.sunilson.appointments.databinding.FragmentHistoryBinding
import at.sunilson.appointments.domain.entities.Service
import at.sunilson.core.extensions.isSameMonth
import at.sunilson.presentationcore.ViewpagerFragmentParentWithAnimation
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.formatPattern
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
internal class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val binding by viewBinding(FragmentHistoryBinding::bind)
    private val viewModel by viewModels<HistoryViewModel>()
    private val vin: String
        get() = checkNotNull(requireArguments().getString("vin"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshHistory(vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        binding.refreshLayout.setOnRefreshListener { viewModel.viewCreated(vin) }

        binding.recyclerView.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f)).apply {
            addDuration = 300L
            removeDuration = 300L
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(300)
            viewModel.viewCreated(vin)
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.container.stateFlow.collect { state ->
                binding.refreshLayout.isRefreshing = state.loading
                setupList(state.services)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? ViewpagerFragmentParentWithAnimation)?.childBecameActive(null)
    }

    private fun setupList(services: List<Service>) {
        binding.recyclerView.withModels {

            var previousService: Service? = null

            services.forEachIndexed { index, service ->
                historyListItem {
                    id(index)
                    service(service)
                    headline(
                        if (previousService == null || !previousService!!.date.isSameMonth(service.date)) {
                            service.date.formatPattern("MMMM YYYY")
                        } else {
                            null
                        }
                    )
                }
                previousService = service
            }
        }
    }

    companion object {
        fun newInstance(vin: String) = HistoryFragment().apply {
            arguments = bundleOf("vin" to vin)
        }
    }
}
