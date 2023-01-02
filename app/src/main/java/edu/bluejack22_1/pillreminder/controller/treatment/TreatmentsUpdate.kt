package edu.bluejack22_1.pillreminder.controller.treatment

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.format.DateFormat
import android.widget.*
import com.google.firebase.Timestamp
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ActivityTreatmentsUpdateBinding
import edu.bluejack22_1.pillreminder.model.Treatment
import edu.bluejack22_1.pillreminder.model.User
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class TreatmentsUpdate : AppCompatActivity() {
    private lateinit var binding: ActivityTreatmentsUpdateBinding
    private lateinit var backButton: ImageView
    private lateinit var updateTrtName: EditText
    private lateinit var updateTrtDose: EditText
    private lateinit var updateTrtUnit: EditText
    private lateinit var updateTrtFreq: Spinner
    private lateinit var updateTrtStartDate: EditText
    private lateinit var updateTrtEndDate: EditText
    private lateinit var updateTrtRemindHourMin: EditText
    private lateinit var goUpdateTrt: Button

    private lateinit var documentid:String
    private lateinit var trt:Treatment

    var startDay = 0; var startMonth = 0; var startYear = 0;
    var endDay = 0; var endMonth = 0; var endYear = 0;
    var setHour = 0; var setMin = 0;

//    val dateTimeFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val items = arrayOf(resources.getString(R.string.daily).toString(), resources.getString(R.string.weekly).toString(), resources.getString(R.string.monthly).toString())

        documentid = intent.getStringExtra("documentid").toString()
        trt = Treatment.get_treatments_documentid(documentid)!!
        var startd = trt.startdate
        var endd = trt.enddate
        var timtim = trt.nextoccurrence
        setHour = trt.remindhour
        setMin = trt.remindmin

        val startCal: Calendar = Calendar.getInstance()
            startCal.time = startd.toDate()
            startDay = startCal.get(Calendar.DAY_OF_MONTH)
            startMonth = startCal.get(Calendar.MONTH)
            startYear = startCal.get(Calendar.YEAR)

        val endCal: Calendar = Calendar.getInstance()
            endCal.time = endd.toDate()
            endDay = endCal.get(Calendar.DAY_OF_MONTH)
            endMonth = endCal.get(Calendar.MONTH)
            endYear = endCal.get(Calendar.YEAR)

        val timCal: Calendar = Calendar.getInstance()
            timCal.time = timtim.toDate()

        binding = ActivityTreatmentsUpdateBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener{
            finish()
        }
        updateTrtName = binding.updateTrtName
        updateTrtName.text = Editable.Factory.getInstance().newEditable(trt.name)
        updateTrtDose = binding.updateTrtDose
        updateTrtDose.text = Editable.Factory.getInstance().newEditable(trt.dose.toString())
        updateTrtUnit = binding.updateTrtUnit
        updateTrtUnit.text = Editable.Factory.getInstance().newEditable(trt.unit)
        updateTrtFreq = binding.updateTrtFreq
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        updateTrtFreq.adapter = adapter
        if (trt.frequency == 1) updateTrtFreq.setSelection(0)
        else if (trt.frequency == 7) updateTrtFreq.setSelection(1)
        if (trt.frequency == 30) updateTrtFreq.setSelection(2)
        updateTrtStartDate = binding.updateTrtStartDate
        updateTrtStartDate.text = Editable.Factory.getInstance().newEditable(dateFormat.format(trt.startdate.toDate()).toString())
        updateTrtStartDate.setOnClickListener{
            val startCal: Calendar = Calendar.getInstance()
            startCal.time = startd.toDate()
            startDay = startCal.get(Calendar.DAY_OF_MONTH)
            startMonth = startCal.get(Calendar.MONTH)
            startYear = startCal.get(Calendar.YEAR)
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                    view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                        startYear = mYear; startMonth = mMonth+1; startDay = mDay;
                        updateTrtStartDate.text = Editable.Factory.getInstance().newEditable(String.format("%02d/%02d/%d", startDay, startMonth, startYear))
                        val temp = LocalDateTime.of(startYear, startMonth, startDay, setHour, setMin)
                        startd = Timestamp(temp.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
            }, startYear, startMonth, startDay)
            dpd.show()
        }
        updateTrtEndDate = binding.updateTrtEndDate
        updateTrtEndDate.text = Editable.Factory.getInstance().newEditable(dateFormat.format(trt.enddate.toDate()).toString())
        updateTrtEndDate.setOnClickListener {
            val endCal: Calendar = Calendar.getInstance()
            endCal.time = endd.toDate()
            endDay = endCal.get(Calendar.DAY_OF_MONTH)
            endMonth = endCal.get(Calendar.MONTH)
            endYear = endCal.get(Calendar.YEAR)
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener{
                    view: DatePicker?, mYear: Int, mMonth: Int, mDay: Int ->
                        endYear = mYear; endMonth = mMonth+1; endDay = mDay;
                        updateTrtEndDate.text = Editable.Factory.getInstance().newEditable(String.format("%02d/%02d/%d", endDay, endMonth, endYear))
                        val temp = LocalDateTime.of(endYear, endMonth, endDay, setHour, setMin)
                        endd = Timestamp(temp.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
            }, endYear, endMonth, endDay)
            dpd.show()
        }
        updateTrtRemindHourMin = binding.updateTrtRemindHourMin
        updateTrtRemindHourMin.text = Editable.Factory.getInstance().newEditable(String.format("%02d:%02d", trt.remindhour, trt.remindmin))
        updateTrtRemindHourMin.setOnClickListener {
            val timCal: Calendar = Calendar.getInstance()
            timCal.time = timtim.toDate()
            var tempYear = timCal.get(Calendar.YEAR)
            var tempMonth = timCal.get(Calendar.MONTH)
            var tempDay = timCal.get(Calendar.DAY_OF_MONTH)
            setHour = timCal.get(Calendar.HOUR)
            setMin = timCal.get(Calendar.MINUTE)
            val tpd = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener {
                    view: TimePicker?, mHour: Int, mMinute: Int ->
                        setHour = mHour; setMin = mMinute;
                        updateTrtRemindHourMin.text = Editable.Factory.getInstance().newEditable(String.format("%02d:%02d", setHour, setMin))
                        val temp = LocalDateTime.of(tempYear, tempMonth, tempDay, setHour, setMin)
                        timtim = Timestamp(temp.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
            }, setHour, setMin, DateFormat.is24HourFormat(this))
            tpd.show()
        }
        goUpdateTrt = binding.goUpdateTrt
        goUpdateTrt.setOnClickListener {
            if (updateTrtName.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.title_empty), Toast.LENGTH_SHORT).show()
            else if (updateTrtDose.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.dose_empty), Toast.LENGTH_SHORT).show()
            else if (updateTrtUnit.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.unit_empty), Toast.LENGTH_SHORT).show()
            else if (updateTrtStartDate.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.start_date_empty), Toast.LENGTH_SHORT).show()
            else if (updateTrtEndDate.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.end_date_empty), Toast.LENGTH_SHORT).show()
            else if (updateTrtRemindHourMin.text.toString().isNullOrEmpty()) Toast.makeText(this, resources.getString(R.string.time_empty), Toast.LENGTH_SHORT).show()
            else {
                val startDate = LocalDateTime.of(startYear, startMonth+1, startDay, setHour, setMin)
                val startTimestamp = Timestamp(startDate.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
                val endDate = LocalDateTime.of(endYear, endMonth+1, endDay, setHour, setMin)
                val endTimestamp = Timestamp(endDate.toEpochSecond(ZoneId.systemDefault().rules.getOffset(Instant.now())), 0)
                val freq =
                    if (updateTrtFreq.selectedItem.toString() == "Monthly" || updateTrtFreq.selectedItem.toString() == "Bulanan") 30
                    else if (updateTrtFreq.selectedItem.toString() == "Weekly" || updateTrtFreq.selectedItem.toString() == "Mingguan") 7
                    else 1

                val nextoccurrence = Treatment.count_next_occurrence(freq, startTimestamp, endTimestamp, setHour, setMin)
                Treatment.update_treatment(
                    documentid,
                    updateTrtName.text.toString(),
                    updateTrtDose.text.toString().toDouble(),
                    updateTrtUnit.text.toString(),
                    freq,
                    nextoccurrence,
                    startTimestamp,
                    endTimestamp,
                    setHour,
                    setMin
                    )
                Treatment.fetch_all_treatments()
                Toast.makeText(this, resources.getString(R.string.update_trt_success), Toast.LENGTH_SHORT).show()
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