package edu.bluejack22_1.pillreminder.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class Appointment (val documentid:String?, val docconid:String?, val doctorid:String?, val patientid:String?, val datetime:Date, val place:String?, val address:String?, val note:String?){
    companion object{
        var db = Firebase.firestore
        var allAppointments: MutableList<Appointment> = mutableListOf()

        fun fetch_all_appointments(){
            allAppointments.clear()
            db.collection("appointments")
                .orderBy("datetime")
                .get()
                .addOnSuccessListener { apts ->
                    for (apt in apts) {
                        val temp =
                            Appointment(
                                apt.id,
                                apt.data.get("docconid").toString(),
                                apt.data.get("doctorid").toString(),
                                apt.data.get("patientid").toString(),
                                Date(apt.data.get("datetime").toString().toLong()),
                                apt.data.get("place").toString(),
                                apt.data.get("address").toString(),
                                apt.data.get("note").toString()
                            )
                        val tempIndex = allAppointments.indexOf(temp)
                        if (tempIndex<0 || tempIndex> allAppointments.size) {
                            allAppointments.add(temp)
                            Log.i("GET_APPOINTMENTS", "Document: " + temp.documentid + " Place: " + temp.place.toString())
                        }
                    }
                }
                .addOnFailureListener{ e ->
                    Log.e("GET_APPOINTMENTS", e.toString())
                }
        }

        fun get_appointments_patientid(patientid: String?): MutableList<Appointment>{
            var apts: MutableList<Appointment> = mutableListOf()
            for (apt in allAppointments) {
                if (apt.patientid.equals(patientid)) apts.add(apt)
            }
            return apts
        }

        fun get_appointments_docconid(docconid: String?): MutableList<Appointment>{
            var apts: MutableList<Appointment> = mutableListOf()
            for (apt in allAppointments) {
                if (apt.docconid.equals(docconid)) apts.add(apt)
            }
            return apts
        }

        fun get_appointments_docconid_patientid(docconid: String?, patientid: String?): MutableList<Appointment>{
            var apts: MutableList<Appointment> = mutableListOf()
            for (apt in allAppointments) {
                if (apt.docconid.equals(docconid) && apt.patientid.equals(patientid)) apts.add(apt)
            }
            return apts
        }

        fun get_appointments_doctorid(doctorid: String?): MutableList<Appointment>{
            var apts: MutableList<Appointment> = mutableListOf()
            for (apt in allAppointments) {
                if (apt.doctorid.equals(doctorid)) apts.add(apt)
            }
            return apts
        }

        fun insert_appointment(apt: Appointment){
            val addDoc = hashMapOf(
                "docconid" to apt.docconid,
                "doctorid" to apt.doctorid,
                "patientid" to apt.patientid,
                "datetime" to Timestamp(apt.datetime),
                "place" to apt.place,
                "address" to apt.address,
                "note" to apt.note
            )
            db.collection("appointments").add(addDoc).addOnSuccessListener { Log.i("INSERT_APPOINTMENT", "Docconid: " + apt.docconid + " Place: " + apt.place.toString()) }.addOnFailureListener{ e -> Log.e("INSERT_APPOINTMENT", e.toString())}
        }

        fun update_appointment(documentid: String, datetime: Date, place: String, address: String, note: String){
            db.collection("appointments").document(documentid).update("datetime", datetime,  "place", place, "address", address, "note", note).addOnSuccessListener { Log.i("UPDATE_APPOINTMENT", "Document: " + documentid + " Place: " + place) }.addOnFailureListener{ e -> Log.e("UPDATE_APPOINTMENT", e.toString())}
        }

        fun delete_appointment(documentid: String){
            db.collection("appointments").document(documentid).delete().addOnSuccessListener { Log.i("DELETE_APPOINTMENT", "Document: " + documentid) }.addOnFailureListener{ e -> Log.e("DELETE_APPOINTMENT", e.toString())}
        }
    }
}