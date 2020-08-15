package at.sunilson.zoe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.work.WorkManager
import at.sunilson.authentication.domain.IsLoggedInUseCase
import at.sunilson.authentication.domain.LogoutHandler
import at.sunilson.navigation.ActivityNavigator
import at.sunilson.navigation.ActivityNavigatorParams
import com.google.firebase.crashlytics.FirebaseCrashlytics
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

    @Inject
    lateinit var workManager: WorkManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkLoggedIn()) return
        observeLogout()
        setContentView(R.layout.activity_main)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        val navController = try {
            findNavController(R.id.nav_host_fragment)
        } catch (error: Exception) {
            return
        }

        navController.handleDeepLink(intent)
    }

    override fun onResume() {
        super.onResume()

        //Workmanager seems to continue work when you ask for work info, so we just ask for anything
        workManager.getWorkInfosForUniqueWorkLiveData("egal")
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