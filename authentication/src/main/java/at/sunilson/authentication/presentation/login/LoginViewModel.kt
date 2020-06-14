package at.sunilson.authentication.presentation.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import at.sunilson.authentication.domain.LoginUseCase


internal class LoginViewModel @ViewModelInject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {

}