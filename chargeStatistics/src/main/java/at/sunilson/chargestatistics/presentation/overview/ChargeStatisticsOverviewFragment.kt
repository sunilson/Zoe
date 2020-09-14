package at.sunilson.chargestatistics.presentation.overview

import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.View
import androidx.appcompat.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.ChargeStatisticsOverviewFragmentBinding
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.updateHeaderAnimationWithViewpager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side

@AndroidEntryPoint
class ChargeStatisticsOverviewFragment : Fragment(R.layout.charge_statistics_overview_fragment) {

    private val binding by viewBinding(ChargeStatisticsOverviewFragmentBinding::bind)
    private val args by navArgs<ChargeStatisticsOverviewFragmentArgs>()

    override fun onResume() {
        super.onResume()
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.white)
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkBatteryOptimization()
        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.TOP)
            .applyToView(binding.topContainer)
        setupViewpager()
        binding.backButton.setOnClickListener { findNavController().navigateUp() }
    }

    private fun setupViewpager() {
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
                updateHeaderAnimationWithViewpager(position, binding.topContainer)
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val position = when (it.itemId) {
                R.id.charge_entries -> 0
                R.id.de_charge_entries -> 1
                R.id.statistics -> 2
                else -> 3
            }
            binding.viewpager.setCurrentItem(position, true)
            updateHeaderAnimationWithViewpager(position, binding.topContainer)
            true
        }

        binding.viewpager.post {
            if (args.manage) {
                switchToPosition(3)
            }
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
        binding.viewpager.currentItem = position
    }
}