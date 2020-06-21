package at.sunilson.zoe

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import at.sunilson.authentication.domain.IsLoggedInUseCase
import at.sunilson.authentication.domain.LogoutHandler
import at.sunilson.navigation.ActivityNavigator
import at.sunilson.navigation.ActivityNavigatorParams
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var isLoggedInUseCase: IsLoggedInUseCase

    @Inject
    lateinit var activityNavigator: ActivityNavigator

    @Inject
    lateinit var logoutHandler: LogoutHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!checkLoggedIn()) return

        observeLogout()

        setContentView(R.layout.activity_main)
    }

    private fun observeLogout() {
        lifecycleScope.launchWhenCreated {
            logoutHandler.loggedOutEvent.collect {
                moveToLogin()
                cancel()
            }
        }
    }

    private fun checkLoggedIn(): Boolean {
        if (!isLoggedInUseCase(Unit).get()) {
            moveToLogin()
            return false
        }

        return true
    }

    private fun moveToLogin() {
        activityNavigator.startAuthenticationActivity(
            ActivityNavigatorParams(
                this,
                intent,
                true
            )
        )
    }
}