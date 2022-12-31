package edu.bluejack22_1.pillreminder.model

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class Timeline (val documentid: String?, val timelinetype: Int, val datetimestamp: Timestamp){
    companion object{
        var auth = Firebase.auth
        var allTimeline: MutableList<Timeline> = mutableListOf()
        fun fetch_all_timeline(){
            allTimeline.clear()
            //if (nextoccurrence.toDate().after(Timestamp.now().toDate()))
            for (trt in Treatment.allTreatments) if (trt.patientid.equals(auth.currentUser!!.uid)) allTimeline.add(Timeline(trt.documentid, 1, trt.nextoccurrence))
            for (apt in Appointment.allAppointments) if (apt.patientid.equals(auth.currentUser!!.uid) || apt.doctorid.equals(User.curr.uid)) allTimeline.add(Timeline(apt.documentid, 2, apt.datetime))
            allTimeline.sortBy { it.datetimestamp }
            for (tl in allTimeline)
                Log.i("TIMELINE", "Doc: " + tl.documentid + " Type: " + tl.timelinetype + " Stamp: " + tl.datetimestamp.toDate().toString())
            Log.i("TIMELINE_GET", "Size: "+ allTimeline.size + " Treatment Size: " + Treatment.allTreatments.size + " Appointment Size: " + Appointment.allAppointments.size)
        }

        fun get_afternow(): Int {
           for (tl in allTimeline) if (tl.datetimestamp.toDate().after(Timestamp.now().toDate())) return allTimeline.indexOf(tl)
           return 0
        }

        fun get_timeline_afternow_asc(): MutableList<Timeline> {
            fetch_all_timeline()
            var tls: MutableList<Timeline> = mutableListOf()
            for (tl in allTimeline) if (tl.datetimestamp.toDate().after(Timestamp.now().toDate())) tls.add(tl)
            tls.sortBy { it.datetimestamp }
            return tls
        }

    }
}