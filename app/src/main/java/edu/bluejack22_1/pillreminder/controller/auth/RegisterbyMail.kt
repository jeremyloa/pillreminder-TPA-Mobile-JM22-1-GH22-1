package edu.bluejack22_1.pillreminder.controller.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ActivityRegisterbymailBinding
import edu.bluejack22_1.pillreminder.model.User

class RegisterbyMail : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterbymailBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    var tempList: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterbymailBinding.inflate(layoutInflater)
        auth = Firebase.auth
        db = Firebase.firestore
        regEmailListener()
        loginListener()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun frontVal(): String {
        val mail = binding.regisMail.text.toString()
        val pass = binding.regisPass.text.toString()
        val confpass = binding.regisConfPass.text.toString()
        if (mail.isBlank()) return resources.getString(R.string.email_empty)
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) return resources.getString(R.string.email_invalid)
        if (pass.isBlank()) return resources.getString(R.string.pass_empty)
        if (pass.length <6) return resources.getString(R.string.pass_invalid)
        if (confpass.isBlank()) return resources.getString(R.string.conf_empty)
        if (!pass.equals(confpass)) return resources.getString(R.string.conf_nomatch)
        return ""
    }

    private fun regEmailListener() {
        binding.toRegEmailRegister.setOnClickListener{
            val res = frontVal()
            if (res.isNotEmpty()) Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
            else {
                val name = intent.getStringExtra("name")
                val phone = intent.getStringExtra("phone")
                val role = intent.getStringExtra("role")
                val email = binding.regisMail.text.toString()
                val pass = binding.regisPass.text.toString()
                val rol = if (role.equals("Register as Doctor")) "doctors" else "patients"
                if (!checkExist(email)) {
                    Log.d("Register Firebase", "Checked not exist")
                    val reg = User.register(rol, email, pass, name.toString(), phone.toString())
                    if (reg) {
                        Toast.makeText(this, resources.getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                        User.logout()
                        startActivity(Intent(this, LoginMain::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, resources.getString(R.string.register_failed), Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, resources.getString(R.string.register_existed), Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginListener() {
        binding.toLoginPageBtn.setOnClickListener{
            val intent = Intent(this, LoginMain::class.java)
            startActivity(intent)
            finish()
        }
    }

    companion object{

     fun checkExist(email:String):Boolean {
        var cek = false
        var user = User.get_user_email(email)
        if (user != null) cek = true
        return cek
    }
    }
}