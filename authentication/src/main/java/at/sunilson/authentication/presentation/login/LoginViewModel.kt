package at.sunilson.authentication.presentation.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import at.sunilson.authentication.domain.LoginUseCase
import at.sunilson.authentication.domain.entities.LoginParams
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber

class LoginState
sealed class LoginSideEffects {
    object LoginSuccess : LoginSideEffects()
    object LoginFailure : LoginSideEffects()
}

internal class LoginViewModel @ViewModelInject constructor(private val loginUseCase: LoginUseCase) :
    ContainerHost<LoginState, LoginSideEffects>, ViewModel() {

    override val container = container<LoginState, LoginSideEffects>(LoginState())

    fun loginClicked(username: String, password: String) = intent {
        loginUseCase(LoginParams(username, password, false)).fold(
            {
                postSideEffect(LoginSideEffects.LoginSuccess)
                Timber.tag(LOG_TAG).d("Login success!")
            },
            {
                postSideEffect(LoginSideEffects.LoginFailure)
                Timber.tag(LOG_TAG).e(it, "Login failure!")
            }
        )
    }

    companion object {
        const val LOG_TAG = "LoginViewModel"
    }
}
