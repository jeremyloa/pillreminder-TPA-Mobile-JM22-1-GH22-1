package edu.bluejack22_1.pillreminder.controller

import android.app.*
import android.app.Notification
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.model.Appointment
import edu.bluejack22_1.pillreminder.model.DoctorContact
import edu.bluejack22_1.pillreminder.model.Timeline
import edu.bluejack22_1.pillreminder.model.Treatment

class Notification : BroadcastReceiver() {

    companion object{
        val NOTIF_TRT = 1
        val NOTIF_APT = 2
        fun initialize_channels(ctx: Context){
            val channel1 = NotificationChannel(NOTIF_TRT.toString(), "Treatments", NotificationManager.IMPORTANCE_DEFAULT)
            val channel2 = NotificationChannel(NOTIF_APT.toString(), "Appointments", NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager = ctx.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
            notificationManager.createNotificationChannel(channel2)
        }

        val alarms:MutableList<AlarmData> = mutableListOf()
        fun schedule_notifications(ctx: Context){
            val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            for (al in alarms) alarmManager.cancel(al.pendingIntent)
            alarms.clear()

            val tls = Timeline.get_timeline_afternow_today_asc()
            for (tl in tls) {
                val intent = Intent(ctx, edu.bluejack22_1.pillreminder.controller.Notification::class.java)
                if (tl.timelinetype == NOTIF_TRT) {
                    intent.putExtra("notificationID", tls.indexOf(tl))
                    intent.putExtra("channelID", NOTIF_TRT)
                    intent.putExtra("documentID", tl.documentid)
                    intent.putExtra("title", ctx.resources.getString(R.string.time_for_treatment))
                    val trt = Treatment.get_treatments_documentid(tl.documentid.toString())
                    val msg = String.format("%s - %.2f %s", trt!!.name, trt.dose, trt.unit)
                    intent.putExtra("message", msg)
                    val pendingIntent = PendingIntent.getBroadcast(ctx, tl.timelinetype, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    val alarm = AlarmData(tl.datetimestamp.seconds*1000, pendingIntent)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.trigerAtMillis, alarm.pendingIntent)
//                    Log.i("ADD_ALARM", "Time: " + alarm.trigerAtMillis + " Content: " + msg)
                    alarms.add(alarm)
                } else {
                    intent.putExtra("notificationID", tls.indexOf(tl))
                    intent.putExtra("channelID", NOTIF_APT)
                    intent.putExtra("documentID", tl.documentid)
                    intent.putExtra("title", ctx.resources.getString(R.string.time_for_appointment))
                    val apt = Appointment.get_appointments_documentid(tl.documentid.toString())
                    val doc = DoctorContact.get_doctorcontacts_documentid(apt!!.docconid.toString())
                    val msg = String.format("%s - %s - %s", doc!!.name, apt.note, apt.place)
                    intent.putExtra("message", msg )
                    val pendingIntent = PendingIntent.getBroadcast(ctx, tl.timelinetype, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    val alarm = AlarmData(tl.datetimestamp.seconds*1000, pendingIntent)
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarm.trigerAtMillis, alarm.pendingIntent)
//                    Log.i("ADD_ALARM", "Time: " + alarm.trigerAtMillis + " Content: " + msg)
                    alarms.add(alarm)
                }
            }
        }
    }

    override fun onReceive(ctx: Context, intent: Intent?) {
        val manager = ctx.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = intent!!.getIntExtra("notificationID", 0)
        val channelID = intent.getIntExtra("channelID", 0)
        val title = intent.getStringExtra("title")
        val msg = intent.getStringExtra("message")
        val notification : Notification = NotificationCompat.Builder(ctx, channelID.toString())
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(msg)
            .build()
        manager.notify(notificationID, notification)

        if (channelID == NOTIF_TRT) {
            val documentID = intent.getStringExtra("documentID")
            val trt = Treatment.get_treatments_documentid(documentID.toString())!!
            Treatment.update_occurrence(trt.documentid.toString(), trt.frequency, trt.nextoccurrence, trt.enddate, trt.remindhour, trt.remindmin)
        }
    }

    data class AlarmData(val trigerAtMillis:Long, val pendingIntent: PendingIntent)
}