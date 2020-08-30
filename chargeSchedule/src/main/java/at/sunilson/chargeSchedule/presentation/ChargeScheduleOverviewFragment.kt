package at.sunilson.chargeSchedule.presentation

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.addCallback
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import at.sunilson.chargeSchedule.R
import at.sunilson.core.Do
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import at.sunilson.scheduleCore.databinding.FragmentScheduleOverviewBinding
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import at.sunilson.scheduleCore.presentation.scheduleEntry
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
internal class ChargeScheduleOverviewFragment :
    Fragment(R.layout.fragment_schedule_overview) {

    private val args by navArgs<ChargeScheduleOverviewFragmentArgs>()
    private val viewModel by viewModels<ChargeScheduleOverviewViewModel>()
    private val binding by viewBinding(FragmentScheduleOverviewBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.refreshSchedules(args.vin)

        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.TOP)
            .applyToView(binding.headerContainer)

        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refreshSchedules(args.vin) }
        binding.toggle.onToggledListener = { viewModel.toggleChargeMode(args.vin, !it) }
        binding.backButton.setOnClickListener { viewModel.askForSaveApproval(true) }
        binding.saveButton.setOnClickListener { viewModel.askForSaveApproval(false) }
        binding.recyclerview.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f)).apply {
            addDuration = 300L
            removeDuration = 300L
        }

        //Delay loading so transition is smooth
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(300)
            viewModel.loadChargeSchedules(args.vin)
        }

        observeState()
        observeEvents()

        setupHeaderAnimation(binding.headerContainer, binding.recyclerview, true)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.askForSaveApproval(true)
        }
    }

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(false)
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(R.color.white)
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                binding.toggle.isEnabled = !it.settingChargeMode && !it.schedulesUpdated
                binding.swipeRefreshLayout.isRefreshing = it.loading
                renderList(it.schedules)
                binding.toggle.toggled = it.chargeType != ScheduleType.ALWAYS

                if (it.schedulesUpdated) {
                    binding.saveButton.show()
                } else {
                    binding.saveButton.hide()
                }

                binding.disabledOverlay.isVisible =
                    it.chargeType != ScheduleType.SCHEDULED || it.settingChargeMode || it.loading
            }
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collect {
                Do exhaustive when (it) {
                    SettingChargeModeFailed -> requireContext().showToast("Konnte nicht geändert werden!")
                    UpdatingSchedulesFailed -> requireContext().showToast("Ladeprogramme konnten nicht angepasst werden! Bitte versuche es später noch einmal")
                    ChargeModeNowAlways -> requireContext().showToast("Ladeplanung ist jetzt inaktiv!")
                    is AskForSaveApproval -> askForSaveApproval(it.exit)
                    Exit -> findNavController().navigateUp()
                }
            }
        }
    }

    private fun askForSaveApproval(exit: Boolean = false) {
        binding.swipeRefreshLayout.isRefreshing = false
        MaterialAlertDialogBuilder(
            ContextThemeWrapper(
                requireContext(),
                R.style.AlertDialogTheme
            )
        )
            .setTitle("Änderungen speichern")
            .setMessage("Wollen Sie die Änderungen speichern?")
            .setNegativeButton("Verwerfen") { _, _ ->
                viewModel.deleteChangedSchedules(args.vin, exit)
            }
            .setPositiveButton("Speichern") { _, _ -> viewModel.saveSchedules(args.vin, exit) }
            .setNeutralButton("Abbrechen") { _, _ -> }
            .show()
    }

    private fun renderList(chargeSchedules: List<Schedule>) {
        binding.recyclerview.withModels {
            chargeSchedules.forEach { chargeSchedule ->
                scheduleEntry {
                    id(chargeSchedule.id)
                    schedule(chargeSchedule)
                    scheduleToggled { }
                    scheduleUpdated { viewModel.updateSchedules(it) }
                }
            }
        }
    }
}