package at.sunilson.settings.presentation.settingsOverview

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.settings.R
import at.sunilson.settings.databinding.FragmentSettingsOverviewBinding
import dagger.hilt.android.AndroidEntryPoint
import dev.chrisbanes.insetter.Insetter
import dev.chrisbanes.insetter.Side

@AndroidEntryPoint
class SettingsOverviewFragment : Fragment(R.layout.fragment_settings_overview) {

    private val binding by viewBinding(FragmentSettingsOverviewBinding::bind)
    private val viewModel by viewModels<SettingsOverviewViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Insetter.builder().applySystemWindowInsetsToPadding(Side.TOP).applyToView(view)
    }
}
