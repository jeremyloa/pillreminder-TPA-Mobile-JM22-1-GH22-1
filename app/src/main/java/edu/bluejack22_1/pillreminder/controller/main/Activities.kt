package edu.bluejack22_1.pillreminder.controller.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.adapter.ActivityAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Activities.newInstance] factory method to
 * create an instance of this fragment.
 */
class Activities : Fragment() {
    private lateinit var vpActivities: ViewPager2
    private lateinit var adapter: ActivityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_activities, container, false)
        vpActivities = view.findViewById(R.id.vpActivities)

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = ActivityAdapter(this)
        vpActivities.adapter = adapter
    }

    companion object {
        fun newInstance(): Activities{
            val fragment = Activities()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}