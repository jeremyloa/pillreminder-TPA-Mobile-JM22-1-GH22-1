package edu.bluejack22_1.pillreminder.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import edu.bluejack22_1.pillreminder.controller.appointment.AppointmentsMain
import edu.bluejack22_1.pillreminder.controller.treatment.TreatmentsMain

class ActivityAdapter(fm: Fragment): FragmentStateAdapter(fm) {
    private val pages = listOf(
        AppointmentsMain.newInstance(),
        TreatmentsMain.newInstance()
    )

    override fun getItemCount(): Int {
        return pages.size
    }

    override fun createFragment(position: Int): Fragment {
        return pages[position]
    }



}