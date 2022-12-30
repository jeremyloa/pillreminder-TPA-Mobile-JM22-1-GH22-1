package edu.bluejack22_1.pillreminder.controller.appointment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Timestamp
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ActivityAppointmentsDetailBinding
import edu.bluejack22_1.pillreminder.model.Appointment
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.User
import java.text.SimpleDateFormat

class AppointmentsDetail : AppCompatActivity() {
    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

    private lateinit var binding: ActivityAppointmentsDetailBinding
    private lateinit var backButton: ImageView
    private lateinit var appDetailPhoto: ImageView
    private lateinit var appDetailName: TextView
    private lateinit var appDetailDatetime: TextView
    private lateinit var appDetailNote: TextView
    private lateinit var appDetailPlace: TextView
    private lateinit var appDetailAddress: TextView
    private lateinit var toUpdateApp: Button
    private lateinit var toDeleteApp: Button

    private lateinit var documentid:String
    private lateinit var docconid:String
    private lateinit var doctorid:String
    private lateinit var patientid:String
    private lateinit var datetime:Timestamp
    private lateinit var place:String
    private lateinit var address:String
    private lateinit var note:String
    private lateinit var doc:DoctorContact
    private lateinit var opponent:User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        documentid = intent.getStringExtra("documentid").toString()
        docconid = intent.getStringExtra("docconid").toString()
        doctorid = intent.getStringExtra("doctorid").toString()
        patientid = intent.getStringExtra("patientid").toString()
        datetime = intent.getParcelableExtra<Timestamp>("datetime")!!
        place = intent.getStringExtra("place").toString()
        address = intent.getStringExtra("address").toString()
        note = intent.getStringExtra("note").toString()
        if (intent.hasExtra("doccon")) doc = intent.getSerializableExtra("doccon") as DoctorContact
        if (intent.hasExtra("opponent")) opponent = intent.getSerializableExtra("opponent") as User

        binding = ActivityAppointmentsDetailBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener{
            finish()
        }

        appDetailPhoto = binding.appDetailPhoto
        if (::opponent.isInitialized) Picasso.get().load(opponent.photo).into(appDetailPhoto)
        appDetailName = binding.appDetailName
        appDetailName.text = if (::doc.isInitialized) doc.name.toString() else opponent.name.toString()
        appDetailDatetime = binding.appDetailDatetime
        appDetailDatetime.text = dateFormat.format(datetime.toDate()).toString()
        appDetailNote = binding.appDetailNote
        appDetailNote.text = note
        appDetailPlace = binding.appDetailPlace
        appDetailPlace.text = place
        appDetailAddress = binding.appDetailAddress
        appDetailAddress.text = address
        toUpdateApp = binding.toUpdateApp
        toUpdateApp.setOnClickListener {
            var intent = Intent(this, AppointmentsUpdate::class.java)
            intent.putExtra("documentid", documentid)
            intent.putExtra("docconid", docconid)
            intent.putExtra("doctorid", doctorid)
            intent.putExtra("patientid", patientid)
            intent.putExtra("datetime", datetime)
            intent.putExtra("place", place)
            intent.putExtra("address", address)
            intent.putExtra("note", note)
            startActivity(intent)
        }
        toDeleteApp = binding.toDeleteApp
        toDeleteApp.setOnClickListener {
            Appointment.delete_appointment(documentid)
            Appointment.fetch_all_appointments()
            Toast.makeText(this, resources.getString(R.string.delete_app_success), Toast.LENGTH_LONG).show()
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 3000)
        }

        setContentView(binding.root)
    }
}