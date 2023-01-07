package edu.bluejack22_1.pillreminder.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import edu.bluejack22_1.pillreminder.controller.main.Messages
import java.io.Serializable

class MsgRoom: Serializable {
    companion object{
        var db = Firebase.firestore
        var auth = Firebase.auth

        var allMsgRoom: MutableList<MsgRoom> = mutableListOf()

        fun fetch_all_msgrooms(){

            if (User.curr.role.equals("doctors")) {
                db.collection("chatrooms")
                .whereEqualTo("doctorid", auth.currentUser!!.uid)
                .orderBy("lasttime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { docs ->
                    allMsgRoom.clear()
                    for (doc in docs) {
                        val temp =
                            if (doc.data.get("lasttime")!=null && doc.data.get("lastmsg")!=null) {
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
                                    Timestamp.now(),
                                    ""
                                )
                            }
                        val tempIndex = allMsgRoom.indexOf(temp)
                        if (tempIndex<0 || tempIndex > allMsgRoom.size) {
                            allMsgRoom.add(temp)
//                            Log.i("GET_MSGROOMS", "Document: " + temp.chatroomid + " Doctor: " + temp.doctorid + " Patient: " + temp.patientid)
//                            Log.i("ADD_MSGROOM", "Document: " + allMsgRoom[allMsgRoom.indexOf(temp)].chatroomid + " Doctor: " + allMsgRoom[allMsgRoom.indexOf(temp)].doctorid + " Patient: " + allMsgRoom[allMsgRoom.indexOf(temp)].patientid)
                        }

                    }
                    if (Messages.adapterInitialized()) Messages.msgRoomAdapter.notifyDataSetChanged()
                }
                .addOnFailureListener { e ->
//                    Log.e("GET_MSGROOMS", e.toString())
                }
            } else {
                db.collection("chatrooms")
                .whereEqualTo("patientid", auth.currentUser!!.uid)
                .orderBy("lasttime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { docs ->
                    allMsgRoom.clear()
                    for (doc in docs) {
                        val temp =
                            if (doc.data.get("lasttime")!=null && doc.data.get("lastmsg")!=null) {
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
//                            Log.i("GET_MSGROOMS", "Document: " + temp.chatroomid + " Doctor: " + temp.doctorid + " Patient: " + temp.patientid)
//                            Log.i("ADD_MSGROOM", "Document: " + allMsgRoom[allMsgRoom.indexOf(temp)].chatroomid + " Doctor: " + allMsgRoom[allMsgRoom.indexOf(temp)].doctorid + " Patient: " + allMsgRoom[allMsgRoom.indexOf(temp)].patientid)
                        }

                    }
                    if (Messages.adapterInitialized()) Messages.msgRoomAdapter.notifyDataSetChanged()
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

        fun search_msgroom(query: String): MutableList<MsgRoom>{
            val result = mutableListOf<MsgRoom>()
            for (room in allMsgRoom){
                if (room.lastmsg!!.contains(query, true)) result.add(room)
                else {
                    if (User.curr.role.equals("doctors")) {
                        if (User.get_user_uid(room.patientid)!!.name!!.contains(query, true)) result.add(room)
                    } else {
                        if (DoctorContact.get_doctorcontacts_doctorid(room.doctorid!!)!!.name!!.contains(query, true)) result.add(room)
                    }
                }
            }
            return result
        }

//        fun insert_msgroom(doctorid: String): Boolean{
//            var cek = false
//            val addDoc = hashMapOf(
//                "doctorid" to doctorid,
//                "patientid" to User.curr.uid,
//                "lastmsg" to "",
//                "lasttime" to Timestamp.now()
//            )
//            db.collection("chatrooms").add(addDoc).addOnSuccessListener { cek = true; Log.i("ADD_MSGROOM_DB", "Success") }.addOnFailureListener { cek = false; Log.i("ADD_MSGROOM_DB", "Failed") }
//            Log.i("ADD_MSGROOM_DB", "doctorid = "+addDoc["doctorid"] + " patientid = "+ addDoc["patientid"] )
//            return cek
//        }

        fun update_msgroom(chatroomid: String, lasttime: Timestamp, lastmsg: String): Boolean{
            var cek = false
            db.collection("chatrooms").document(chatroomid).update("lasttime", lasttime, "lastmsg", lastmsg).addOnSuccessListener { fetch_all_msgrooms() }.addOnFailureListener { cek = false }
            return cek
        }
    }

    var chatroomid: String? = null
    var doctorid: String? = null
    var patientid: String? = null
    var lasttime: Timestamp? = null
    var lastmsg: String? = null

    constructor()
    constructor(chatroomid: String, doctorid: String, patientid: String, lasttime: Timestamp?, lastmsg: String) {
        this.chatroomid = chatroomid
        this.doctorid = doctorid
        this.patientid = patientid
        this.lasttime = lasttime
        this.lastmsg = lastmsg
    }
}