package at.sunilson.contracts.presentation

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.sunilson.contracts.R
import at.sunilson.contracts.databinding.FragmentContractsBinding
import at.sunilson.contracts.domain.entities.Contract
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
internal class ContractsFragment : Fragment(R.layout.fragment_contracts) {

    private val viewModel by viewModels<ContractsViewModel>()
    private val binding by viewBinding(FragmentContractsBinding::bind)
    private val args by navArgs<ContractsFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.refreshConracts(args.vin)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()

        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.TOP)
            .applyToView(binding.topContainer)

        setupHeaderAnimation(binding.topContainer, binding.recyclerView, true)
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
        binding.refreshLayout.setOnRefreshListener { viewModel.refreshConracts(args.vin) }
        binding.refreshLayout.isEnabled = true

        binding.recyclerView.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f)).apply {
            addDuration = 300L
            removeDuration = 300L
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(300)
            viewModel.loadContracts(args.vin)
        }
    }

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(false)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                binding.refreshLayout.isRefreshing = it.loading
                renderList(it.contracts)
            }
        }
    }

    private fun renderList(contracts: List<Contract>) {
        binding.recyclerView.withModels {
            contracts.forEach { contract ->
                contractListItem {
                    id(contract.id)
                    contract(contract)
                }
            }
        }
    }
}