package at.sunilson.appointments.presentation.history

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.appointments.R
import at.sunilson.appointments.databinding.FragmentHistoryBinding
import at.sunilson.presentationcore.ViewpagerFragmentParentWithHeaderAnimation
import at.sunilson.presentationcore.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
internal class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val binding by viewBinding(FragmentHistoryBinding::bind)
    private val viewModel by viewModels<HistoryViewModel>()
    private val vin: String
        get() = checkNotNull(requireArguments().getString("vin"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.loadHistory(vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        binding.refreshLayout.setOnRefreshListener { viewModel.loadHistory(vin) }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect { state ->
                binding.refreshLayout.isRefreshing = state.loading
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (parentFragment as? ViewpagerFragmentParentWithHeaderAnimation)?.childBecameActive(null)
    }

    companion object {
        fun newInstance(vin: String) = HistoryFragment().apply {
            arguments = bundleOf("vin" to vin)
        }
    }
}