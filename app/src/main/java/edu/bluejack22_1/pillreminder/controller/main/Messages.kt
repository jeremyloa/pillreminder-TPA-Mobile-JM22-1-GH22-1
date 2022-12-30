package edu.bluejack22_1.pillreminder.controller.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.adapter.MsgRoomAdapter
import edu.bluejack22_1.pillreminder.controller.chat.ChatPage
import edu.bluejack22_1.pillreminder.databinding.FragmentMessagesBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.Msg
import edu.bluejack22_1.pillreminder.model.MsgRoom
import edu.bluejack22_1.pillreminder.model.User

class Messages : Fragment(), MsgRoomAdapter.MsgRoomListener {
    companion object {
        lateinit var msgRoomAdapter: MsgRoomAdapter
        fun adapterInitialized(): Boolean{
            if (::msgRoomAdapter.isInitialized) return true
            return false
        }
        fun newInstance(): Messages{
            val fragment = Messages()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
    private lateinit var binding: FragmentMessagesBinding
    private lateinit var rvMsgRoom: RecyclerView
    private lateinit var searchMsgText: TextInputEditText
    private lateinit var searchMsgBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(layoutInflater, container, false)
        searchMsgText = binding.searchMsgText
        searchMsgBtn = binding.searchMsgBtn
        searchMsgBtn.setOnClickListener{
            val query = searchMsgText.text.toString()
            if (query.isNullOrEmpty()) {
                buildRecyclerView()
            } else {
                searchRecyclerView(query)
            }
        }
        buildRecyclerView()
        return binding.root
    }

    override fun onResume() {
        super.onResume()
//        Log.i("CHECK_MSG_ROOM", MsgRoom.allMsgRoom.size.toString())
//        MsgRoom.allMsgRoom.clear()
//        MsgRoom.fetch_all_msgrooms()
//        buildRecyclerView()
    }

    private fun buildRecyclerView(){
        rvMsgRoom = binding.rvMsgRoom
        rvMsgRoom.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        if (MsgRoom.allMsgRoom.isNullOrEmpty()) Log.i("ROOM_RV", "is null")
        else Log.i("ROOM_RV", MsgRoom.allMsgRoom[0].chatroomid.toString())
        msgRoomAdapter = MsgRoomAdapter(MsgRoom.allMsgRoom, this)
        rvMsgRoom.adapter = msgRoomAdapter
    }

    private fun searchRecyclerView(query: String){
        rvMsgRoom = binding.rvMsgRoom
        rvMsgRoom.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        if (MsgRoom.allMsgRoom.isNullOrEmpty()) Log.i("ROOM_RV", "is null")
        else Log.i("ROOM_RV", MsgRoom.allMsgRoom[0].chatroomid.toString())
        msgRoomAdapter = MsgRoomAdapter(MsgRoom.search_msgroom(query), this)
        rvMsgRoom.adapter = msgRoomAdapter
    }


    override fun omRoomClicked(pos: Int) {
//        Msg.fetch_curr_msg(MsgRoom.allMsgRoom[pos].chatroomid.toString())
        var intent = Intent(context, ChatPage::class.java)
        intent.putExtra("lasttime", MsgRoom.allMsgRoom[pos].lasttime)
        intent.putExtra("lastmsg", MsgRoom.allMsgRoom[pos].lastmsg)

        intent.putExtra("chatroomid", MsgRoom.allMsgRoom[pos].chatroomid)
        intent.putExtra("doctorid", MsgRoom.allMsgRoom[pos].doctorid)
        if (MsgRoom.allMsgRoom[pos].patientid == User.curr.uid) {
            intent.putExtra("doctorname", DoctorContact.get_doctorcontacts_doctorid(MsgRoom.allMsgRoom[pos].doctorid!!)!!.name)
        } else {
            intent.putExtra("doctorname", "")
        }
        intent.putExtra("patientid", MsgRoom.allMsgRoom[pos].patientid)
        Log.i("ROOM_CLICKED", "Pos = " + pos + " roomid = " + MsgRoom.allMsgRoom[pos].chatroomid)
//        Log.i("CLICK_DOC_MSG_ROOM", MsgRoom.allMsgRoom[pos].chatroomid.toString())
        startActivity(intent)
    }
}