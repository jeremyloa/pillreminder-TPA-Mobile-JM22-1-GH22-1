package edu.bluejack22_1.pillreminder.controller.auth

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth

class LoginGoogle: AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var  googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

}