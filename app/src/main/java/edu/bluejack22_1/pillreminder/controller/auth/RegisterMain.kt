package edu.bluejack22_1.pillreminder.controller.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import edu.bluejack22_1.pillreminder.databinding.ActivityRegisterMainBinding

class RegisterMain : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterMainBinding.inflate(layoutInflater)
        regEmailListener()
        regGoogleListener()
        loginListener()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun frontVal(): String {
        val name = binding.regisName.text.toString()
        val phone = binding.regisPhone.text.toString()
        val role = binding.regisRole.selectedItem.toString()
        if (name.isBlank()) return "Name should not be empty"
        if (phone.isBlank()) return "Phone should not be empty"
        if (role.isBlank()) return "Role should be chosen"
        return ""
    }

    private fun regEmailListener() {
        binding.toRegEmailBtn.setOnClickListener{
            val res = frontVal()
            if (res.isNotEmpty()) Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
            else {
                val intent = Intent(this, RegisterbyMail::class.java)
                intent.putExtra("name", binding.regisName.text.toString())
                intent.putExtra("phone", binding.regisPhone.text.toString())
                intent.putExtra("role", binding.regisRole.selectedItem.toString())
                startActivity(intent)
            }
        }
    }

    private fun regGoogleListener() {
        binding.toRegGoogleBtn.setOnClickListener{
            val res = frontVal()
            if (res.isNotEmpty()) Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
            else {

            }
        }
    }

    private fun loginListener() {
        binding.toLoginMainBtn.setOnClickListener {
            val intent = Intent(this, LoginMain::class.java)
            startActivity(intent)
            finish()
        }
    }
}