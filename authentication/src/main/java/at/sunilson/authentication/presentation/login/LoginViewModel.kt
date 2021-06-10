package at.sunilson.authentication.presentation.login

import androidx.lifecycle.ViewModel
import at.sunilson.authentication.domain.LoginUseCase
import at.sunilson.authentication.domain.entities.LoginParams
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

class LoginState
sealed class LoginSideEffects {
    object LoginSuccess : LoginSideEffects()
    object LoginFailure : LoginSideEffects()
}

@HiltViewModel
internal class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) :
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
