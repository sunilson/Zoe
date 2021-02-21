package at.sunilson.authentication.presentation

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.sunilson.authentication.R
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticationActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(sharedPreferences.getInt("theme", R.style.Base_Theme_Zoe_Blue))
        setContentView(R.layout.activity_authentication)
    }
}
