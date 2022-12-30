package edu.bluejack22_1.pillreminder.controller.treatment

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
import edu.bluejack22_1.pillreminder.databinding.ActivityTreatmentsAddBinding
import edu.bluejack22_1.pillreminder.model.Treatment
import edu.bluejack22_1.pillreminder.model.User
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TreatmentsAdd : AppCompatActivity() {

    private lateinit var binding: ActivityTreatmentsAddBinding
    private lateinit var backButton: ImageView
    private lateinit var addTrtName: TextView
    private lateinit var addTrtDose: TextView
    private lateinit var addTrtUnit: TextView
    private lateinit var addTrtFreq: Spinner
    private lateinit var addTrtStartDate: TextView
    private lateinit var addTrtEndDate: TextView
    private lateinit var addTrtRemindHourMin: TextView
    private lateinit var goAddTrt: Button

    val c: Calendar = Calendar.getInstance()
    var startDay = c.get(Calendar.DAY_OF_MONTH); var startMonth = c.get(Calendar.MONTH)+1; var startYear = c.get(Calendar.YEAR);
    var endDay = c.get(Calendar.DAY_OF_MONTH); var endMonth = c.get(Calendar.MONTH)+1; var endYear = c.get(Calendar.YEAR);
    var setHour = c.get(Calendar.HOUR); var setMin = c.get(Calendar.MINUTE);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = arrayOf(resources.getString(R.string.daily).toString(), resources.getString(R.string.weekly).toString(), resources.getString(R.string.monthly).toString())
        binding = ActivityTreatmentsAddBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener { finish() }
        addTrtName = binding.addTrtName
        addTrtDose = binding.addTrtDose
        addTrtUnit = binding.addTrtUnit
        addTrtFreq = binding.addTrtFreq
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        addTrtFreq.adapter = adapter
        addTrtStartDate = binding.addTrtStartDate
        addTrtStartDate.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                    view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                        startYear = mYear; startMonth = mMonth+1; startDay = mDay;
                        addTrtStartDate.text = Editable.Factory.getInstance().newEditable(String.format("%d-%02d-%02d", startYear, startMonth, startDay))
            }, startYear, startMonth-1, startDay)
            dpd.show()
        }
        addTrtEndDate = binding.addTrtEndDate
        addTrtEndDate.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                    view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                        endYear = mYear; endMonth = mMonth+1; endDay = mDay;
                        addTrtEndDate.text = Editable.Factory.getInstance().newEditable(String.format("%d-%02d-%02d", endYear, endMonth, endDay))
            }, endYear, endMonth-1, endDay)
            dpd.show()
        }
        addTrtRemindHourMin = binding.addTrtRemindHourMin
        addTrtRemindHourMin.setOnClickListener {
            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                    view: TimePicker?, mHour: Int, mMinute: Int ->
                        setHour = mHour; setMin = mMinute;
                        addTrtRemindHourMin.text = Editable.Factory.getInstance().newEditable(String.format("%02d:%02d", setHour, setMin))
            }, setHour, setMin, DateFormat.is24HourFormat(this))
            tpd.show()
        }
        goAddTrt = binding.goAddTrt
        goAddTrt.setOnClickListener {
            if (addTrtName.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.title_empty), Toast.LENGTH_SHORT).show()
            else if (addTrtDose.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.dose_empty), Toast.LENGTH_SHORT).show()
            else if (addTrtUnit.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.unit_empty), Toast.LENGTH_SHORT).show()
            else if (addTrtStartDate.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.start_date_empty), Toast.LENGTH_SHORT).show()
            else if (addTrtEndDate.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.end_date_empty), Toast.LENGTH_SHORT).show()
            else if (addTrtRemindHourMin.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.time_empty), Toast.LENGTH_SHORT).show()
            else {
                val startDate = LocalDateTime.of(startYear, startMonth, startDay, setHour, setMin)
                val startTimestamp = Timestamp(startDate.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
                val endDate = LocalDateTime.of(endYear, endMonth, endDay, setHour, setMin)
                val endTimestamp = Timestamp(endDate.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
                val freq =
                    if (addTrtFreq.selectedItem.toString() == "Monthly" || addTrtFreq.selectedItem.toString() == "Bulanan") 30
                    else if (addTrtFreq.selectedItem.toString() == "Weekly" || addTrtFreq.selectedItem.toString() == "Mingguan") 7
                    else 1

                val nextoccurrence = Treatment.count_next_occurrence(freq, startTimestamp, endTimestamp, setHour, setMin)
                val trt = Treatment(
                    "",
                    User.curr.uid.toString(),
                    addTrtName.text.toString(),
                    addTrtDose.text.toString().toDouble(),
                    addTrtUnit.text.toString(),
                    freq,
                    nextoccurrence,
                    startTimestamp,
                    endTimestamp,
                    setHour,
                    setMin
                )
                Treatment.insert_treatment(trt)
                Treatment.fetch_all_treatments()
                Toast.makeText(this, resources.getString(R.string.insert_trt_success), Toast.LENGTH_SHORT).show()
                Handler(Looper.getMainLooper()).postDelayed({
//                    val intent = Intent(this, MainActivity::class.java)
//                    intent.putExtra("fragment", Activities::class.java)
////                    intent.putExtra("opens", AppointmentsMain::class.java)
//                    startActivity(intent)
                    finish()
                }, 3000)
            }
        }
        setContentView(binding.root)
    }

}