package at.sunilson.chargestatistics.presentation.overview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import at.sunilson.chargestatistics.R
import at.sunilson.chargestatistics.databinding.ChargeStatisticsOverviewFragmentBinding
import at.sunilson.presentationcore.base.viewBinding
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChargeStatisticsOverviewFragment : Fragment(R.layout.charge_statistics_overview_fragment) {

    private val viewModel by viewModels<ChargeStatisticsOverviewViewModel>()
    private val binding by viewBinding(ChargeStatisticsOverviewFragmentBinding::bind)
    private val args by navArgs<ChargeStatisticsOverviewFragmentArgs>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val forward = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        enterTransition = forward

        val backward = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        returnTransition = backward
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewpager.adapter = ViewPagerAdapter(this, args.vin)
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.bottomNavigation.selectedItemId = when (position) {
                    0 -> R.id.entries
                    1 -> R.id.statistics
                    else -> R.id.manage
                }
            }
        })

        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.entries -> binding.viewpager.setCurrentItem(0, true)
                R.id.statistics -> binding.viewpager.setCurrentItem(1, true)
                else -> binding.viewpager.setCurrentItem(2, true)
            }
            true
        }
    }

    private fun renderList() {

    }
}