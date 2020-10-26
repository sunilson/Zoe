package at.sunilson.presentationcore.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import at.sunilson.presentationcore.R

class ProgressDialogFragment : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var cancelTries = 0
        dialog?.setOnKeyListener { _, _, _ ->
            // if true it will cancel on the current try
            isCancelable = cancelTries++ > CANCELABLE_THRESHOLD
            false
        }
    }

    companion object {
        const val CANCELABLE_THRESHOLD = 2
    }
}