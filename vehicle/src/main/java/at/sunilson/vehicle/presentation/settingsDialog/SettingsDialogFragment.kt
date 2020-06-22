package at.sunilson.vehicle.presentation.settingsDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import at.sunilson.presentationcore.base.viewBinding
import at.sunilson.vehicle.R
import at.sunilson.vehicle.databinding.DialogFragmentSettingsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsDialogFragment : BottomSheetDialogFragment() {

    val binding by viewBinding(DialogFragmentSettingsBinding::bind)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.openSettings.setOnClickListener {
            findNavController().navigate("https://zoe.app/settings".toUri())
        }
    }

}