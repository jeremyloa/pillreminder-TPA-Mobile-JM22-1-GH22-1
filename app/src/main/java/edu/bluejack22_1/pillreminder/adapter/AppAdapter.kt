package edu.bluejack22_1.pillreminder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.model.Appointment
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.User
import java.text.SimpleDateFormat

class AppAdapter(var currApp: MutableList<Appointment>, var listener: AppListener) : RecyclerView.Adapter<AppAdapter.ViewHolder>() {
    lateinit var doc: DoctorContact
    lateinit var opponent: User
    val dateFormat: SimpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_app, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        if (!User.curr.role.equals("doctors")) {
            doc = DoctorContact.get_doctorcontacts_documentid(currApp[position].docconid.toString())!!
            if (!doc.photo.equals("unregistered")) opponent = User.get_user_uid(currApp[position].doctorid.toString())!!
        } else {
            opponent = User.get_user_uid(currApp[position].patientid.toString())!!
        }
        vh.appDocName.text = if (::doc.isInitialized) doc.name.toString() else opponent.name.toString()
        vh.appNote.text = currApp[position].note
        vh.appDatetime.text = dateFormat.format(currApp[position].datetime.toDate()).toString()
        if (::opponent.isInitialized) Picasso.get().load(opponent.photo).into(vh.appDocPhoto)
        vh.cvApp.setOnClickListener { listener.onAppClicked(position) }
    }

    override fun getItemCount() = currApp.size

    class ViewHolder(view: View, listener: AppListener) : RecyclerView.ViewHolder(view) {
        val appDocPhoto: ShapeableImageView
        val appDocName: TextView
        val appDatetime: TextView
        val appNote: TextView
        val cvApp: CardView
        init {
            appDocPhoto = view.findViewById(R.id.appDocPhoto)
            appDocName = view.findViewById(R.id.appDocName)
            appDatetime = view.findViewById(R.id.appDatetime)
            appNote = view.findViewById(R.id.appNote)
            cvApp = view.findViewById(R.id.cvApp)
            cvApp.setOnClickListener { listener.onAppClicked(adapterPosition) }
        }
    }

    interface AppListener {
        fun onAppClicked(pos: Int)
    }

}