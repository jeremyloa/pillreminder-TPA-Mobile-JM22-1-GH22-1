package edu.bluejack22_1.pillreminder.controller.doctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactAddBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.User

class DocContactAdd : AppCompatActivity() {
    private lateinit var binding: ActivityDocContactAddBinding
    private lateinit var backButton: ImageView
    private lateinit var addDocName: EditText
    private lateinit var addDocMail: EditText
    private lateinit var addDocPhone: EditText
    private lateinit var toAddDoc: Button
    private lateinit var name: String
    private lateinit var mail: String
    private lateinit var phone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDocContactAddBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener{finish()}
        addDocName = binding.addDocName
        addDocMail = binding.addDocMail
        addDocPhone = binding.addDocPhone
        toAddDoc = binding.toAddDoc
        toAddDocListener()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun frontVal(): String {
        name = addDocName.text.toString()
        mail = addDocMail.text.toString()
        phone = addDocPhone.text.toString()
        if (mail.isBlank()) return "E-mail should not be empty"
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) return "E-mail format is invalid"
        if (name.isBlank()) return "Name should not be empty"
        if (phone.isBlank()) return "Phone should not be empty"
        return ""
    }

    private fun toAddDocListener(){
        toAddDoc.setOnClickListener{
            val res = frontVal()
            if (res.isNotEmpty()) Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
            else {
                val user = User.get_user_email(mail)
                if (user!=null) {
                    DoctorContact.insert_doctorcontacts_patientid(name, phone, mail, user.uid.toString(), user.photo.toString())
                        Toast.makeText(this, "Add contact success. Your doctor has an account in pillreminder.", Toast.LENGTH_LONG).show()
                } else {
                    DoctorContact.insert_doctorcontacts_patientid(name, phone, mail, "unregistered", "unregistered")
                        Toast.makeText(this, "Add contact success. Your doctor don't have an account in pillreminder.", Toast.LENGTH_LONG).show()
                }
                DoctorContact.fetch_all_doctorcontacts_patientid()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 3000)
            }
        }
    }
}