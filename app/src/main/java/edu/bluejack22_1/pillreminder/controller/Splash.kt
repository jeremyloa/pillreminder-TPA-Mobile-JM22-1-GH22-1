package edu.bluejack22_1.pillreminder.controller

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.controller.auth.LoginMain
import edu.bluejack22_1.pillreminder.controller.main.MainActivity
import edu.bluejack22_1.pillreminder.model.*

class Splash : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        var auth = FirebaseAuth.getInstance()

        if (auth.currentUser!=null){
            User.login(auth.currentUser!!.uid)
            DoctorContact.fetch_all_doctorcontacts_patientid()
            Appointment.fetch_all_appointments()
            Treatment.fetch_all_treatments()
        }

        User.fetch_all_users()

        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser!=null) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginMain::class.java))
                finish()
            }
        }, 3000)
    }
}