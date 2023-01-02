package edu.bluejack22_1.pillreminder.controller.appointment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.format.DateFormat
import android.util.Log
import android.widget.*
import com.google.firebase.Timestamp
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.controller.main.Activities
import edu.bluejack22_1.pillreminder.controller.main.MainActivity
import edu.bluejack22_1.pillreminder.databinding.ActivityAppointmentsUpdateBinding
import edu.bluejack22_1.pillreminder.model.Appointment
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class AppointmentsUpdate : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityAppointmentsUpdateBinding
    private lateinit var backButton: ImageView
    private lateinit var updateAppNote: EditText
    private lateinit var updateAppPlace: EditText
    private lateinit var updateAppAddress: EditText
    private lateinit var updateAppDateTime: EditText
    private lateinit var goUpdateApp: Button

    private lateinit var documentid:String
    private lateinit var docconid:String
    private lateinit var doctorid:String
    private lateinit var patientid:String
    private lateinit var datetime:Timestamp
    private lateinit var place:String
    private lateinit var address:String
    private lateinit var note:String

    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0

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

        binding = ActivityAppointmentsUpdateBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener{
            finish()
        }

        updateAppNote = binding.updateAppNote
        updateAppNote.text = Editable.Factory.getInstance().newEditable(note)
        updateAppPlace = binding.updateAppPlace
        updateAppPlace.text = Editable.Factory.getInstance().newEditable(place)
        updateAppAddress = binding.updateAppAddress
        updateAppAddress.text = Editable.Factory.getInstance().newEditable(address)
        updateAppDateTime = binding.updateAppDateTime
        val tempCal = Calendar.getInstance()
        tempCal.time = datetime.toDate()
        myDay = tempCal.get(Calendar.DAY_OF_MONTH)
        myMonth = tempCal.get(Calendar.MONTH)+1
        myYear = tempCal.get(Calendar.YEAR)
        myHour = tempCal.get(Calendar.HOUR)
        myMinute = tempCal.get(Calendar.MINUTE)
        updateAppDateTime.text = Editable.Factory.getInstance().newEditable(String.format("%d-%02d-%02d %02d:%02d", myYear, myMonth, myDay, myHour, myMinute))
        updateAppDateTime.setOnClickListener{
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = datetime.toDate()
             day = calendar.get(Calendar.DAY_OF_MONTH)
             month = calendar.get(Calendar.MONTH)
             year = calendar.get(Calendar.YEAR)
             val datePickerDialog =
                DatePickerDialog(this@AppointmentsUpdate, this@AppointmentsUpdate, year, month,day)
             datePickerDialog.show()
        }
        goUpdateApp = binding.goUpdateApp
        goUpdateApp.setOnClickListener {
            if (updateAppNote.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.note_empty), Toast.LENGTH_SHORT).show()
            else if (updateAppPlace.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.place_empty), Toast.LENGTH_SHORT).show()
            else if (updateAppAddress.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.address_empty), Toast.LENGTH_SHORT).show()
            else if (updateAppDateTime.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.date_time_empty), Toast.LENGTH_SHORT).show()
            else {
                var datetime = LocalDateTime.of(myYear, myMonth, myDay, myHour, myMinute)
                Log.i("TIMESTAMP", datetime.year.toString() + "-" + datetime.month.toString())
                var timestamp = Timestamp(datetime.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
                Log.i("TIMESTAMP", timestamp.seconds.toString() + " " + timestamp.nanoseconds.toString())
                Appointment.update_appointment(documentid, timestamp, updateAppPlace.text.toString(), updateAppAddress.text.toString(), updateAppNote.text.toString())
                Appointment.fetch_all_appointments()
                Toast.makeText(this, resources.getString(R.string.update_app_success), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("fragment", Activities::class.java)
//                    intent.putExtra("opens", AppointmentsMain::class.java)
                    startActivity(intent)
                    finish()
                }, 3000)
            }
        }

        setContentView(binding.root)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, day: Int) {
        myDay = day
        myYear = year
        myMonth = month+1
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = datetime.toDate()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@AppointmentsUpdate, this@AppointmentsUpdate, hour, minute,
        DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        myHour = hour
        myMinute = minute
        updateAppDateTime.text = Editable.Factory.getInstance().newEditable(String.format("%d-%02d-%02d %02d:%02d", myYear, myMonth, myDay, myHour, myMinute))
    }
}