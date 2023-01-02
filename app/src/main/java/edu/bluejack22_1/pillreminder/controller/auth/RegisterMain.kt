package edu.bluejack22_1.pillreminder.controller.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.controller.Splash
import edu.bluejack22_1.pillreminder.databinding.ActivityRegisterMainBinding
import edu.bluejack22_1.pillreminder.model.User

class RegisterMain : AppCompatActivity() {
    private val RC_SIGN_IN = 9001
    private lateinit var client: GoogleSignInClient
    private lateinit var db: FirebaseFirestore

    private lateinit var auth: FirebaseAuth

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
                                val name = binding.regisName.text.toString()
                                val phone = binding.regisPhone.text.toString()
                                val role = binding.regisRole.selectedItem.toString()
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

    private fun loginListener() {
        binding.toLoginMainBtn.setOnClickListener {
            val intent = Intent(this, LoginMain::class.java)
            startActivity(intent)
            finish()
        }
    }
}