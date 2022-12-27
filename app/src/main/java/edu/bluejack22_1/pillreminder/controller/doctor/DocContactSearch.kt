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
import edu.bluejack22_1.pillreminder.databinding.ActivityDocContactSearchBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact
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
//        var intent = Intent(this, ChatPage::class.java)
//        var chtroom: MsgRoom? = MsgRoom.get_msgroom_patientid_doctorid(User.curr.uid.toString(), DoctorContact.allDoctorCon[pos].doctorid.toString())
//        if (chtroom==null) {
//            MsgRoom.insert_msgroom(DoctorContact.allDoctorCon[pos].doctorid.toString())
//        }
//        chtroom = MsgRoom.get_msgroom_patientid_doctorid(User.curr.uid.toString(), DoctorContact.allDoctorCon[pos].doctorid.toString())
//        Log.i("CLICK_DOC_MSG_ROOM", chtroom.toString())
//        intent.putExtra("chatroom", chtroom)
//        startActivity(intent)
    }

    override fun onDocAppointmentClicked(pos: Int) {
        TODO("Not yet implemented")
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