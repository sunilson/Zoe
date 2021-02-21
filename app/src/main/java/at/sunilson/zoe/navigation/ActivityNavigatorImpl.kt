package at.sunilson.zoe.navigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import at.sunilson.authentication.presentation.AuthenticationActivity
import at.sunilson.navigation.ActivityNavigator
import at.sunilson.navigation.ActivityNavigatorParams
import at.sunilson.zoe.MainActivity

internal class ActivityNavigatorImpl : ActivityNavigator {

    private fun <T : AppCompatActivity> startActivity(
        target: Class<T>,
        params: ActivityNavigatorParams
    ) {
        params.origin.startActivity(Intent(params.origin, target).apply {
            if (params.finishOrigin) addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            data = params.originalIntent?.data
            params.block(this)
        })
        if (params.finishOrigin) params.origin.finish()
    }

    override fun startMainActivity(params: ActivityNavigatorParams) =
        startActivity(MainActivity::class.java, params)

    override fun startAuthenticationActivity(params: ActivityNavigatorParams) =
        startActivity(AuthenticationActivity::class.java, params)
}
