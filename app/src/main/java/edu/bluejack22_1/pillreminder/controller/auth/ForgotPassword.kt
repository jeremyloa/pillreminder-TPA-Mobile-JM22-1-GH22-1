package edu.bluejack22_1.pillreminder.controller.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ActivityForgotpasswordBinding
import edu.bluejack22_1.pillreminder.model.User

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotpasswordBinding
    private lateinit var forgotMail: EditText
    private lateinit var toResetToken: Button
    private val auth = FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        User.fetch_all_users()
        binding = ActivityForgotpasswordBinding.inflate(layoutInflater)
        forgotMail = binding.forgotMail
        toResetToken = binding.toResetToken
        toResetToken.setOnClickListener {
            if (forgotMail.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.email_empty), Toast.LENGTH_LONG).show()
            else if (!Patterns.EMAIL_ADDRESS.matcher(forgotMail.text.toString()).matches()) Toast.makeText(this, resources.getString(R.string.email_invalid), Toast.LENGTH_LONG).show()
            else if (User.get_user_email(forgotMail.text.toString())==null) Toast.makeText(this, resources.getString(R.string.user_not_existed), Toast.LENGTH_LONG).show()
            else {
                auth.sendPasswordResetEmail(forgotMail.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, resources.getString(R.string.reset_success), Toast.LENGTH_LONG).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                finish()
                            }, 3000)
                        } else {
                            Toast.makeText(this, resources.getString(R.string.reset_failed), Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
        setContentView(binding.root)
    }
}