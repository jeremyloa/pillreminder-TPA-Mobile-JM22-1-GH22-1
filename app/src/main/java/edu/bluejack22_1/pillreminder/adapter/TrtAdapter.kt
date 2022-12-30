package edu.bluejack22_1.pillreminder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.model.Treatment
import java.text.SimpleDateFormat

class TrtAdapter (var currTrt: MutableList<Treatment>, var listener: TrtListener) : RecyclerView.Adapter<TrtAdapter.ViewHolder>() {

    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_trt, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.trtName.text = currTrt[position].name
        vh.trtDoseUnit.text = String.format("%.2f %s", currTrt[position].dose, currTrt[position].unit)
        vh.trtNextOccurrence.text =  dateFormat.format(currTrt[position].nextoccurrence.toDate()).toString()
        vh.cvTrt.setOnClickListener { listener.onTrtClicked(position) }
    }

    override fun getItemCount() = currTrt.size

    class ViewHolder(view: View, listener: TrtListener): RecyclerView.ViewHolder(view) {
        val trtName: TextView
        val trtDoseUnit: TextView
        val trtNextOccurrence: TextView
        val cvTrt: CardView
        init {
            trtName = view.findViewById(R.id.trtName)
            trtDoseUnit = view.findViewById(R.id.trtDoseUnit)
            trtNextOccurrence = view.findViewById(R.id.trtNextOccurrence)
            cvTrt = view.findViewById(R.id.cvTrt)
            cvTrt.setOnClickListener { listener.onTrtClicked(adapterPosition) }
        }
    }

    interface TrtListener {
        fun onTrtClicked(pos: Int)
    }

}