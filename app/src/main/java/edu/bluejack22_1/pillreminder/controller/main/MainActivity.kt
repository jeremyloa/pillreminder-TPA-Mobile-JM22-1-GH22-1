package edu.bluejack22_1.pillreminder.controller.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.controller.Notification
import edu.bluejack22_1.pillreminder.model.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        User.login()
        Notification.initialize_channels(this)
        MsgRoom.allMsgRoom.clear()
        MsgRoom.fetch_all_msgrooms()
        setContentView(R.layout.activity_main)
        val nav: BottomNavigationView = findViewById(R.id.navigation)
        nav.selectedItemId = R.id.navigation_home
        nav.setOnItemSelectedListener{item ->
        when (item.itemId) {
            R.id.navigation_messages -> {
                val fragment = Messages.newInstance()
                loadFragment(fragment)
                true
            }
            R.id.navigation_home -> {
                val fragment = Home.newInstance()
                loadFragment(fragment)
                true
            }
            R.id.navigation_treatments -> {
                val fragment = Activities.newInstance()
                loadFragment(fragment)
                true
            }
            else -> {false}
        }}
        if (intent.hasExtra("fragment")) {
            val frag = intent.getSerializableExtra("fragment") as Class<out Fragment>
            loadFragment(frag.newInstance())
            nav.selectedItemId = R.id.navigation_treatments
        } else {
            loadFragment(Home.newInstance())
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        if (fragment!=null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.content, fragment, fragment.javaClass.getSimpleName())
                .commit()
            return true
        }
        return false
    }

}