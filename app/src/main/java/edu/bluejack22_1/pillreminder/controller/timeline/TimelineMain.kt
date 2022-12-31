package edu.bluejack22_1.pillreminder.controller.timeline

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.adapter.TimelineAdapter
import edu.bluejack22_1.pillreminder.controller.appointment.AppointmentsDetail
import edu.bluejack22_1.pillreminder.controller.doctor.DocContactMain
import edu.bluejack22_1.pillreminder.controller.treatment.TreatmentsAdd
import edu.bluejack22_1.pillreminder.controller.treatment.TreatmentsDetail
import edu.bluejack22_1.pillreminder.databinding.ActivityTimelineMainBinding
import edu.bluejack22_1.pillreminder.model.Appointment
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.Timeline
import edu.bluejack22_1.pillreminder.model.User

class TimelineMain : AppCompatActivity(), TimelineAdapter.TimelineListener {
    private lateinit var binding: ActivityTimelineMainBinding
    private lateinit var backButton: ImageView
    private lateinit var toAddAppTL: ImageView
    private lateinit var toAddTrtTL: ImageView
    private lateinit var rvTLMain: RecyclerView
    private lateinit var tlAdapter: TimelineAdapter
    private lateinit var layoutManager: LinearLayoutManager

    val ITEM_TRT = 1
    val ITEM_APT = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineMainBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener { finish() }
        toAddAppTL = binding.toAddAppTL
        toAddAppTL.setOnClickListener { startActivity(Intent(this, DocContactMain::class.java)) }
        toAddTrtTL = binding.toAddTrtTL
        toAddTrtTL.setOnClickListener { startActivity(Intent(this, TreatmentsAdd::class.java)) }
        rvTLMain = binding.rvTLMain
        buildRecyclerView()
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        buildRecyclerView()
    }

    private fun buildRecyclerView() {
        rvTLMain = binding.rvTLMain
        layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        Timeline.fetch_all_timeline()
        tlAdapter = TimelineAdapter(Timeline.allTimeline, this)
        layoutManager.scrollToPosition(Timeline.get_afternow())
        rvTLMain.layoutManager = layoutManager
        rvTLMain.adapter = tlAdapter
    }

    override fun onTimelineClicked(pos: Int) {
        val tl = Timeline.allTimeline[pos]
        if (tl.timelinetype == ITEM_TRT) {
            val intent = Intent(this, TreatmentsDetail::class.java)
            intent.putExtra("documentid", tl.documentid)
            startActivity(intent)
        } else {
            val intent = Intent(this, AppointmentsDetail::class.java)
            val apt = Appointment.get_appointments_documentid(tl.documentid)!!
            intent.putExtra("documentid", apt.documentid)
            intent.putExtra("docconid", apt.docconid)
            intent.putExtra("doctorid", apt.doctorid)
            intent.putExtra("patientid", apt.patientid)
            intent.putExtra("datetime", apt.datetime)
            intent.putExtra("place", apt.place)
            intent.putExtra("address", apt.address)
            intent.putExtra("note", apt.note)
            if (!User.curr.role.equals("doctors")) {
                val doc = DoctorContact.get_doctorcontacts_documentid(apt.docconid.toString())!!
                intent.putExtra("doccon", doc)
                if (!doc.photo.equals("unregistered")) {
                    val opponent = User.get_user_uid(apt.doctorid.toString())!!
                    intent.putExtra("opponent", opponent)
                }
            } else {
                val opponent = User.get_user_uid(apt.patientid.toString())!!
                intent.putExtra("opponent", opponent)
            }
            startActivity(intent)
        }
    }
}