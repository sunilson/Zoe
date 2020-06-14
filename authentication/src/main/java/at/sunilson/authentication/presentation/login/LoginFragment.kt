package at.sunilson.authentication.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import at.sunilson.authentication.R
import at.sunilson.authentication.databinding.FragmentLoginBinding
import at.sunilson.navigation.ActivityNavigator
import at.sunilson.navigation.ActivityNavigatorParams
import at.sunilson.presentationcore.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    private lateinit var activityNavigator: ActivityNavigator

    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.usernameInput.text.toString(),
                binding.passwordInput.text.toString()
            )
        }
    }

    private fun observeEvents() {
        startActivity(Intent().apply {
            activityNavigator.startMainActivity(ActivityNavigatorParams(requireActivity()))
        })
    }
}