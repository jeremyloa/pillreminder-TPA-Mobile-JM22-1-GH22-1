package edu.bluejack22_1.pillreminder

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.pillreminder.databinding.ActivityLoginMainBinding

class LoginMain : AppCompatActivity() {

    private lateinit var binding: ActivityLoginMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var pass: String
    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        UserCurrent.logout()
        forgotListener()
        loginEmailListener()
        loginGoogleListener()
        registerListener()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }


    private fun validate(): String{
        email = binding.loginMail.text.toString()
        pass = binding.loginPass.text.toString()
        if (email.isBlank()) return "E-mail should not be empty"
        if (pass.isBlank()) return "Password should not be empty"
        return ""
    }

    private fun loginEmailListener(){

        binding.toLoginEmailBtn.setOnClickListener {
            val res = validate()
            if (res.isNotEmpty()) Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
            else {
//                Log.d("Login Firebase", "eligible to do login")
//                Log.d("Login Firebase", "Email: $email Pass: $pass")

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
//                    Log.d("Login Firebase", "signInWithEmail:success")
                    UserCurrent.login(auth.currentUser!!.uid)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    Log.w("Login Firebase", "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                }
                }
            }
        }
    }

    private fun loginGoogleListener(){

        binding.toLoginGoogleBtn.setOnClickListener {

        }
    }

    private fun registerListener(){
        binding.toRegMainBtn.setOnClickListener {
            startActivity(Intent(this, RegisterMain::class.java))
        }
    }

    private fun forgotListener(){
        binding.toForget.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }
}