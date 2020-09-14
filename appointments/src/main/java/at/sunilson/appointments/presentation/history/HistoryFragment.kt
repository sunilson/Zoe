package at.sunilson.appointments.presentation.history

import androidx.fragment.app.Fragment
import at.sunilson.appointments.R

internal class HistoryFragment: Fragment(R.layout.fragment_history) {


    companion object {
        fun newInstance(vin: String) = HistoryFragment()
    }
}