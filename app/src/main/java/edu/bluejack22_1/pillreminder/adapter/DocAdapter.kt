package edu.bluejack22_1.pillreminder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.model.DoctorContact

class DocAdapter(var docs: MutableList<DoctorContact>, var listener: DocClickListener) : RecyclerView.Adapter<DocAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.item_doc_contact, viewGroup, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        vh.docName.text = docs[position].name
        vh.docPhone.text = docs[position].phone
        vh.docMail.text = docs[position].email
        if (!docs[position].photo.equals("unregistered"))
            Picasso.get().load(docs[position].photo).into(vh.docPhoto)
        if (docs[position].doctorid.equals("unregistered"))
            vh.docChat.visibility = View.INVISIBLE
    }

    override fun getItemCount() = docs.size

    class ViewHolder(view: View, listener: DocClickListener) : RecyclerView.ViewHolder(view) {
        val docPhoto: ShapeableImageView
        val docName: TextView
        val docPhone: TextView
        val docMail: TextView
        val docChat: ImageView
        val docAppointment: ImageView
        val cvDoc: CardView

        init {
            docPhoto = view.findViewById(R.id.docPhoto)
            docName = view.findViewById(R.id.docName)
            docPhone = view.findViewById(R.id.docPhone)
            docMail = view.findViewById(R.id.docMail)
            docChat = view.findViewById(R.id.docChat)
            docChat.setOnClickListener { listener.onDocChatClicked(adapterPosition) }
            docAppointment = view.findViewById(R.id.docAppointment)
            docAppointment.setOnClickListener{ listener.onDocAppointmentClicked(adapterPosition) }
            cvDoc = view.findViewById(R.id.cvDoc)
            cvDoc.setOnClickListener { listener.onDocClicked(adapterPosition) }
        }
    }

    interface DocClickListener{
        fun onDocClicked(pos: Int)
        fun onDocChatClicked(pos: Int)
        fun onDocAppointmentClicked(pos: Int)
    }
}