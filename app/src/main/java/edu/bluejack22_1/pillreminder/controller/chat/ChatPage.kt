package edu.bluejack22_1.pillreminder.controller.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import edu.bluejack22_1.pillreminder.adapter.MsgAdapter
import edu.bluejack22_1.pillreminder.controller.doctor.DocContactDetail
import edu.bluejack22_1.pillreminder.databinding.ActivityChatPageBinding
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.Msg
import edu.bluejack22_1.pillreminder.model.MsgRoom
import edu.bluejack22_1.pillreminder.model.User
import java.util.*

class ChatPage : AppCompatActivity(), MsgAdapter.MsgListener {

   private lateinit var binding: ActivityChatPageBinding
   private lateinit var msgAdapter: MsgAdapter
   private var msgs: MutableList<Msg> = mutableListOf()
   private lateinit var db: FirebaseFirestore
   private lateinit var storage: FirebaseStorage
   private lateinit var opponent: User
   private lateinit var chatroom: MsgRoom
   private val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    private lateinit var sendImgLink: String
   private var sendImg = false
   private lateinit var chatPageBack: ImageView
   private lateinit var chatPageName: TextView
   private lateinit var chatPagePhoto: ShapeableImageView
   private lateinit var rvChatPage: RecyclerView
   private lateinit var chatPageTxt: TextInputEditText
   private lateinit var chatPageAttach: ImageButton
   private lateinit var chatPageSend: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Firebase.firestore
        storage = Firebase.storage
        binding = ActivityChatPageBinding.inflate(layoutInflater)
        val chatroomid = intent.getStringExtra("chatroomid").toString()
        val doctorid = intent.getStringExtra("doctorid").toString()
        val doctorname = intent.getStringExtra("doctorname").toString()
        val patientid = intent.getStringExtra("patientid").toString()
        val lasttime = intent.getParcelableExtra<Timestamp>("lasttime")
        val lastmsg = intent.getStringExtra("lastmsg").toString()
        chatroom = MsgRoom(chatroomid, doctorid, patientid, lasttime, lastmsg)
        opponent = if (chatroom.doctorid == User.curr.uid) User.get_user_uid(chatroom.patientid)!! else User.get_user_uid(chatroom.doctorid)!!
        chatPageBack = binding.chatPageBack
        chatPageBack.setOnClickListener{
            finish()
        }
        chatPageName = binding.chatPageName
        chatPageName.text = if (chatroom.patientid == User.curr.uid) doctorname else opponent.name
        chatPageName.setOnClickListener {
            goProfile(doctorid)
        }
        chatPagePhoto = binding.chatPagePhoto
        Picasso.get().load(opponent.photo).into(chatPagePhoto)
        chatPagePhoto.setOnClickListener{
            goProfile(doctorid)
        }
        rvChatPage = binding.rvChatPage
        chatPageTxt = binding.chatPageTxt
        chatPageAttach = binding.chatPageAttach
        chatPageAttach.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent,25)
        }

        chatPageSend = binding.chatPageSend
        chatPageSend.setOnClickListener{
            if (!sendImg) {
                val ctn = chatPageTxt.text.toString()
                if (!ctn.isNullOrEmpty()) {
                    Msg.insert_msg(chatroom.chatroomid.toString(), ctn, "text")
                }
            } else {
                Msg.insert_msg(chatroom.chatroomid.toString(), sendImgLink, "photo")
                sendImgLink = ""
            }
            sendImg = false
            chatPageTxt.isEnabled = true
            chatPageTxt.text?.clear()
        }

        val ssListener = db.collection("chatmessages")
            .whereEqualTo("chatroomid", chatroomid)
            .orderBy("sent")
            .addSnapshotListener { ss, e ->
                if (e!=null) Log.i("CHAT_SS_ERROR", e.message.toString())
                else if (ss!=null && !ss.isEmpty) {
                    val newItems = ss.toObjects(Msg::class.java)
                    msgs.clear()
                    msgs.addAll(newItems)
                    msgAdapter.notifyDataSetChanged()
                    if (::msgAdapter.isInitialized) layoutManager.scrollToPosition(msgAdapter.itemCount-1)
                }
            }

        buildRecyclerView()
        setContentView(binding.root)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==25) {
            if (data!=null) {
                if (data.data!=null) {
                    val selectImg = data.data
                    val calendar = Calendar.getInstance()
                    val ref = storage.reference.child("chats").child(calendar.timeInMillis.toString()+"")
                    ref.putFile(selectImg!!).addOnCompleteListener{ task ->
                         if (task.isSuccessful) {
                            ref.downloadUrl.addOnSuccessListener { uri ->
                                sendImgLink = uri.toString()
                                Log.i("UPLOAD_CHAT_IMG", sendImgLink)
                                chatPageTxt.text = Editable.Factory.getInstance().newEditable(sendImgLink)
                                chatPageTxt.isEnabled = false
                                sendImg = true
                                data.data = null
                            }
                        }
                    }
                }
            }
        }
    }

    private fun buildRecyclerView(){
        rvChatPage = binding.rvChatPage

        rvChatPage.layoutManager = layoutManager
        msgAdapter = MsgAdapter(msgs, this)
        layoutManager.scrollToPosition(msgAdapter.itemCount-1)
//        if (Msg.currMsg.isNullOrEmpty()) Log.i("CHAT_RV", "is null")
//        else Log.i("CHAT_RV", Msg.currMsg[0].documentid.toString())
        rvChatPage.adapter = msgAdapter
    }

    private fun goProfile(doctorid: String){
        var intent = Intent(this, DocContactDetail::class.java)
        intent.putExtra("doctorcontact", DoctorContact.get_doctorcontacts_doctorid(doctorid))
        startActivity(intent)
    }

    override fun orMsgClicked(pos: Int) {
        TODO("Not yet implemented")
    }

//    fun init_msgs(){
//        adapter = MsgAdapter(this@ChatPage, msgs)
//        rvChatPage.layoutManager = LinearLayoutManager(this@ChatPage)
//        db.collection("chatmessages").whereEqualTo("chatroomid", chatroom.chatroomid).addSnapshotListener{docs, e ->
//            if (e!=null) Log.e("LISTEN_MSG", e.toString())
//            else {
//                val tempList = mutableListOf<Msg>()
//                for (doc in docs!!) {
//                    val temp =
//                        Msg(
//                            doc.id,
//                            doc.data.get("chatroomid").toString(),
//                            doc.data.get("ctn").toString(),
//                            doc.data.get("sender").toString(),
//                            doc.data.get("sent") as Timestamp,
//                            doc.data.get("type").toString()
//                        )
//                    val tempIndex = tempList.indexOf(temp)
//                    if (tempIndex<0 || tempIndex > tempList.size) {
//                        tempList.add(temp)
//                        Log.i("GET_MSG", "Document: " + temp.documentid + " Sender: " + temp.sender + " Msg: " + temp.ctn)
//                    }
//                }
//                msgs = tempList
//                adapter.notifyDataSetChanged()
//            }
//        }
//    }
}