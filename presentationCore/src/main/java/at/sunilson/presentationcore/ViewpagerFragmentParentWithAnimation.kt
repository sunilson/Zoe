package at.sunilson.presentationcore

import androidx.recyclerview.widget.RecyclerView

interface ViewpagerFragmentParentWithAnimation {
    var currentUnregisterCallback: (() -> Unit)?
    fun childBecameActive(list: RecyclerView?)
}
