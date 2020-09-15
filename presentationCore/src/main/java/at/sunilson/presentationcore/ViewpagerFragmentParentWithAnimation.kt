package at.sunilson.presentationcore

import androidx.recyclerview.widget.RecyclerView

interface ViewpagerFragmentParentWithHeaderAnimation {
    var currentUnregisterCallback: (() -> Unit)?
    fun childBecameActive(list: RecyclerView?)
}