package edu.bluejack22_1.pillreminder.controller.auth

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.controller.Splash
import edu.bluejack22_1.pillreminder.controller.main.MainActivity
import edu.bluejack22_1.pillreminder.databinding.ActivityLoginMainBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.User

class LoginMain : AppCompatActivity() {
    private val RC_SIGN_IN = 9001
    private lateinit var client: GoogleSignInClient
    private lateinit var db: FirebaseFirestore

    private lateinit var binding: ActivityLoginMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var pass: String

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        binding = ActivityLoginMainBinding.inflate(layoutInflater)
        User.logout()
        forgotListener()
        loginEmailListener()
        loginGoogleListener()
        registerListener()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }


    private fun validate(): String {
        email = binding.loginMail.text.toString()
        pass = binding.loginPass.text.toString()
        if (email.isBlank()) return resources.getString(R.string.email_empty)
        if (pass.isBlank()) return resources.getString(R.string.pass_empty)
        return ""
    }

    private fun loginEmailListener() {

        binding.toLoginEmailBtn.setOnClickListener {
            val res = validate()
            if (res.isNotEmpty()) Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
            else {
//                Log.d("Login Firebase", "eligible to do login")
//                Log.d("Login Firebase", "Email: $email Pass: $pass")

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
//                    Log.d("Login Firebase", "signInWithEmail:success")
                        User.login()
                        startActivity(Intent(this, Splash::class.java))
                        finish()
                    } else {
                        Log.w("Login Firebase", "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            this, resources.getString(R.string.auth_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }


    private fun registerListener() {
        binding.toRegMainBtn.setOnClickListener {
            startActivity(Intent(this, RegisterMain::class.java))
        }
    }

    private fun forgotListener() {
        binding.toForget.setOnClickListener {
            startActivity(Intent(this, ForgotPassword::class.java))
        }
    }


    private fun loginGoogleListener() {
        binding.toLoginGoogleBtn.setOnClickListener {
            auth = FirebaseAuth.getInstance()
            val gso:GoogleSignInOptions = GoogleSignInOptions
                    .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client))
                    .requestEmail()
                    .build()

            client = GoogleSignIn.getClient(this, gso)
            val signInIntent = client.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            if (task.isSuccessful) {
                val acc: GoogleSignInAccount? = task.result
                if (acc!=null) {
                    val cred = GoogleAuthProvider.getCredential(acc.idToken, null)
                    auth.signInWithCredential(cred).addOnCompleteListener {
                        if (it.isSuccessful) {
                            val email = acc.email.toString()
                            val cek = User.get_user_email(email)
                            if (cek!=null) {
                                User.login()
                                startActivity(Intent(this, Splash::class.java))
                                finish()
                            } else {
                                db = Firebase.firestore
                                val name = acc.displayName.toString()
                                val phone = "0"
                                val role = ""
                                val rol = if (role.equals(resources.getString(R.string.register_as_doctor))) "doctors" else "patients"
                                val addDoc = hashMapOf(
                                    "role" to rol,
                                    "name" to name,
                                    "email" to email,
                                    "phone" to phone,
                                    "photo" to "https://i.pravatar.cc/150?u=" + auth.currentUser!!.uid
                                )
                                db.collection("users").document(auth.currentUser!!.uid).set(addDoc).addOnSuccessListener {
                                    User.fetch_all_users()
                                    Toast.makeText(this, resources.getString(R.string.register_success), Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, Splash::class.java))
                                    finish()
                                }.addOnFailureListener {
                                    Toast.makeText(this, resources.getString(R.string.register_failed), Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else Toast.makeText(this, resources.getString(R.string.auth_failed), Toast.LENGTH_SHORT).show()
                    }
                }
            } else Toast.makeText(this, resources.getString(R.string.register_failed), Toast.LENGTH_SHORT).show()
        }
    }
}