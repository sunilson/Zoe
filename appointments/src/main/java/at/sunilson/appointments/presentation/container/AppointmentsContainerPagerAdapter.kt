package at.sunilson.appointments.presentation.container

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import at.sunilson.appointments.presentation.appointments.AppointmentsFragment
import at.sunilson.appointments.presentation.history.HistoryFragment

internal class AppointmentsContainerPagerAdapter(fragment: Fragment, private val vin: String) :
    FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int) = when (position) {
        0 -> AppointmentsFragment.newInstance(vin)
        else -> HistoryFragment.newInstance(vin)
    }

    override fun getItemCount() = 2
}