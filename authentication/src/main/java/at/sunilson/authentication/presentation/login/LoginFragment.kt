package at.sunilson.authentication.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import at.sunilson.authentication.R
import at.sunilson.authentication.databinding.FragmentLoginBinding
import at.sunilson.presentationcore.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            TODO("Login")
        }
    }

}