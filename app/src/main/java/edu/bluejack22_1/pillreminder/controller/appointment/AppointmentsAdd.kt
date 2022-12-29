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
import edu.bluejack22_1.pillreminder.databinding.ActivityAppointmentsAddBinding
import edu.bluejack22_1.pillreminder.model.Appointment
import edu.bluejack22_1.pillreminder.model.User
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date

class AppointmentsAdd : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
TimePickerDialog.OnTimeSetListener {
    private lateinit var binding: ActivityAppointmentsAddBinding
    private lateinit var backButton: ImageView
    private lateinit var addAppNote: EditText
    private lateinit var addAppPlace: EditText
    private lateinit var addAppAddress: EditText
    private lateinit var addAppDateTime: EditText
    private lateinit var goAddApp: Button

    lateinit var docconid: String
    lateinit var doctorid: String

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
        binding = ActivityAppointmentsAddBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener{
            finish()
        }
        docconid = intent.getStringExtra("docconid").toString()
        doctorid = intent.getStringExtra("doctorid").toString()
        addAppNote = binding.addAppNote
        addAppPlace = binding.addAppPlace
        addAppAddress = binding.addAppAddress
        addAppDateTime = binding.addAppDateTime
        addAppDateTime.setOnClickListener{
            val calendar: Calendar = Calendar.getInstance()
             day = calendar.get(Calendar.DAY_OF_MONTH)
             month = calendar.get(Calendar.MONTH)
             year = calendar.get(Calendar.YEAR)
             val datePickerDialog =
                DatePickerDialog(this@AppointmentsAdd, this@AppointmentsAdd, year, month,day)
             datePickerDialog.show()
        }
        goAddApp = binding.goAddApp
        goAddApp.setOnClickListener {
            if (addAppNote.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.note_empty), Toast.LENGTH_SHORT).show()
            else if (addAppPlace.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.place_empty), Toast.LENGTH_SHORT).show()
            else if (addAppAddress.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.address_empty), Toast.LENGTH_SHORT).show()
            else if (addAppDateTime.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.date_time_empty), Toast.LENGTH_SHORT).show()
            else {
                var datetime = LocalDateTime.of(myYear, myMonth, myDay, myHour, myMinute)
                Log.i("TIMESTAMP", datetime.year.toString() + "-" + datetime.month.toString())
                var timestamp = Timestamp(datetime.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
                Log.i("TIMESTAMP", timestamp.seconds.toString() + " " + timestamp.nanoseconds.toString())
                var apt = Appointment("", docconid, doctorid, User.curr.uid, timestamp, addAppPlace.text.toString(), addAppAddress.text.toString(), addAppNote.text.toString())
                Appointment.insert_appointment(apt)
                Appointment.fetch_all_appointments()
                Toast.makeText(this, resources.getString(R.string.insert_app_success), Toast.LENGTH_SHORT).show()
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
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this@AppointmentsAdd, this@AppointmentsAdd, hour, minute,
        DateFormat.is24HourFormat(this))
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hour: Int, minute: Int) {
        myHour = hour
        myMinute = minute
        addAppDateTime.text = Editable.Factory.getInstance().newEditable(String.format("%d-%d-%d %d:%d", myYear, myMonth, myDay, myHour, myMinute))
    }
}