package edu.bluejack22_1.pillreminder.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class MsgRoom (var chatroomid: String, var doctorid: String, var patientid: String, var lasttime: Timestamp?, var lastmsg: String?):
    Serializable {
    companion object{
        var db = Firebase.firestore
        var allMsgRoom: MutableList<MsgRoom> = mutableListOf()

        fun fetch_all_msgrooms(){
            allMsgRoom.clear()
            if (User.curr.role.equals("doctors")) {
                db.collection("chatrooms")
                .whereEqualTo("doctorid", User.curr.uid)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        val temp =
                            if (doc.data.get("lasttime")!=null && doc.data.get("lastmsg").toString()!=null) {
                                MsgRoom(
                                    doc.id,
                                    doc.data.get("doctorid").toString(),
                                    doc.data.get("patientid").toString(),
                                    doc.data.get("lasttime") as Timestamp,
                                    doc.data.get("lastmsg").toString(),
                                )
                            } else {
                                MsgRoom(
                                    doc.id,
                                    doc.data.get("doctorid").toString(),
                                    doc.data.get("patientid").toString(),
                                    Timestamp.now(), ""
                                )
                            }
                        val tempIndex = allMsgRoom.indexOf(temp)
                        if (tempIndex<0 || tempIndex > allMsgRoom.size) {
                            allMsgRoom.add(temp)
                            Log.i("GET_MSGROOMS", "Document: " + temp.chatroomid + " Doctor: " + temp.doctorid + " Patient: " + temp.patientid)
                        }

                    }
                }
                .addOnFailureListener { e ->
                    Log.e("GET_MSGROOMS", e.toString())
                }
            } else {
                db.collection("chatrooms")
                .whereEqualTo("patientid", User.curr.uid)
                .get()
                .addOnSuccessListener { docs ->
                    for (doc in docs) {
                        val temp =
                            if (doc.data.get("lasttime")!=null && doc.data.get("lastmsg").toString()!=null) {
                                MsgRoom(
                                    doc.id,
                                    doc.data.get("doctorid").toString(),
                                    doc.data.get("patientid").toString(),
                                    doc.data.get("lasttime") as Timestamp,
                                    doc.data.get("lastmsg").toString(),
                                )
                            } else {
                                MsgRoom(
                                    doc.id,
                                    doc.data.get("doctorid").toString(),
                                    doc.data.get("patientid").toString(),
                                    Timestamp.now(), ""
                                )
                            }
                        val tempIndex = allMsgRoom.indexOf(temp)
                        if (tempIndex<0 || tempIndex > allMsgRoom.size) {
                            allMsgRoom.add(temp)
                            Log.i("GET_MSGROOMS", "Document: " + temp.chatroomid + " Doctor: " + temp.doctorid + " Patient: " + temp.patientid)
                        }

                    }
                }
                .addOnFailureListener { e ->
                    Log.e("GET_MSGROOMS", e.toString())
                }
            }
        }

        fun get_msgroom_patientid_doctorid(patientid: String, doctorid: String): MsgRoom?{
            for (msgroom in allMsgRoom){
                if (msgroom.patientid == patientid && msgroom.doctorid == doctorid) return msgroom
            }
            return null
        }

        fun insert_msgroom(doctorid: String): Boolean{
            var cek = false
            val addDoc = hashMapOf(
                "doctorid" to doctorid,
                "patientid" to User.curr.uid
            )
            db.collection("chatrooms").add(addDoc).addOnSuccessListener { cek = true }.addOnFailureListener { cek = false }
            return cek
        }

        fun update_msgroom(chatroomid: String, lasttime: Timestamp, lastmsg: String): Boolean{
            var cek = false
            db.collection("chatrooms").document(chatroomid).update("lasttime", lasttime, "lastmsg", lastmsg).addOnSuccessListener { cek = true }.addOnFailureListener { cek = false }
            return cek
        }
    }
}