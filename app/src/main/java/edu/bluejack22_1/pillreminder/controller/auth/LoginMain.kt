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
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.controller.main.MainActivity
import edu.bluejack22_1.pillreminder.databinding.ActivityLoginMainBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.User

class LoginMain : AppCompatActivity() {

    private lateinit var binding: ActivityLoginMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var email: String
    private lateinit var pass: String
    private lateinit var googleSignInClient: GoogleSignInClient

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
                        User.login(auth.currentUser!!.uid)
                        startActivity(Intent(this, MainActivity::class.java))
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
            val webClientId = "918358412448-7e3fuq3vpralsiicvqdmv8b497crb387.apps.googleusercontent.com"
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(webClientId)
                .requestEmail()
                .build()

            googleSignInClient = GoogleSignIn.getClient(this, gso)
            signInGoogle()



        }
    }

    fun signInGoogle() {
        val signInIntent = googleSignInClient.signInIntent

        launcher.launch(signInIntent)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            Log.d("loginGoogle", "launcher2")
            if (result.resultCode == Activity.RESULT_OK) {
                Log.d("loginGoogle", "launcher1")
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                handleResults(task)
            }

        }

    private fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            Log.d("loginGoogle", "handleres")
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateLayout(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()

        }
    }

    private fun updateLayout(account: GoogleSignInAccount) {
        val cre = GoogleAuthProvider.getCredential(account.idToken, null)
        Log.d("loginGoogle", "luar2")
        auth.signInWithCredential(cre).addOnCompleteListener {
            Log.d("loginGoogle", "luar")
            if (it.isSuccessful) {
                Log.d("loginGoogle", "masuklogingoogle")
                val intent: Intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email", account.email)
                intent.putExtra("name", account.displayName)
                startActivity(intent)

            } else {
                Log.d("loginGoogle", "GAMASUKlogingoogle")
                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}