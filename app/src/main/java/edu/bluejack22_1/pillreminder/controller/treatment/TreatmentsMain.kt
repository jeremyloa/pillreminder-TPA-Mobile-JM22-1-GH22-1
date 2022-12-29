package edu.bluejack22_1.pillreminder.controller.treatment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.adapter.ActivityAdapter

class TreatmentsMain : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_treatments_main, container, false)
    }



    companion object {
        @JvmStatic
        fun newInstance() =
            TreatmentsMain().apply {
                arguments = Bundle().apply {
                }
            }
    }
}