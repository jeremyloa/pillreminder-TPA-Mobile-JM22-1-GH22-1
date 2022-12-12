package edu.bluejack22_1.pillreminder.controller.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.controller.doctor.DocContactMain
import edu.bluejack22_1.pillreminder.databinding.FragmentHomeBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Home.newInstance] factory method to
 * create an instance of this fragment.
 */
class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private lateinit var toTimeline: CardView
    private lateinit var toAppointment: CardView
    private lateinit var toDoctorContactList: CardView

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DoctorContact.fetch_all_doctorcontacts_patientid()
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        toTimeline = binding.toTimeline
        toAppointment = binding.toAppointment
        toDoctorContactList = binding.toDoctorContactList
        toDoctorContactList.setOnClickListener {
            startActivity(
                Intent(
                    context,
                    DocContactMain::class.java
                )
            )
        }
        return binding.root
    }

   companion object {
        fun newInstance(): Home{
            val fragment = Home()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}