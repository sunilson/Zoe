package at.sunilson.chargestatistics.presentation.overview

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import at.sunilson.chargestatistics.presentation.entries.EntriesFragment
import at.sunilson.chargestatistics.presentation.manage.ManageFragment
import at.sunilson.chargestatistics.presentation.statistics.StatisticsFragment

class ViewPagerAdapter(fragment: Fragment, private val vin: String) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int) = when (position) {
        0 -> EntriesFragment.newInstance(vin)
        1 -> StatisticsFragment()
        else -> ManageFragment()
    }

    override fun getItemCount() = 3

}