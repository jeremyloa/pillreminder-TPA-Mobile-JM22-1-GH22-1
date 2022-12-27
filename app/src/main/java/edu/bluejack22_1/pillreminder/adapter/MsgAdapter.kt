package edu.bluejack22_1.pillreminder.adapter

import android.content.Context
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

class MsgAdapter (var ctx: Context, msgs:MutableList<Msg>): RecyclerView.Adapter<RecyclerView.ViewHolder?>() {

    lateinit var msgs:MutableList<Msg>
    val ITEM_SENT = 1
    val ITEM_RECEIVE = 2

    inner class ItemChatSendHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        var binding:ItemChatSendBinding = ItemChatSendBinding.bind(itemView)
    }

    inner class ItemChatRecHolder (itemView:View):RecyclerView.ViewHolder(itemView){
        var binding:ItemChatRecBinding = ItemChatRecBinding.bind(itemView)
    }

    init {
        if (!msgs.isNullOrEmpty()){
            this.msgs = msgs
        }
    }

    override fun getItemViewType(position: Int): Int {
        val msg = msgs[position]
        return if (User.curr.uid == msg.sender) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==ITEM_SENT) {
            val view:View = LayoutInflater.from(ctx).inflate(R.layout.item_chat_send, parent, false)
            ItemChatSendHolder(view)
        } else {
            val view:View = LayoutInflater.from(ctx).inflate(R.layout.item_chat_rec, parent, false)
            ItemChatRecHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = msgs[position]
        if (holder.javaClass ==  ItemChatSendHolder::class.java) {
            val viewHolder = holder as ItemChatSendHolder
            if (msg.type.equals("photo")) {
                viewHolder.binding.sendImg.visibility = View.VISIBLE
                viewHolder.binding.sendText.visibility = View.GONE
                Picasso.get().load(msgs[position].ctn).into(viewHolder.binding.sendImg)
            } else {
                viewHolder.binding.sendText.text = msgs[position].ctn
            }
            viewHolder.binding.sendTime.text = msgs[position].ctn
        } else {
            val viewHolder = holder as ItemChatRecHolder
            if (msg.type.equals("photo")) {
                viewHolder.binding.recImg.visibility = View.VISIBLE
                viewHolder.binding.recText.visibility = View.GONE
                Picasso.get().load(msgs[position].ctn).into(viewHolder.binding.recImg)
            } else {
                viewHolder.binding.recText.text = msgs[position].ctn
            }
            viewHolder.binding.recTime.text = msgs[position].ctn
        }
    }

    override fun getItemCount(): Int  = msgs.size


}