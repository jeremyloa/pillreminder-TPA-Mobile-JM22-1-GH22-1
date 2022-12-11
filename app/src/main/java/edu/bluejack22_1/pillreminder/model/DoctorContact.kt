package edu.bluejack22_1.pillreminder.model

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DoctorContact (val documentid:String?, val doctorid:String?, val patientid:String?, var name:String?, val email:String?, var phone:String?, var photo:String?){
    companion object {
        var db = Firebase.firestore
        var allDoctorCon: MutableList<DoctorContact> = mutableListOf()

        fun fetch_all_doctorcontacts_patientid(){
            allDoctorCon.clear()
            db.collection("doctorcontacts")
            .whereEqualTo("patientid", User.curr.uid)
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val temp =
                        DoctorContact(
                            doc.id,
                            doc.data.get("doctorid").toString(),
                            doc.data.get("patientid").toString(),
                            doc.data.get("name").toString(),
                            doc.data.get("email").toString(),
                            doc.data.get("phone").toString(),
                            doc.data.get("photo").toString()
                        )
                    val tempIndex = allDoctorCon.indexOf(temp)
                    if (tempIndex<0 || tempIndex > allDoctorCon.size) {
                        allDoctorCon.add(temp)
                        Log.i("GET_DOCTORCONTACTS", "Document: " + temp.documentid.toString() + " Doctor: " + temp.doctorid.toString())
                    }

                }
            }
            .addOnFailureListener { e ->
                Log.e("GET_DOCTORCONTACTS", e.toString())
            }
        }

        fun insert_doctorcontacts_patientid(name: String, phone: String, email: String, doctorid: String, photo: String): Boolean{
            var cek = false
            val addDoc = hashMapOf(
                "doctorid" to doctorid,
                "email" to email,
                "name" to name,
                "patientid" to User.curr.uid,
                "phone" to phone,
                "photo" to photo
            )
            db.collection("doctorcontacts").add(addDoc).addOnSuccessListener { cek = true }.addOnFailureListener { cek = false }
            if (cek) fetch_all_doctorcontacts_patientid()
            return cek
        }

        fun update_doctorcontacts_documentid(documentid: String, name: String, phone: String): Boolean{
            var cek = false
            db.collection("doctorcontacts").document(documentid).update("name", name, "phone", phone).addOnSuccessListener { cek = true }.addOnFailureListener { cek = false }
            if (cek) fetch_all_doctorcontacts_patientid()
            return cek
        }

        fun delete_doctorcontacts_documentid(documentid: String): Boolean{
            var cek = false
            db.collection("doctorcontacts").document(documentid).delete().addOnSuccessListener { cek = true }.addOnFailureListener { cek = false }
            if (cek) fetch_all_doctorcontacts_patientid()
            return cek
        }
    }
}