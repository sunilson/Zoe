package at.sunilson.impressum.presentation

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import at.sunilson.impressum.R
import at.sunilson.impressum.databinding.FragmentImpressumBinding
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.presentationcore.base.viewBinding
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side

class ImpressumFragment : Fragment(R.layout.fragment_impressum) {

    private val binding by viewBinding(FragmentImpressumBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Insetter.builder().applySystemWindowInsetsToPadding(Side.TOP).applyToView(view)

        binding.backButton.setOnClickListener { findNavController().navigateUp() }

        binding.openSourceLicenceButton.setOnClickListener {
            startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
    }
}
