package edu.bluejack22_1.pillreminder.controller.treatment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ActivityTreatmentsDetailBinding
import edu.bluejack22_1.pillreminder.model.Treatment
import java.text.SimpleDateFormat

class TreatmentsDetail : AppCompatActivity() {
    private lateinit var binding: ActivityTreatmentsDetailBinding
    private lateinit var backButton: ImageView
    private lateinit var trtDetailTitle: TextView
    private lateinit var trtDetailDoseUnit: TextView
    private lateinit var trtDetailNextOccurrence: TextView
    private lateinit var trtFrequency: TextView
    private lateinit var trtStartEnd: TextView
    private lateinit var toUpdateTrt: Button
    private lateinit var toDeleteTrt: Button

    private lateinit var documentid:String
    private lateinit var trt: Treatment

    val dateTimeFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        documentid = intent.getStringExtra("documentid").toString()
        trt = Treatment.get_treatments_documentid(documentid)!!

        binding = ActivityTreatmentsDetailBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener{
            finish()
        }
        trtDetailTitle = binding.trtDetailTitle
        trtDetailTitle.text = trt.name
        trtDetailDoseUnit = binding.trtDetailDoseUnit
        trtDetailDoseUnit.text = String.format("%.2f %s", trt.dose, trt.unit)
        trtDetailNextOccurrence = binding.trtDetailNextOccurrence
        trtDetailNextOccurrence.text = String.format("%s: %s", resources.getString(R.string.Next), dateTimeFormat.format(trt.nextoccurrence.toDate()).toString())
        trtFrequency = binding.trtFrequency
        trtFrequency.text =
            if (trt.frequency==1) resources.getString(R.string.daily)
            else if (trt.frequency==7) resources.getString(R.string.weekly)
            else resources.getString(R.string.monthly)

        trtStartEnd = binding.trtStartEnd
        trtStartEnd.text = String.format("%s %s %s %s", resources.getString(R.string.From), dateFormat.format(trt.startdate.toDate()).toString(), resources.getString(R.string.to), dateFormat.format(trt.enddate.toDate()).toString())
        toUpdateTrt = binding.toUpdateTrt
        toUpdateTrt.setOnClickListener {
            var intent = Intent(this, TreatmentsUpdate::class.java)
            intent.putExtra("documentid", documentid)
            startActivity(intent)
            finish()
        }
        toDeleteTrt = binding.toDeleteTrt
        toDeleteTrt.setOnClickListener {
            Treatment.delete_treatment(documentid)
            Treatment.fetch_all_treatments()
            Toast.makeText(this, resources.getString(R.string.delete_trt_success), Toast.LENGTH_LONG).show()
            Handler(Looper.getMainLooper()).postDelayed({
                finish()
            }, 3000)
        }
        setContentView(binding.root)
    }
}
