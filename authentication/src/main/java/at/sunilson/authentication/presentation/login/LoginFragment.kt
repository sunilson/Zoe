package at.sunilson.authentication.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import at.sunilson.authentication.R
import at.sunilson.authentication.databinding.FragmentLoginBinding
import at.sunilson.presentationcore.base.viewBinding

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val binding: FragmentLoginBinding by viewBinding(FragmentLoginBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            TODO("Login")
        }
    }

}