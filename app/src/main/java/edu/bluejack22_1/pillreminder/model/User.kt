package edu.bluejack22_1.pillreminder.model

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class User(val role:String?, val uid:String?, val name:String?, val email:String?, val phone:String?, val photo:String?) {
    companion object {
        lateinit var curr: User
        var db = Firebase.firestore
        var auth = Firebase.auth

        fun relog(){
            db.collection("users").document(auth.currentUser!!.uid).addSnapshotListener{ ss, e->
                if (e!=null) return@addSnapshotListener
                if (ss!=null && ss.exists()) {
                    curr = User(ss.data!!.get("role").toString(), ss.id, ss.data!!.get("name").toString(), ss.data!!.get("email").toString(), ss.data!!.get("phone").toString(), ss.data!!.get("photo").toString())
                }
            }
        }

        fun login(uid: String) {
            db.collection("users").document(auth.currentUser!!.uid).addSnapshotListener{ ss, e->
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

        fun register(role:String, email: String, pass: String, name: String, phone: String): Boolean {
            var cek = false
            auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Register Firebase", "Added to auth")
                    val user = task.result.user
                    val addDoc = hashMapOf(
                        "role" to role,
                        "name" to name,
                        "email" to email,
                        "phone" to phone,
                        "photo" to "https://i.pravatar.cc/150?u=" + user!!.uid
                    )
                    db.collection("users").document(user!!.uid).set(addDoc).addOnSuccessListener { cek = true }.addOnFailureListener { cek = false }
                } else {
                   cek = false
                }
            }
            if (cek) fetch_all_users()
            return cek
        }

        var allUsers: MutableList<User> = mutableListOf()
        fun fetch_all_users(){
            allUsers.clear()
            db.collection("users")
            .get()
            .addOnSuccessListener { docs ->
                for (doc in docs) {
                    val temp =
                        User (
                            doc.data.get("role").toString(),
                            doc.id,
                            doc.data.get("name").toString(),
                            doc.data.get("email").toString(),
                            doc.data.get("phone").toString(),
                            doc.data.get("photo").toString()
                        )
                    allUsers.add(temp)
                    Log.i("GET_USERS", temp.toString())
                }
            }
            .addOnFailureListener { e ->
                Log.e("GET_USERS", e.toString())
            }
        }

        fun get_user_email(email: String?):User? {
            for (user in allUsers){
                if (user.email.equals(email)) return user
            }
            return null
        }

        fun get_user_uid(uid: String?):User? {
            for (user in allUsers){
                if (user.uid.equals(uid)) return user
            }
            return null
        }

    }
}