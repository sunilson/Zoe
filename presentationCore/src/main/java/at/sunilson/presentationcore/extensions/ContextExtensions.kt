package at.sunilson.presentationcore.extensions

import android.content.Context
import android.content.res.Configuration

val Context.nightMode: Boolean
    get() = when (resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK)) {
        Configuration.UI_MODE_NIGHT_YES -> true
        else -> false
    }
