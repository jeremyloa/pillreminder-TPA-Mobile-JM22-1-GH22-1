package edu.bluejack22_1.pillreminder.controller.chat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
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
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.adapter.MsgAdapter
import edu.bluejack22_1.pillreminder.databinding.ActivityChatPageBinding
import edu.bluejack22_1.pillreminder.model.Msg
import edu.bluejack22_1.pillreminder.model.MsgRoom
import edu.bluejack22_1.pillreminder.model.User
import java.util.*

class ChatPage : AppCompatActivity() {

   private lateinit var binding: ActivityChatPageBinding
   private lateinit var adapter: MsgAdapter
   private lateinit var msgs: MutableList<Msg>
   private lateinit var db: FirebaseFirestore
   private lateinit var storage: FirebaseStorage
   private lateinit var opponent: User
   private lateinit var chatroom: MsgRoom

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
        chatroom = intent.getSerializableExtra("msgroom") as MsgRoom
        opponent = if (chatroom.doctorid == User.curr.uid) User.get_user_uid(chatroom.patientid)!! else User.get_user_uid(chatroom.doctorid)!!
        chatPageBack = binding.chatPageBack
        chatPageBack.setOnClickListener{
            finish()
        }
        chatPageName = binding.chatPageName
        chatPageName.text = opponent.name
        chatPagePhoto = binding.chatPagePhoto
        Picasso.get().load(opponent.photo).into(chatPagePhoto)
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
                Msg.insert_msg(chatroom.chatroomid, ctn, "text")
            } else {
                Msg.insert_msg(chatroom.chatroomid, sendImgLink, "photo")
                sendImgLink = ""
                sendImg = false
            }
        }

        init_msgs()
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
                                sendImg = true
                            }
                        }
                    }
                }
            }
        }
    }
    fun init_msgs(){
        adapter = MsgAdapter(this@ChatPage, msgs)
        rvChatPage.layoutManager = LinearLayoutManager(this@ChatPage)
        db.collection("chatmessages").whereEqualTo("chatroomid", chatroom.chatroomid).addSnapshotListener{docs, e ->
            if (e!=null) Log.e("LISTEN_MSG", e.toString())
            else {
                val tempList = mutableListOf<Msg>()
                for (doc in docs!!) {
                    val temp =
                        Msg(
                            doc.id,
                            doc.data.get("chatroomid").toString(),
                            doc.data.get("ctn").toString(),
                            doc.data.get("sender").toString(),
                            doc.data.get("sent") as Timestamp,
                            doc.data.get("type").toString()
                        )
                    val tempIndex = tempList.indexOf(temp)
                    if (tempIndex<0 || tempIndex > tempList.size) {
                        tempList.add(temp)
                        Log.i("GET_MSG", "Document: " + temp.documentid + " Sender: " + temp.sender + " Msg: " + temp.ctn)
                    }
                }
                msgs = tempList
                adapter.notifyDataSetChanged()
            }
        }
    }
}