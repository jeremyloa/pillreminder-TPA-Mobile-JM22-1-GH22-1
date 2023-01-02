package edu.bluejack22_1.pillreminder.controller.treatment

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
import edu.bluejack22_1.pillreminder.adapter.TrtAdapter
import edu.bluejack22_1.pillreminder.databinding.FragmentTreatmentsMainBinding
import edu.bluejack22_1.pillreminder.model.Treatment
import edu.bluejack22_1.pillreminder.model.User

class TreatmentsMain : Fragment(), TrtAdapter.TrtListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var binding: FragmentTreatmentsMainBinding
    private lateinit var toAddTrt: ImageView
    private lateinit var rvTrt: RecyclerView
    private lateinit var trtAdapter: TrtAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTreatmentsMainBinding.inflate(layoutInflater, container, false)
        toAddTrt = binding.toAddTrt
        toAddTrt.setOnClickListener {
            startActivity(Intent(context, TreatmentsAdd::class.java))
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        buildRecyclerView()
    }

    private fun buildRecyclerView() {
        rvTrt = binding.rvTrt
        rvTrt.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        trtAdapter = TrtAdapter(Treatment.get_treatments_patientid(User.curr.uid.toString()), this)
        rvTrt.adapter = trtAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TreatmentsMain().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onTrtClicked(pos: Int) {
        var intent = Intent(context, TreatmentsDetail::class.java)
        val trts = Treatment.get_treatments_patientid(User.curr.uid.toString())
        val trt = trts[pos]
        intent.putExtra("documentid", trt.documentid)
        startActivity(intent)
    }
}