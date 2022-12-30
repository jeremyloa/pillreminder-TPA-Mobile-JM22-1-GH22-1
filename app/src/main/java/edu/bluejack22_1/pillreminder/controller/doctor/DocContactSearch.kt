package edu.bluejack22_1.pillreminder.controller.doctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.pillreminder.adapter.DocAdapter
import edu.bluejack22_1.pillreminder.controller.appointment.AppointmentsAdd
import edu.bluejack22_1.pillreminder.controller.chat.ChatPage
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactSearchBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.Msg
import edu.bluejack22_1.pillreminder.model.MsgRoom
import edu.bluejack22_1.pillreminder.model.User

class DocContactSearch : AppCompatActivity(), DocAdapter.DocClickListener {
    private lateinit var binding: ActivityDocContactSearchBinding
    private lateinit var rvDocSearch: RecyclerView
    private lateinit var docAdapter: DocAdapter
    private lateinit var searchDocSearchText: TextInputEditText
    private lateinit var searchDocSearchBtn: ImageButton

    private lateinit var backButton: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDocContactSearchBinding.inflate(layoutInflater)
        searchDocSearchText = binding.searchDocSearchText
        searchDocSearchText.setText(intent.getStringExtra("query").toString())
        searchDocSearchBtn = binding.searchDocSearchBtn
        searchDocSearchBtn.setOnClickListener {
            if (!searchDocSearchText.toString().isNullOrEmpty()) {
                var intent = Intent(this, DocContactSearch::class.java)
                intent.putExtra("query", searchDocSearchText.text.toString())
                startActivity(intent)
            }
        }
        backButton = binding.backButton
        backButton.setOnClickListener{
            finish()
        }

        buildRecylcerView()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        buildRecylcerView()
    }

    private fun buildRecylcerView(){
        rvDocSearch = binding.rvDocSearch
        rvDocSearch.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        docAdapter = DocAdapter(searchDoc(intent.getStringExtra("query").toString()), this)
        rvDocSearch.adapter = docAdapter
    }

    override fun onDocClicked(pos: Int) {
        var intent = Intent(this, DocContactDetail::class.java)
        intent.putExtra("doctorcontact", DoctorContact.allDoctorCon.get(pos))
        startActivity(intent)
    }

    override fun onDocChatClicked(pos: Int) {
        var intent = Intent(this, ChatPage::class.java)
        var chtroom: MsgRoom? = MsgRoom.get_msgroom_patientid_doctorid(User.curr.uid.toString(), DoctorContact.allDoctorCon[pos].doctorid.toString())
        if (chtroom==null) {
            var db = Firebase.firestore
            val addDoc = hashMapOf(
                "doctorid" to DoctorContact.allDoctorCon[pos].doctorid.toString(),
                "patientid" to User.curr.uid,
                "lastmsg" to "",
                "lasttime" to Timestamp.now()
            )
            db.collection("chatrooms")
                .add(addDoc)
                .addOnSuccessListener { ref ->
                    MsgRoom.allMsgRoom.clear()
                    MsgRoom.fetch_all_msgrooms()
                    intent.putExtra("doctorid", DoctorContact.allDoctorCon[pos].doctorid.toString())
                    intent.putExtra("doctorname", DoctorContact.allDoctorCon[pos].name.toString())
                    intent.putExtra("patientid", User.curr.uid)
                    intent.putExtra("lasttime", Timestamp.now())
                    intent.putExtra("lastmsg", "")
                    Log.i("ADD_MSGROOM_DB", "Success")
                    Log.i("ROOM_CODE",ref.id)
                    intent.putExtra("chatroomid", ref.id)
                    Log.i("CLICK_DOC_MSG_ROOM", chtroom?.chatroomid.toString())
                    Log.i("CURR_USER", User.curr.uid.toString())
                    startActivity(intent)
                }
                .addOnFailureListener { Log.i("ADD_MSGROOM_DB", "Failed") }

        } else {
            Log.i("ROOM_AVAIL", chtroom!!.chatroomid.toString())
//            Msg.fetch_curr_msg(chtroom!!.chatroomid.toString())
            intent.putExtra("doctorid", chtroom!!.doctorid)
            intent.putExtra("patientid", chtroom!!.patientid)
            intent.putExtra("lasttime", chtroom!!.lasttime)
            intent.putExtra("lastmsg", chtroom!!.lastmsg)
            Log.i("ROOM_CODE", chtroom!!.chatroomid.toString())
            intent.putExtra("chatroomid", chtroom!!.chatroomid)
            Log.i("CLICK_DOC_MSG_ROOM", chtroom?.chatroomid.toString())
            Log.i("CURR_USER", User.curr.uid.toString())
            startActivity(intent)
        }
    }

    override fun onDocAppointmentClicked(pos: Int) {
        var intent = Intent(this, AppointmentsAdd::class.java)
        intent.putExtra("docconid", DoctorContact.allDoctorCon[pos].documentid)
        intent.putExtra("doctorid", DoctorContact.allDoctorCon[pos].doctorid)
        startActivity(intent)
        finish()
    }

    fun searchDoc(query: String): MutableList<DoctorContact>{
        var docs: MutableList<DoctorContact> = mutableListOf()
        for (doc in DoctorContact.allDoctorCon){
            if (doc.email!!.contains(query, ignoreCase = true) || doc.name!!.contains(query, ignoreCase = true) || doc.phone!!.contains(query, ignoreCase = true))
                docs.add(doc)
        }
        return docs
    }

}