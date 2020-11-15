package at.sunilson.scheduleCore.presentation

import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.activity.addCallback
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import at.sunilson.core.Do
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setNavigationBarThemeColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.nightMode
import at.sunilson.presentationcore.extensions.setupHeaderAnimation
import at.sunilson.scheduleCore.R
import at.sunilson.scheduleCore.databinding.FragmentScheduleOverviewBinding
import at.sunilson.scheduleCore.domain.entities.Schedule
import at.sunilson.scheduleCore.domain.entities.ScheduleType
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side
import jp.wasabeef.recyclerview.animators.ScaleInAnimator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

abstract class ScheduleFragment : Fragment(R.layout.fragment_schedule_overview) {

    private val binding by viewBinding(FragmentScheduleOverviewBinding::bind)
    protected abstract val vin: String
    protected abstract val viewModel: SchedulesViewModel
    protected open val isHvac: Boolean = false

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(requireContext().nightMode)
        setStatusBarColor(android.R.color.transparent)
        if (requireContext().nightMode) {
            setNavigationBarThemeColor(R.attr.colorSurface)
        } else {
            setNavigationBarColor(android.R.color.white)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeEvents()
        observeState()

        viewModel.refreshSchedules(vin)

        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.TOP)
            .applyToView(binding.headerContainer)

        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.refreshSchedules(vin) }
        binding.toggle.onToggledListener = { viewModel.toggleChargeMode(vin, !it) }
        binding.backButton.setOnClickListener { viewModel.askForSaveApproval(true) }
        binding.saveButton.setOnClickListener { viewModel.askForSaveApproval(false) }
        binding.recyclerview.itemAnimator = ScaleInAnimator(OvershootInterpolator(1f)).apply {
            addDuration = 300L
            removeDuration = 300L
        }

        //Delay loading so transition is smooth
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            delay(300)
            viewModel.loadChargeSchedules(vin)
        }

        setupHeaderAnimation(binding.headerContainer, binding.recyclerview)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.askForSaveApproval(
                true
            )
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
                viewModel.deleteChangedSchedules(vin, exit)
            }
            .setPositiveButton("Speichern") { _, _ -> viewModel.saveSchedules(vin, exit) }
            .setNeutralButton("Abbrechen") { _, _ -> }
            .show()
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collect {
                Do exhaustive when (it) {
                    SettingModeFailed -> if (!isHvac) {
                        requireContext().showToast("Konnte nicht geändert werden!")
                    } else {
                        MaterialAlertDialogBuilder(
                            ContextThemeWrapper(
                                requireContext(),
                                R.style.AlertDialogTheme
                            )
                        )
                            .setTitle(R.string.cant_change_in_app)
                            .setMessage(R.string.must_change_in_zoe_climate_controle)
                            .setPositiveButton("Ok") { _, _ -> }
                            .show()
                    }
                    UpdatingSchedulesFailed -> requireContext().showToast("Ladeprogramme konnten nicht angepasst werden! Bitte versuche es später noch einmal")
                    ModeNowAlways -> requireContext().showToast("Ladeplanung ist jetzt inaktiv!")
                    is AskForSaveApproval -> askForSaveApproval(it.exit)
                    Exit -> findNavController().navigateUp()
                }
            }
        }
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

    private fun renderList(chargeSchedules: List<Schedule>) {
        binding.recyclerview.withModels {
            chargeSchedules.forEach { chargeSchedule ->
                scheduleEntry {
                    id(chargeSchedule.id)
                    isHvac(isHvac)
                    schedule(chargeSchedule)
                    scheduleToggled { }
                    scheduleUpdated { viewModel.updateSchedules(it) }
                }
            }
        }
    }
}