package edu.bluejack22_1.pillreminder.controller.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_1.pillreminder.adapter.TimelineAdapter
import edu.bluejack22_1.pillreminder.controller.Notification
import edu.bluejack22_1.pillreminder.controller.Splash
import edu.bluejack22_1.pillreminder.controller.appointment.AppointmentsDetail
import edu.bluejack22_1.pillreminder.controller.doctor.DocContactMain
import edu.bluejack22_1.pillreminder.controller.timeline.TimelineMain
import edu.bluejack22_1.pillreminder.controller.treatment.TreatmentsDetail
import edu.bluejack22_1.pillreminder.databinding.FragmentHomeBinding
import edu.bluejack22_1.pillreminder.model.*

class Home : Fragment(), TimelineAdapter.TimelineListener {
    private lateinit var binding: FragmentHomeBinding

    val ITEM_TRT = 1
    val ITEM_APT = 2
    private lateinit var rvTLHome: RecyclerView
    private lateinit var tlAdapter: TimelineAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var toTimeline: CardView
    private lateinit var toDoctorContactList: CardView
    private lateinit var toLogout: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Timeline.fetch_all_timeline()
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        rvTLHome = binding.rvTLHome
        toTimeline = binding.toTimeline
        toTimeline.setOnClickListener { startActivity(Intent(context, TimelineMain::class.java)) }
        toDoctorContactList = binding.toDoctorContactList
        if (User.curr.role=="doctors") toDoctorContactList.visibility = View.GONE
        toDoctorContactList.setOnClickListener {
            startActivity(Intent(context, DocContactMain::class.java))
        }
        toLogout = binding.toLogout
        toLogout.setOnClickListener{
            User.logout()
            startActivity(Intent(context, Splash::class.java))
            activity?.finish()
        }
        buildRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        buildRecyclerView()
    }
   companion object {
        fun newInstance(): Home{
            val fragment = Home()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }

    private fun buildRecyclerView() {
        rvTLHome = binding.rvTLHome
        layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//        Timeline.fetch_all_timeline()
        Notification.schedule_notifications(requireContext())
        tlAdapter = TimelineAdapter(Timeline.allTimeline, this)
        layoutManager.scrollToPosition(Timeline.get_afternow())
        rvTLHome.layoutManager = layoutManager
        rvTLHome.adapter = tlAdapter
    }

    override fun onTimelineClicked(pos: Int) {
        val tl = Timeline.allTimeline[pos]
        if (tl.timelinetype == ITEM_TRT) {
            val intent = Intent(context, TreatmentsDetail::class.java)
            intent.putExtra("documentid", tl.documentid)
            startActivity(intent)
        } else {
            val intent = Intent(context, AppointmentsDetail::class.java)
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