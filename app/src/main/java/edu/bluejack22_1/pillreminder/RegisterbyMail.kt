package edu.bluejack22_1.pillreminder

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
import edu.bluejack22_1.pillreminder.databinding.ActivityRegisterbymailBinding

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
        if (mail.isBlank()) return "E-mail should not be empty"
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) return "E-mail format is invalid"
        if (pass.isBlank()) return "Password should not be empty"
        if (pass.length <6) return "Password length should be at least 6 characters"
        if (confpass.isBlank()) return "Confirmed password should not be empty"
        if (!pass.equals(confpass)) return "Password and confirmed password should be the same"
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
                if (!checkExist(rol, email, name, phone)) {
                    Log.d("Register Firebase", "Checked not exist")
                    auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Log.d("Register Firebase", "Added to auth")
                            val user = task.result.user
                            val addDoc = hashMapOf(
                                "role" to rol,
                                "name" to name,
                                "email" to email,
                                "phone" to phone,
                                "photo" to "https://i.pravatar.cc/150?u=" + user!!.uid
                            )
                            db.collection("users").document(user!!.uid).set(addDoc).addOnSuccessListener { doc ->
                                 Log.d("Register Firebase", "Register Success")
                                Toast.makeText(this, "You are now registered. ", Toast.LENGTH_SHORT).show()
                                UserCurrent.logout()
                                val intent = Intent(this, LoginMain::class.java)
                                startActivity(intent)
                                finish()
                            }
                            .addOnFailureListener { e ->
                                Log.d("Register Firebase", "Add user to database error")
//                                Toast.makeText(this, "Add user to database error", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Log.d("Register Firebase", "Add user to auth error")
                            Toast.makeText(this, "User already existed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "User already existed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loginListener() {
        binding.toLoginPageBtn.setOnClickListener{
            val intent = Intent(this, LoginMain::class.java)
            startActivity(intent)
        }
    }

    private fun checkExist(rol:String, email:String, name:String?, phone:String?):Boolean {
        var cek = false
        db.collection("users").get().addOnSuccessListener { res ->
            for (doc in res) {
                if (doc.getString("email").equals(email) || doc.getString("phone").equals(phone)) cek = true
                tempList.add(User(rol, doc.id, doc.getString("name"), doc.getString("email"), doc.getString("phone"), doc.getString("photo")))
            }
        }
        .addOnFailureListener { exception ->
            Toast.makeText(this, exception.toString(), Toast.LENGTH_SHORT).show()
        }
        return cek
    }
}