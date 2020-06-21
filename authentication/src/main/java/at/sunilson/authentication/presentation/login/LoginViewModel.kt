package at.sunilson.authentication.presentation.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import at.sunilson.authentication.domain.LoginUseCase
import at.sunilson.authentication.domain.entities.LoginParams
import at.sunilson.unidirectionalviewmodel.core.UniDirectionalViewModel
import kotlinx.coroutines.launch
import timber.log.Timber


class LoginState()
sealed class LoginEvent {
    object LoginSuccess : LoginEvent()
    object LoginFailure : LoginEvent()
}

internal class LoginViewModel @ViewModelInject constructor(
    private val loginUseCase: LoginUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : UniDirectionalViewModel<LoginState, LoginEvent>(LoginState()) {

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginUseCase(LoginParams(username, password, false)).fold(
                {
                    sendEvent(LoginEvent.LoginSuccess)
                    Timber.tag(LOG_TAG).d("Login success!")
                },
                {
                    sendEvent(LoginEvent.LoginFailure)
                    Timber.tag(LOG_TAG).e(it, "Login failure!")
                }
            )
        }
    }

    companion object {
        const val LOG_TAG = "LoginViewModel"
    }
}