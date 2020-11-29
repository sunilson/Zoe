package at.sunilson.vehicle.presentation.hvacStartDialog

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.core.Do
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setNavigationBarThemeColor
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.formatPattern
import at.sunilson.presentationcore.extensions.nightMode
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.HvacStartDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.slider.Slider
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.time.LocalTime

@AndroidEntryPoint
class StartHVACDialog : BottomSheetDialogFragment() {

    private val viewModel by viewModels<StartHVACViewModel>()
    private val binding by viewBinding(HvacStartDialogBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.hvac_start_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSlider()
        observeState()
        observeEvents()

        binding.time.setOnClickListener {
            val picker = MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).build()
            picker.show(childFragmentManager, null)
            picker.addOnPositiveButtonClickListener {
                viewModel.setTime(LocalTime.now().withHour(picker.hour).withMinute(picker.minute))
            }
        }

        binding.startButton.setOnClickListener { viewModel.startClicked() }
    }

    private fun setupSlider() {
        binding.temperatureSlider.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {}
            override fun onStopTrackingTouch(slider: Slider) {
                viewModel.setTemperature(slider.value)
            }
        })

        binding.temperatureSlider.addOnChangeListener { _, value, _ ->
            binding.temperature.text = getString(R.string.degrees, value.toInt().toString())
        }
    }

    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.events.collect {
                Do exhaustive when (it) {
                    StartHVACEvent.HVACStarted -> {
                        requireContext().showToast(R.string.hvac_started)
                        dismiss()
                    }
                    StartHVACEvent.HVACNotStarted -> {
                        requireContext().showToast(R.string.hvac_not_started)
                    }
                }
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                binding.temperature.text =
                    getString(R.string.degrees, it.temperature.toInt().toString())
                binding.temperatureSlider.value = it.temperature
                binding.time.text = if (it.startTime != null) {
                    it.startTime.formatPattern("HH:mm")
                } else {
                    getString(R.string.instant)
                }
            }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        setNavigationBarColor(android.R.color.transparent)
    }

    override fun onResume() {
        super.onResume()
        if (requireContext().nightMode) {
            setNavigationBarThemeColor(R.attr.colorSurface)
        } else {
            setNavigationBarColor(android.R.color.white)
        }
    }
}