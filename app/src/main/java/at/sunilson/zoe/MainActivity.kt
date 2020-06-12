package at.sunilson.zoe

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import at.sunilson.authentication.presentation.AuthenticationActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startActivity(Intent(this, AuthenticationActivity::class.java))
    }
}