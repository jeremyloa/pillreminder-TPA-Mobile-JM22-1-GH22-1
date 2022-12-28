package edu.bluejack22_1.pillreminder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.MsgRoom
import edu.bluejack22_1.pillreminder.model.User
import java.text.SimpleDateFormat

class MsgRoomAdapter (var rooms: MutableList<MsgRoom>, var listener: MsgRoomListener): RecyclerView.Adapter<MsgRoomAdapter.ViewHolder>() {
    val dateFormat: SimpleDateFormat = SimpleDateFormat("HH:mm")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(vh: ViewHolder, position: Int) {
        if (rooms.isNullOrEmpty()) return
        if (rooms[position].doctorid == User.curr.uid) {
            vh.itemChatName.text = User.get_user_uid(rooms[position].patientid)!!.name
            Picasso.get().load(User.get_user_uid(rooms[position].patientid)!!.photo).into(vh.itemChatPhoto)
        } else {
            vh.itemChatName.text = DoctorContact.get_doctorcontacts_doctorid(rooms[position].doctorid!!)!!.name
            Picasso.get().load(User.get_user_uid(rooms[position].doctorid)!!.photo).into(vh.itemChatPhoto)
        }
        if (rooms[position].lasttime != null) {
            vh.itemChatTime.text = dateFormat.format(rooms[position].lasttime?.toDate()).toString()
        } else {
            vh.itemChatTime.text = ""
        }

        if (rooms[position].lastmsg != null) {
            vh.itemChatText.text = rooms[position].lastmsg
        } else {
            vh.itemChatText.text = ""
        }
    }

    override fun getItemCount() = rooms.size

    class ViewHolder(view: View, listener: MsgRoomListener): RecyclerView.ViewHolder(view) {
        val itemChatPhoto: ShapeableImageView
        val itemChatName: TextView
        val itemChatTime: TextView
        val itemChatText: TextView
        val llChat: LinearLayout
        init {
            itemChatPhoto = view.findViewById(R.id.itemChatPhoto)
            itemChatName = view.findViewById(R.id.itemChatName)
            itemChatTime = view.findViewById(R.id.itemChatTime)
            itemChatText = view.findViewById(R.id.itemChatText)
            llChat = view.findViewById(R.id.llChat)
            llChat.setOnClickListener{ listener.omRoomClicked(adapterPosition) }
        }
    }

    interface MsgRoomListener {
        fun omRoomClicked(pos: Int)
    }
}