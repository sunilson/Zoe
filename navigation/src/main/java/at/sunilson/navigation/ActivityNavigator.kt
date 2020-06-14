package at.sunilson.navigation

import android.app.Activity
import android.content.Intent

data class ActivityNavigatorParams(
    val origin: Activity,
    val originalIntent: Intent? = null,
    val finishOrigin: Boolean = true,
    val block: Intent.() -> Unit = {}
)

interface ActivityNavigator {
    fun startMainActivity(params: ActivityNavigatorParams)
    fun startAuthenticationActivity(params: ActivityNavigatorParams)
}