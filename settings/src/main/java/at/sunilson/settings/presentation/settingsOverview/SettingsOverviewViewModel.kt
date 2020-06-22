package at.sunilson.settings.presentation.settingsOverview

import androidx.hilt.lifecycle.ViewModelInject
import at.sunilson.authentication.domain.LogoutUseCase
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel

class SettingsOverviewState
sealed class SettingsOverviewEvents
object LoggedOut : SettingsOverviewEvents()

class SettingsOverviewViewModel @ViewModelInject constructor(private val logoutUseCase: LogoutUseCase) :
    UniDirectionalViewModel<SettingsOverviewState, SettingsOverviewEvents>(
        SettingsOverviewState()
    ) {

    fun logout() {
        logoutUseCase(Unit).fold(
            {
                sendEvent(LoggedOut)
            },
            {}
        )
    }

}