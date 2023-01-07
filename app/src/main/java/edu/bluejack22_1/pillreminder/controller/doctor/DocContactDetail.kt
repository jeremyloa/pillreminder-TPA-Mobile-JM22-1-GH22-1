package edu.bluejack22_1.pillreminder.controller.doctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.controller.main.Activities
import edu.bluejack22_1.pillreminder.controller.main.MainActivity
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactDetailBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact

class DocContactDetail : AppCompatActivity() {
    private lateinit var binding: ActivityDocContactDetailBinding
    private lateinit var doc: DoctorContact

    private lateinit var docDetailPhoto: ImageView
    private lateinit var docDetailName: TextView
    private lateinit var docDetailPhone: TextView
    private lateinit var docDetailMail: TextView
    private lateinit var docDetailChat: ImageView
    private lateinit var docDetailAppointment: ImageView
    private lateinit var toUpdateDocContact: TextView
    private lateinit var toDeleteDocContact: TextView

    private lateinit var backButton: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDocContactDetailBinding.inflate(layoutInflater)
        doc = intent.getSerializableExtra("doctorcontact") as DoctorContact

        docDetailPhoto = binding.docDetailPhoto
        docDetailName = binding.docDetailName
        docDetailPhone = binding.docDetailPhone
        docDetailMail = binding.docDetailMail
        toUpdateDocContact = binding.toUpdateDocContact
        toDeleteDocContact = binding.toDeleteDocContact
        docDetailChat = binding.docDetailChat
        docDetailAppointment = binding.docDetailAppointment
        backButton = binding.backButton

        init()

        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        for (dc in DoctorContact.allDoctorCon){
            if (dc.documentid.equals(doc.documentid)) {
                doc = dc
                break
            }
        }
        init()
//        doc = DoctorContact.get_doctorcontacts_documentid(doc.documentid.toString())!!
    }

    private fun init(){
        if (!doc.photo.equals("unregistered"))
            Picasso.get().load(doc.photo).into(docDetailPhoto)
        docDetailName.text = doc.name
        docDetailPhone.text = doc.phone
        docDetailMail.text = doc.email
        if (doc.doctorid.equals("unregistered"))
            docDetailChat.visibility = View.INVISIBLE
        docDetailAppointment.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("fragment", Activities::class.java)
            startActivity(intent)
//            finish()
        }
        toUpdateDocContact.setOnClickListener{
            var intent = Intent(this, DocContactUpdate::class.java)
            intent.putExtra("doctorcontact", doc)
            startActivity(intent)
        }
        toDeleteDocContact.setOnClickListener{
            DoctorContact.delete_doctorcontacts_documentid(doc.documentid.toString())
            Toast.makeText(this, resources.getString(R.string.del_con_success), Toast.LENGTH_LONG).show()
            DoctorContact.fetch_all_doctorcontacts_patientid()
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 3000)
        }
        backButton.setOnClickListener {
            finish()
        }
    }
}