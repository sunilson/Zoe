package at.sunilson.settings.presentation.settingsOverview

import androidx.hilt.lifecycle.ViewModelInject
import at.sunilson.authentication.domain.LogoutUseCase
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel

class SettingsOverviewState
sealed class SettingsOverviewEvents

class SettingsOverviewViewModel @ViewModelInject constructor() : UniDirectionalViewModel<SettingsOverviewState, SettingsOverviewEvents>(
        SettingsOverviewState()
    )