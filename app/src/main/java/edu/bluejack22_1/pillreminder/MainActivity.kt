package edu.bluejack22_1.pillreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack22_1.pillreminder.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        logoutListener()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun logoutListener(){
        binding.toLogoutBtn.setOnClickListener {
            UserCurrent.logout()
            startActivity(Intent(this, Splash::class.java))
            finish()
        }
    }
}