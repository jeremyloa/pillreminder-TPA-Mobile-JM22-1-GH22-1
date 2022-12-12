package edu.bluejack22_1.pillreminder.controller.doctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactUpdateBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact

class DocContactUpdate : AppCompatActivity() {
    private lateinit var binding: ActivityDocContactUpdateBinding
    private lateinit var backButton: ImageView
    private lateinit var updateDocPhoto: ShapeableImageView
    private lateinit var updateDocName: EditText
    private lateinit var updateDocPhone: EditText
    private lateinit var toUpdateDoc: Button
    private lateinit var name: String
    private lateinit var phone: String

    private lateinit var doc: DoctorContact

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDocContactUpdateBinding.inflate(layoutInflater)
        doc = intent.getSerializableExtra("doctorcontact") as DoctorContact
        backButton = binding.backButton
        backButton.setOnClickListener{finish()}
        updateDocName = binding.updateDocName
        updateDocName.text = Editable.Factory.getInstance().newEditable(doc.name)
        updateDocPhone = binding.updateDocPhone
        updateDocPhone.text = Editable.Factory.getInstance().newEditable(doc.phone)
        updateDocPhoto = binding.updateDocPhoto
        if (!doc.photo.equals("unregistered"))
            Picasso.get().load(doc.photo).into(updateDocPhoto)
        toUpdateDoc = binding.toUpdateDoc
        toUpdateDocListener()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    private fun frontVal(): String {
        name = updateDocName.text.toString()
        phone = updateDocPhone.text.toString()
        if (name.isBlank()) return "Name should not be empty"
        if (phone.isBlank()) return "Phone should not be empty"
        return ""
    }

    private fun toUpdateDocListener(){
        toUpdateDoc.setOnClickListener{
            val res = frontVal()
            if (res.isNotEmpty()) Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
            else {
                DoctorContact.update_doctorcontacts_documentid(doc.documentid.toString(), name, phone)
                Toast.makeText(this, "Update contact success.", Toast.LENGTH_LONG).show()
                DoctorContact.fetch_all_doctorcontacts_patientid()
                Handler(Looper.getMainLooper()).postDelayed({
                    finish()
                }, 3000)
            }
        }
    }
}