package edu.bluejack22_1.pillreminder.controller.treatment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class TreatmentsMain : Fragment() {



    companion object {
        @JvmStatic
        fun newInstance() =
            TreatmentsMain().apply {
                arguments = Bundle().apply {
                }
            }
    }
}