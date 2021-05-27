package com.manageitid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val DashboardFragment =  Dashboard()
        val fragment = supportFragmentManager.findFragmentByTag(DashboardFragment::class.java.simpleName)

        if (fragment !is Dashboard) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, DashboardFragment, DashboardFragment::class.java.simpleName)
                    .commit()
        }
    }
}