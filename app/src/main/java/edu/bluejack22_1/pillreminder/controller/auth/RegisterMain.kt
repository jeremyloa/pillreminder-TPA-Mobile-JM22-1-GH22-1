package edu.bluejack22_1.pillreminder.controller.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ActivityRegisterMainBinding

class RegisterMain : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
//    private val gso:GoogleSignInOptions = GoogleSignInOptions
//        .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//        .requestIdToken(getString(R.string.default_web_client_id))
//        .requestEmail()
//        .build()
//    val Req_Code: Int = 123

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
        if (name.isBlank()) return resources.getString(R.string.name_empty)
        if (phone.isBlank()) return resources.getString(R.string.phone_empty)
        if (role.isBlank()) return resources.getString(R.string.role_empty)
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