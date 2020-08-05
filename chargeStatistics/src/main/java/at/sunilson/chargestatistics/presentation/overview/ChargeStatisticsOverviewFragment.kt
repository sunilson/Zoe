package at.sunilson.chargestatistics.presentation.overview

import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.ChargeStatisticsOverviewFragmentBinding
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChargeStatisticsOverviewFragment : Fragment(R.layout.charge_statistics_overview_fragment) {

    private val viewModel by viewModels<ChargeStatisticsOverviewViewModel>()
    private val binding by viewBinding(ChargeStatisticsOverviewFragmentBinding::bind)
    private val args by navArgs<ChargeStatisticsOverviewFragmentArgs>()

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
        setStatusBarColor(R.color.white)
        setNavigationBarColor(R.color.white)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkBatteryOptimization()

        binding.viewpager.adapter = ViewPagerAdapter(this, args.vin)
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigation.selectedItemId = when (position) {
                    0 -> R.id.charge_entries
                    1 -> R.id.de_charge_entries
                    2 -> R.id.statistics
                    else -> R.id.manage
                }
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.charge_entries -> binding.viewpager.setCurrentItem(0, true)
                R.id.de_charge_entries -> binding.viewpager.setCurrentItem(1, true)
                R.id.statistics -> binding.viewpager.setCurrentItem(2, true)
                else -> binding.viewpager.setCurrentItem(3, true)
            }
            true
        }
    }

    private fun checkBatteryOptimization() {
        val powerManager = requireActivity().getSystemService(POWER_SERVICE) as PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(requireActivity().packageName)) {
            MaterialAlertDialogBuilder(
                ContextThemeWrapper(
                    requireContext(),
                    R.style.AlertDialogTheme
                )
            ).setTitle("Whitelist")
                .setMessage("Damit das Tracking zuverlässig funktioniert, deaktiviere bitte die Batterie Optimierung für diese App.")
                .setPositiveButton("Ok") { _, _ ->
                    startActivity(Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
                }
                .setNegativeButton("Abbrechen") { _, _ -> }
                .show()
        }
    }

    fun switchToPosition(position: Int) {
        check(position < 4)
        binding.viewpager.setCurrentItem(position, true)
    }
}