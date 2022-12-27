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
import edu.bluejack22_1.pillreminder.adapter.DocAdapter
import edu.bluejack22_1.pillreminder.controller.chat.ChatPage
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactMainBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.MsgRoom
import edu.bluejack22_1.pillreminder.model.User

class DocContactMain : AppCompatActivity(), DocAdapter.DocClickListener {
    private lateinit var binding: ActivityDocContactMainBinding
    private lateinit var rvDoc: RecyclerView
    private lateinit var docAdapter: DocAdapter

    private lateinit var backButton: ImageView
    private lateinit var toAddDoc: ImageView

    private lateinit var searchDocText: TextInputEditText
    private lateinit var searchDocBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDocContactMainBinding.inflate(layoutInflater)
        searchDocText = binding.searchDocText
        searchDocBtn = binding.searchDocBtn
        searchDocBtn.setOnClickListener {
            if (!searchDocText.toString().isNullOrEmpty()) {
                var intent = Intent(this, DocContactSearch::class.java)
                intent.putExtra("query", searchDocText.text.toString())
                startActivity(intent)
            }
        }

        backButton = binding.backButton
        backButton.setOnClickListener{
            finish()
        }

        toAddDoc = binding.toAddDoc
        toAddDoc.setOnClickListener{
            startActivity(Intent(this, DocContactAdd::class.java))
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
        rvDoc = binding.rvDoc
        rvDoc.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        docAdapter = DocAdapter(DoctorContact.allDoctorCon, this)
        rvDoc.adapter = docAdapter
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
            MsgRoom.insert_msgroom(DoctorContact.allDoctorCon[pos].doctorid.toString())
        }
        chtroom = MsgRoom.get_msgroom_patientid_doctorid(User.curr.uid.toString(), DoctorContact.allDoctorCon[pos].doctorid.toString())
        Log.i("CLICK_DOC_MSG_ROOM", chtroom!!.chatroomid)
        Log.i("CURR_USER", User.curr.uid.toString())
        intent.putExtra("msgroom", chtroom)
        startActivity(intent)
    }

    override fun onDocAppointmentClicked(pos: Int) {
//        TODO("Not yet implemented")
    }

}