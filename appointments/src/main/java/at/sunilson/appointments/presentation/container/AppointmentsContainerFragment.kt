package at.sunilson.appointments.presentation.container

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import at.sunilson.appointments.R
import at.sunilson.appointments.databinding.FragmentAppointmentsContainerBinding
import at.sunilson.ktx.fragment.drawBelowStatusBar
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.presentationcore.extensions.updateHeaderAnimationWithViewpager
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side

@AndroidEntryPoint
internal class AppointmentsContainerFragment : Fragment(R.layout.fragment_appointments_container) {

    private val binding by viewBinding(FragmentAppointmentsContainerBinding::bind)
    private val args by navArgs<AppointmentsContainerFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Insetter
            .builder()
            .applySystemWindowInsetsToPadding(Side.TOP)
            .applyToView(binding.topContainer)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        binding.viewpager.adapter = AppointmentsContainerPagerAdapter(this, args.vin)
        binding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> binding.bottomNavigation.selectedItemId = R.id.appointments
                    else -> binding.bottomNavigation.selectedItemId = R.id.history
                }
                updateHeaderAnimationWithViewpager(position, binding.topContainer)
            }
        })
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            val position = when (it.itemId) {
                R.id.appointments -> 0
                else -> 1
            }
            binding.viewpager.currentItem = position
            updateHeaderAnimationWithViewpager(position, binding.topContainer)
            true
        }
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(android.R.color.transparent)
        setNavigationBarColor(android.R.color.white)
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
        drawBelowStatusBar()
    }
}