package edu.bluejack22_1.pillreminder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.databinding.ItemChatRecBinding
import edu.bluejack22_1.pillreminder.databinding.ItemChatSendBinding
import edu.bluejack22_1.pillreminder.model.Msg
import edu.bluejack22_1.pillreminder.model.User
import java.text.SimpleDateFormat

class MsgAdapter(var currMsg: MutableList<Msg>, var listener: MsgListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dateFormat: SimpleDateFormat = SimpleDateFormat("HH:mm")
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
         return if (viewType==ITEM_SENT) {
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_send, parent, false)
            ItemChatSendHolder(view, listener)
        } else {
            val view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_chat_rec, parent, false)
            ItemChatRecHolder(view, listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = currMsg[position]
        if (holder.javaClass ==  ItemChatSendHolder::class.java) {
            val viewHolder = holder as ItemChatSendHolder
            if (msg.type.equals("photo")) {
                viewHolder.binding.sendImg.visibility = View.VISIBLE
                viewHolder.binding.sendText.visibility = View.GONE
                Picasso.get().load(currMsg[position].ctn).into(viewHolder.binding.sendImg)
            } else {
                viewHolder.binding.sendImg.visibility = View.GONE
                viewHolder.binding.sendText.visibility = View.VISIBLE
                viewHolder.binding.sendText.text = currMsg[position].ctn
            }
            viewHolder.binding.sendTime.text = dateFormat.format(currMsg[position].sent!!.toDate()).toString()
            viewHolder.binding.sendCard.setOnClickListener{
                listener.orMsgClicked(position)
            }
        } else {
            val viewHolder = holder as ItemChatRecHolder
            if (msg.type.equals("photo")) {
                viewHolder.binding.recImg.visibility = View.VISIBLE
                viewHolder.binding.recText.visibility = View.GONE
                Picasso.get().load(currMsg[position].ctn).into(viewHolder.binding.recImg)
            } else {
                viewHolder.binding.recImg.visibility = View.GONE
                viewHolder.binding.recText.visibility = View.VISIBLE
                viewHolder.binding.recText.text = currMsg[position].ctn
            }
            viewHolder.binding.recTime.text = dateFormat.format(currMsg[position].sent!!.toDate()).toString()
            viewHolder.binding.recCard.setOnClickListener{
                listener.orMsgClicked(position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val msg = currMsg[position]
        return if (User.curr.uid == msg.sender) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun getItemCount(): Int = currMsg.size


    inner class ItemChatSendHolder(view: View, listener: MsgListener): RecyclerView.ViewHolder(view) {
        var binding:ItemChatSendBinding = ItemChatSendBinding.bind(itemView)
    }

    inner class ItemChatRecHolder(view: View, listener: MsgListener): RecyclerView.ViewHolder(view) {
        var binding:ItemChatRecBinding = ItemChatRecBinding.bind(itemView)
    }

    interface MsgListener {
        fun orMsgClicked(pos: Int)
    }

}