package com.manageitid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.manageitid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = FirebaseAuth.getInstance()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val DashboardFragment =  Dashboard()
        val fragment = supportFragmentManager.findFragmentByTag(DashboardFragment::class.java.simpleName)

        if (fragment !is Dashboard) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, DashboardFragment, DashboardFragment::class.java.simpleName)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ui, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId
        when(itemView){
            R.id.action_logout -> {
                auth.signOut()
                startActivity(Intent(baseContext, Login::class.java))
            }
        }


        return false
    }

}