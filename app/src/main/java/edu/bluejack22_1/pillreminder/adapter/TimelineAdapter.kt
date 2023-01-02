package edu.bluejack22_1.pillreminder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ItemAppBinding
import edu.bluejack22_1.pillreminder.databinding.ItemTrtBinding
import edu.bluejack22_1.pillreminder.model.*
import java.text.SimpleDateFormat

class TimelineAdapter(var currTl: MutableList<Timeline>, var listener: TimelineListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val timeFormat: SimpleDateFormat = SimpleDateFormat("HH:mm")
    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
    val dateTimeFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    val ITEM_TRT = 1
    val ITEM_APT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==ITEM_TRT) {
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_trt, parent, false)
            ItemTrtHolder(view, listener)
        } else {
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_app, parent, false)
            ItemAptHolder(view, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val tl = currTl[position]
        if (holder.javaClass == ItemTrtHolder::class.java) {
            val vh = holder as ItemTrtHolder
            val trt = Treatment.get_treatments_documentid(tl.documentid.toString())!!
            vh.binding.trtName.text = trt.name
            vh.binding.trtDoseUnit.text = String.format("%.2f %s", trt.dose, trt.unit)
            vh.binding.trtNextOccurrence.text =  dateTimeFormat.format(trt.nextoccurrence.toDate()).toString()
            vh.binding.cvTrt.setOnClickListener { listener.onTimelineClicked(position) }
        } else {
            lateinit var doc: DoctorContact
            lateinit var opponent: User
            val vh = holder as ItemAptHolder
            val apt = Appointment.get_appointments_documentid(tl.documentid.toString())!!
            if (!User.curr.role.equals("doctors")) {
                doc = DoctorContact.get_doctorcontacts_documentid(apt.docconid.toString())!!
                if (!doc.photo.equals("unregistered")) {
                    opponent = User.get_user_uid(apt.doctorid.toString())!!
                    vh.binding.appDocName.text = opponent.name.toString()
                    Picasso.get().load(opponent.photo).into(vh.binding.appDocPhoto)
                } else {
                    vh.binding.appDocName.text = doc.name.toString()
                }
            } else {
                opponent = User.get_user_uid(apt.patientid.toString())!!
                vh.binding.appDocName.text = opponent.name.toString()
                Picasso.get().load(opponent.photo).into(vh.binding.appDocPhoto)
            }
            vh.binding.appNote.text = apt.note
            vh.binding.appDatetime.text = dateTimeFormat.format(apt.datetime.toDate()).toString()
            vh.binding.cvApp.setOnClickListener { listener.onTimelineClicked(position) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val tl = currTl[position]
        return if (tl.timelinetype==ITEM_TRT) ITEM_TRT else ITEM_APT
    }
    override fun getItemCount(): Int = currTl.size

    inner class ItemTrtHolder(view: View, listener: TimelineListener): RecyclerView.ViewHolder(view) {
        var binding:ItemTrtBinding = ItemTrtBinding.bind(itemView)
    }

    inner class ItemAptHolder(view: View, listener: TimelineListener): RecyclerView.ViewHolder(view) {
        var binding:ItemAppBinding = ItemAppBinding.bind(itemView)
    }

    interface TimelineListener {
        fun onTimelineClicked(pos: Int)
    }
}
