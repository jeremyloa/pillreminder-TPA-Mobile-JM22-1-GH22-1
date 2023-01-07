package edu.bluejack22_1.pillreminder.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Appointment (val documentid:String?, val docconid:String?, val doctorid:String?, val patientid:String?, val datetime:Timestamp, val place:String?, val address:String?, val note:String?){
    companion object{
        var db = Firebase.firestore
        var allAppointments: MutableList<Appointment> = mutableListOf()

        fun fetch_all_appointments(){
            allAppointments.clear()
            db.collection("appointments")
                .orderBy("datetime", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { apts ->
                    for (apt in apts) {
                        val temp =
                            Appointment(
                                apt.id,
                                apt.data.get("docconid").toString(),
                                apt.data.get("doctorid").toString(),
                                apt.data.get("patientid").toString(),
                                apt.data.get("datetime") as Timestamp,
                                apt.data.get("place").toString(),
                                apt.data.get("address").toString(),
                                apt.data.get("note").toString()
                            )
                        val tempIndex = allAppointments.indexOf(temp)
                        if (tempIndex<0 || tempIndex> allAppointments.size) {
                            allAppointments.add(temp)
//                            Log.i("GET_APPOINTMENTS", "Document: " + temp.documentid + " Place: " + temp.place.toString())
                        }
                    }
                    Timeline.fetch_all_timeline()
                }
                .addOnFailureListener{ e ->
//                    Log.e("GET_APPOINTMENTS", e.toString())
                }
        }

        fun get_appointments_patientid(patientid: String?): MutableList<Appointment>{
            val apts: MutableList<Appointment> = mutableListOf()
            for (apt in allAppointments) {
                if (apt.patientid.equals(patientid)) apts.add(apt)
            }
            return apts
        }

        fun get_appointments_docconid(docconid: String?): MutableList<Appointment>{
            val apts: MutableList<Appointment> = mutableListOf()
            for (apt in allAppointments) {
                if (apt.docconid.equals(docconid)) apts.add(apt)
            }
            return apts
        }

        fun get_appointments_docconid_patientid(docconid: String?, patientid: String?): MutableList<Appointment>{
            val apts: MutableList<Appointment> = mutableListOf()
            for (apt in allAppointments) {
                if (apt.docconid.equals(docconid) && apt.patientid.equals(patientid)) apts.add(apt)
            }
            return apts
        }

        fun get_appointments_doctorid(doctorid: String?): MutableList<Appointment>{
            val apts: MutableList<Appointment> = mutableListOf()
            for (apt in allAppointments) {
                if (apt.doctorid.equals(doctorid)) apts.add(apt)
            }
            return apts
        }

        fun get_appointments_documentid(documentid: String?): Appointment?{
            for (apt in allAppointments) {
                if (apt.documentid.equals(documentid)) return apt
            }
            return null
        }

        fun insert_appointment(apt: Appointment){
            val addDoc = hashMapOf(
                "docconid" to apt.docconid,
                "doctorid" to apt.doctorid,
                "patientid" to apt.patientid,
                "datetime" to apt.datetime,
                "place" to apt.place,
                "address" to apt.address,
                "note" to apt.note
            )
            db.collection("appointments").add(addDoc).addOnSuccessListener {
//                Log.i("INSERT_APPOINTMENT", "Docconid: " + apt.docconid + " Place: " + apt.place.toString())
            }.addOnFailureListener{ e ->
//                Log.e("INSERT_APPOINTMENT", e.toString())
            }
        }

        fun update_appointment(documentid: String, datetime: Timestamp, place: String, address: String, note: String){
            db.collection("appointments").document(documentid).update("datetime", datetime,  "place", place, "address", address, "note", note).addOnSuccessListener {
//                Log.i("UPDATE_APPOINTMENT", "Document: " + documentid + " Place: " + place)
            }.addOnFailureListener{ e ->
//                Log.e("UPDATE_APPOINTMENT", e.toString())
            }
        }

        fun delete_appointment(documentid: String){
            db.collection("appointments").document(documentid).delete().addOnSuccessListener {
//                Log.i("DELETE_APPOINTMENT", "Document: " + documentid)
            }.addOnFailureListener{ e ->
//                Log.e("DELETE_APPOINTMENT", e.toString())
            }
        }

        fun get_afternow(): Int {
           val apt = get_appointments_doctorid(User.curr.uid)
           for (ap in apt) if (ap.datetime.toDate().after(Timestamp.now().toDate())) return apt.indexOf(ap)
           return 0
        }
    }
}