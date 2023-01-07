package edu.bluejack22_1.pillreminder.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar
import java.util.Date

class Treatment (val documentid:String?, val patientid:String, val name:String, val dose:Double, val unit:String, val frequency:Int, val nextoccurrence:Timestamp, val startdate:Timestamp, val enddate:Timestamp, val remindhour:Int, val remindmin:Int) {
    companion object{
        var db = Firebase.firestore
        var allTreatments: MutableList<Treatment> = mutableListOf()

        fun fetch_all_treatments(){
            allTreatments.clear()
            db.collection("treatments")
                .orderBy("nextoccurrence", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { trts ->
                    for (trt in trts) {
                        val tempFreq = trt.data.get("frequency") as Long
                        val tempHour = trt.data.get("remindhour") as Long
                        val tempMin = trt.data.get("remindmin") as Long
                        val temp =
                            Treatment(
                                trt.id,
                                trt.data.get("patientid").toString(),
                                trt.data.get("name").toString(),
                                trt.data.get("dose") as Double,
                                trt.data.get("unit").toString(),
                                tempFreq.toInt(),
                                trt.data.get("nextoccurrence") as Timestamp,
                                trt.data.get("startdate") as Timestamp,
                                trt.data.get("enddate") as Timestamp,
                                tempHour.toInt(),
                                tempMin.toInt()
                            )
                        val tempIndex = allTreatments.indexOf(temp)
                        if (tempIndex<0 || tempIndex> allTreatments.size) {
                            allTreatments.add(temp)
//                            Log.i("GET_TREATMENTS", "Document: " + temp.name + " Name: " + temp.name)
                        }
                    }
                    Timeline.fetch_all_timeline()
                }
                .addOnFailureListener { e -> Log.e("GET_TREATMENTS", e.toString()) }
        }

        fun get_treatments_patientid(patientid: String): MutableList<Treatment>{
            val trts: MutableList<Treatment> = mutableListOf()
            for (trt in allTreatments) {
                if (trt.patientid.equals(patientid)) trts.add(trt)
            }
            return trts
        }

        fun get_treatments_documentid(documentid: String):Treatment?{
            for (trt in allTreatments) {
                if (trt.documentid.equals(documentid)) return trt
            }
            return null
        }

        fun insert_treatment(trt: Treatment){
            val addDoc = hashMapOf(
                "patientid" to trt.patientid,
                "name" to trt.name,
                "dose" to trt.dose,
                "unit" to trt.unit,
                "frequency" to trt.frequency,
                "nextoccurrence" to trt.nextoccurrence,
                "startdate" to trt.startdate,
                "enddate" to trt.enddate,
                "remindhour" to trt.remindhour,
                "remindmin" to trt.remindmin
            )
            db.collection("treatments").add(addDoc).addOnSuccessListener {
//                Log.i("INSERT_TREATMENT", "Patientid: " + trt.patientid + " Name: " + trt.name)
            }.addOnFailureListener{ e ->
//                Log.e("INSERT_TREATMENT", e.toString())
            }
        }

        fun update_treatment(documentid: String, name:String, dose:Double, unit:String, frequency:Int, nextoccurrence:Timestamp, startdate:Timestamp, enddate:Timestamp, remindhour:Int, remindmin:Int){
            val newoccurrence = count_next_occurrence(frequency, nextoccurrence, enddate, remindhour, remindmin)
            db.collection("treatments").document(documentid).update("name", name,  "dose", dose, "unit", unit, "frequency", frequency, "nextoccurrence", newoccurrence, "startdate", startdate, "enddate", enddate, "remindhour", remindhour, "remindmin", remindmin).addOnSuccessListener {
//                Log.i("UPDATE_TREATMENT", "Document: " + documentid + " Name: " + name)
            }.addOnFailureListener{ e ->
//                Log.e("UPDATE_TREATMENT", e.toString())
            }
        }

        fun update_occurrence(documentid: String, frequency: Int, nextoccurrence: Timestamp, enddate: Timestamp, remindhour: Int, remindmin: Int){
            val newoccurrence = count_next_occurrence(frequency, nextoccurrence, enddate, remindhour, remindmin)
//            var temp: Treatment? = null
            if (nextoccurrence!=newoccurrence) {
                db.collection("treatments").document(documentid)
                    .update("nextoccurrence", newoccurrence, )
                    .addOnSuccessListener {
//                        Log.i("UPDATE_TREATMENT_OCCURRENCE", "Document: " + documentid)
                        fetch_all_treatments()
//                        db.collection("treatments").document(documentid).get().addOnSuccessListener { trt ->
//                            val tempFreq = trt.data!!.get("frequency") as Long
//                            val tempHour = trt.data!!.get("remindhour") as Long
//                            val tempMin = trt.data!!.get("remindmin") as Long
//                            temp =
//                            Treatment(
//                                trt.id,
//                                trt.data!!.get("patientid").toString(),
//                                trt.data!!.get("name").toString(),
//                                trt.data!!.get("dose") as Double,
//                                trt.data!!.get("unit").toString(),
//                                tempFreq.toInt(),
//                                trt.data!!.get("nextoccurrence") as Timestamp,
//                                trt.data!!.get("startdate") as Timestamp,
//                                trt.data!!.get("enddate") as Timestamp,
//                                tempHour.toInt(),
//                                tempMin.toInt()
//                            )
//                        }
                    }
                    .addOnFailureListener{ e -> Log.e("UPDATE_TREATMENT_OCCURRENCE", e.toString())}
            }
//            return temp
        }

//        fun update_all_occurrence(){
//            var cek = false
//            for (trt in allTreatments) {
//                if (trt.nextoccurrence.toDate().before(Timestamp.now().toDate()))  {
//                    update_occurrence(trt.documentid.toString(), trt.frequency, trt.nextoccurrence, trt.enddate, trt.remindhour, trt.remindmin)
//                    cek = true
//                }
//            }
//            if (cek) fetch_all_treatments()
//        }

        fun delete_treatment(documentid: String){
            db.collection("treatments").document(documentid).delete().addOnSuccessListener {
//                Log.i("DELETE_TREATMENT", "Document: " + documentid)
            }.addOnFailureListener{ e ->
//                Log.e("DELETE_TREATMENT", e.toString())
            }
        }

        fun count_next_occurrence(frequency: Int, nextoccurrence: Timestamp, enddate: Timestamp, remindhour: Int, remindmin: Int): Timestamp {
            var ts = nextoccurrence
            val origin = ts.toDate()
            lateinit var next: Date

            val calendar = Calendar.getInstance()
            calendar.time = origin

            //if the next occurrence (unedited) is still after current time, let it be
            if (nextoccurrence.toDate().after(Timestamp.now().toDate())) return nextoccurrence
            //if the next occurrence is past, set new occurrence
            if (frequency==30){
                calendar.add(Calendar.DATE, 30)
                calendar.set(Calendar.HOUR, remindhour)
                calendar.set(Calendar.MINUTE, remindmin)
                next = calendar.time
            } else if (frequency==7) {
                calendar.add(Calendar.DATE, 7)
                calendar.set(Calendar.HOUR, remindhour)
                calendar.set(Calendar.MINUTE, remindmin)
                next = calendar.time
            } else {
                calendar.add(Calendar.DATE, 1)
                calendar.set(Calendar.HOUR, remindhour)
                calendar.set(Calendar.MINUTE, remindmin)
                next = calendar.time
            }
            ts = Timestamp(next)

            if (ts.toDate().after(enddate.toDate())) return nextoccurrence
            return ts
        }

        fun get_afternow(): Int {
           val trt = get_treatments_patientid(User.curr.uid.toString())
           for (tr in trt) if (tr.nextoccurrence.toDate().after(Timestamp.now().toDate())) return trt.indexOf(tr)
           return 0
        }
    }
}