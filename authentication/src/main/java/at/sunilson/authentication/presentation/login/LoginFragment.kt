package at.sunilson.authentication.presentation.login

import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import at.sunilson.authentication.R
import at.sunilson.authentication.databinding.FragmentLoginBinding
import at.sunilson.core.Do
import at.sunilson.ktx.context.showToast
import at.sunilson.ktx.fragment.safeStartActivity
import at.sunilson.ktx.fragment.setNavigationBarColor
import at.sunilson.ktx.fragment.setStatusBarColor
import at.sunilson.ktx.fragment.useLightNavigationBarIcons
import at.sunilson.ktx.fragment.useLightStatusBarIcons
import at.sunilson.navigation.ActivityNavigator
import at.sunilson.navigation.ActivityNavigatorParams
import at.sunilson.presentationcore.base.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class LoginFragment : Fragment(R.layout.fragment_login) {

    @Inject
    lateinit var activityNavigator: ActivityNavigator

    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)
    private val viewModel: LoginViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.white)
        setNavigationBarColor(R.color.white)
        useLightStatusBarIcons(false)
        useLightNavigationBarIcons(false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            viewModel.login(
                binding.usernameInput.text.toString(),
                binding.passwordInput.text.toString()
            )
        }

        binding.registerButton.setOnClickListener {
            safeStartActivity(Intent(ACTION_VIEW).apply {
                data = Uri.parse("market://dev?id=RENAULT+SAS")
            })
        }

        observeEvents()
    }


    private fun observeEvents() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.events.collect {
                Do exhaustive when (it) {
                    LoginEvent.LoginSuccess -> activityNavigator.startMainActivity(
                        ActivityNavigatorParams(requireActivity())
                    )
                    LoginEvent.LoginFailure -> requireContext().showToast("Login fehlgeschlagen!")
                }
            }
        }
    }
}