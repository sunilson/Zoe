package at.sunilson.authentication.presentation.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import at.sunilson.authentication.domain.LoginUseCase
import at.sunilson.authentication.domain.entities.LoginParams
import kotlinx.coroutines.launch
import timber.log.Timber


internal class LoginViewModel @ViewModelInject constructor(
    private val loginUseCase: LoginUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    fun login(username: String, password: String) {
        viewModelScope.launch {
            loginUseCase(LoginParams(username, password, false)).fold(
                {
                    Timber.tag(LOG_TAG).d("Login success!")
                },
                {
                    Timber.tag(LOG_TAG).e(it, "Login failure!")
                }
            )
        }
    }

    companion object {
        const val LOG_TAG = "LoginViewModel"
    }
}