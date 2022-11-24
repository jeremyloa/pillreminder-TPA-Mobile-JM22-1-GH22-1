package edu.bluejack22_1.pillreminder

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object UserCurrent {
    lateinit var curr: User
    var db = Firebase.firestore
    var auth = Firebase.auth

    fun login(uid: String) {
        db.collection("users").document(auth.currentUser!!.uid).addSnapshotListener{ss, e->
            if (e!=null) return@addSnapshotListener
            if (ss!=null && ss.exists()) {
                curr = User(ss.data!!.get("role").toString(), ss.id, ss.data!!.get("name").toString(), ss.data!!.get("email").toString(), ss.data!!.get("phone").toString(), ss.data!!.get("photo").toString())
            }
        }
    }

    fun logout(){
        if (FirebaseAuth.getInstance().currentUser!=null) {
            FirebaseAuth.getInstance().signOut()
        }
        curr = User("", "", "", "", "", "")
    }

}