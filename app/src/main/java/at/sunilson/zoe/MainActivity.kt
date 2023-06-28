package at.sunilson.zoe

import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.ShortcutManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.work.WorkManager
import at.sunilson.authentication.domain.IsLoggedInUseCase
import at.sunilson.authentication.domain.LogoutHandler
import at.sunilson.navigation.ActivityNavigator
import at.sunilson.navigation.ActivityNavigatorParams
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
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

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(sharedPreferences.getInt("theme", R.style.Base_Theme_Zoe_Blue))
        if (!checkLoggedIn()) return
        observeLogout()
        setContentView(R.layout.activity_main)
        requirePermissions()
    }

    private fun requirePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 0)
        }
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

        // Workmanager seems to continue work when you ask for work info, so we just ask for anything
        workManager.getWorkInfosForUniqueWorkLiveData("egal")
    }

    private fun observeLogout() {
        lifecycleScope.launchWhenCreated {
            logoutHandler.loggedOutEvent.collect {
                // On logout the shortcuts we created are not valid anymore!
                ContextCompat.getSystemService<ShortcutManager>(
                    this@MainActivity,
                    ShortcutManager::class.java
                )?.removeAllDynamicShortcuts()
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
