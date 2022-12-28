package edu.bluejack22_1.pillreminder.controller.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.adapter.MsgAdapter
import edu.bluejack22_1.pillreminder.databinding.ActivityChatSearchBinding
import edu.bluejack22_1.pillreminder.model.Msg

class ChatSearch : AppCompatActivity(), MsgAdapter.MsgListener {

    private lateinit var binding: ActivityChatSearchBinding
    private lateinit var backButton: ImageView
    private lateinit var chatSearchTxt: TextInputEditText
    private lateinit var chatSearchBtn: ImageButton
    private lateinit var rvChatSearch: RecyclerView
    private lateinit var msgAdapter: MsgAdapter
    private lateinit var temp: MutableList<Msg>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        temp = mutableListOf()
        binding = ActivityChatSearchBinding.inflate(layoutInflater)
        backButton = binding.backButton
        backButton.setOnClickListener{
            finish()
        }
        chatSearchTxt = binding.chatSearchTxt
        chatSearchBtn = binding.chatSearchBtn
        chatSearchBtn.setOnClickListener {
            query(chatSearchTxt.text.toString())
        }
        rvChatSearch = binding.rvChatSearch

        setContentView(binding.root)
    }

    fun query(query:String){
        rvChatSearch = binding.rvChatSearch
        rvChatSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        temp.clear()
        for (msg in ChatPage.msgs){
            if (msg.type == "text" && msg.ctn!!.contains(query, true)) temp.add(msg)
        }
        msgAdapter = MsgAdapter(temp, this)
        rvChatSearch.adapter = msgAdapter
    }

    override fun orMsgClicked(pos: Int) {
//        var chatid:String = temp[pos].documentid.toString()
//        Log.i("CHAT_CLICKED", pos.toString() + " chatid = " + temp[pos].documentid)
//        var count = 0
//        for (msg in ChatPage.msgs){
//            Log.i("CHAT_SEARCH", "Click = " + chatid + " Msg = " + msg.documentid)
//            if (msg.documentid==chatid) {
//                ChatPage.layoutManager.scrollToPosition(count)
//                Log.i("FOUND_CHAT", count.toString())
//                finish()
//            }
//            count++
//        }
    }
}