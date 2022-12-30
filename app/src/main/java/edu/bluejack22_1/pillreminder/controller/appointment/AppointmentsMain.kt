package edu.bluejack22_1.pillreminder.controller.appointment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.adapter.ActivityAdapter
import edu.bluejack22_1.pillreminder.adapter.AppAdapter
import edu.bluejack22_1.pillreminder.controller.doctor.DocContactMain
import edu.bluejack22_1.pillreminder.databinding.FragmentAppointmentsMainBinding
import edu.bluejack22_1.pillreminder.model.Appointment
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.User

class AppointmentsMain : Fragment(), AppAdapter.AppListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentAppointmentsMainBinding
    private lateinit var toAddApp: ImageView
    private lateinit var rvApp: RecyclerView
    private lateinit var appAdapter: AppAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAppointmentsMainBinding.inflate(layoutInflater, container, false)
        toAddApp = binding.toAddApp
        toAddApp.setOnClickListener {
            startActivity(Intent(context, DocContactMain::class.java))
        }
//        buildRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        buildRecyclerView()
    }
    private fun buildRecyclerView(){
        rvApp = binding.rvApp
        rvApp.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        if (User.curr.role.equals("doctors"))  appAdapter = AppAdapter(Appointment.get_appointments_doctorid(User.curr.uid), this)
        else appAdapter = AppAdapter(Appointment.get_appointments_patientid(User.curr.uid), this)
        rvApp.adapter = appAdapter
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            AppointmentsMain().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onAppClicked(pos: Int) {
        val intent = Intent(context, AppointmentsDetail::class.java)
        val apts =  if (User.curr.role.equals("doctors")) Appointment.get_appointments_doctorid(User.curr.uid)
                    else Appointment.get_appointments_patientid(User.curr.uid)
        val apt = apts[pos]
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