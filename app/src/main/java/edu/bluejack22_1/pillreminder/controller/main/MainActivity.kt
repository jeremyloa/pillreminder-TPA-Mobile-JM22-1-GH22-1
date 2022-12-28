package edu.bluejack22_1.pillreminder.controller.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack22_1.pillreminder.R
import edu.bluejack22_1.pillreminder.model.User

class MainActivity : AppCompatActivity() {
    private var content: FrameLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        User.relog()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var nav: BottomNavigationView = findViewById(R.id.navigation)
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
        loadFragment(Home.newInstance())
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