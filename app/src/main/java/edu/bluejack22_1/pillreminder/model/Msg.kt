package edu.bluejack22_1.pillreminder.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.pillreminder.controller.chat.ChatPage
import java.io.Serializable

class Msg: Serializable {
    companion object{
        var db = Firebase.firestore
//        var currMsg: MutableList<Msg> = mutableListOf()
//
//        fun fetch_curr_msg(chatroomid: String){
//            currMsg.clear()
//            db.collection("chatmessages")
//            .whereEqualTo("chatroomid", chatroomid)
//            .orderBy("sent")
//            .get()
//            .addOnSuccessListener { docs ->
//                for (doc in docs) {
//                    val temp =
//                        Msg(
//                            doc.id,
//                            doc.data.get("chatroomid").toString(),
//                            doc.data.get("ctn").toString(),
//                            doc.data.get("sender").toString(),
//                            doc.data.get("sent") as Timestamp,
//                            doc.data.get("type").toString()
//                        )
//                    val tempIndex = currMsg.indexOf(temp)
//                    if (tempIndex<0 || tempIndex > currMsg.size) {
//                        currMsg.add(temp)
//                        Log.i("GET_MSGS", "Document: " + temp.documentid + " Sender: " + temp.sender + " Msg: " + temp.ctn)
//                    }
//
//                }
//                ChatPage.msgAdapter.notifyDataSetChanged()
//            }
//            .addOnFailureListener { e ->
//                Log.e("GET_MSGS", e.toString())
//            }
//        }

        fun insert_msg(chatroomid:String, ctn:String, type:String): Boolean{
            var cek = false
            val addDoc = hashMapOf(
                "chatroomid" to chatroomid,
                "ctn" to ctn,
                "sender" to User.curr.uid,
                "sent" to Timestamp.now(),
                "type" to type
            )
            db.collection("chatmessages").add(addDoc)
                .addOnSuccessListener {
                    MsgRoom.update_msgroom(chatroomid, Timestamp.now(), ctn)
//                    fetch_curr_msg(chatroomid)
                }
                .addOnFailureListener { cek = false }
            return cek
        }
//
    }

    var documentid:String? = null
    var chatroomid:String? = null
    var ctn:String? = null
    var sender:String? = null
    var sent:Timestamp? = null
    var type:String? = null

    constructor()
    constructor(documentid:String, chatroomid:String, ctn:String, sender:String, sent:Timestamp, type:String) {
        this.documentid = documentid
        this.chatroomid = chatroomid
        this.ctn = ctn
        this.sender = sender
        this.sent = sent
        this.type = type
    }
}