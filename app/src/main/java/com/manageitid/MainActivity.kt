package com.manageitid

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebViewFragment
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.manageitid.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val auth : FirebaseAuth get() = Login.user
    private val DashboardFragment =  DashboardFragment()


    public companion object {
        const val TAG = "___TEST___"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val fragment = supportFragmentManager.findFragmentByTag(DashboardFragment::class.java.simpleName)

        if (fragment !is DashboardFragment) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.fragment_container, DashboardFragment, DashboardFragment::class.java.simpleName)
                    .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_ui, menu)
        val item = menu?.findItem(R.id.spinner)
        val spinner = item?.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(
                baseContext,
                R.array.allFilters,
                R.layout.item_filter_dropdown
        )
        adapter.setDropDownViewResource(R.layout.item_filter_dropdown)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
            ) {

                when (position) {
                    0 -> {
                        DashboardFragment.filter("All")
                    }
                    1 -> {
                        DashboardFragment.filter("Income")
                    }
                    2 -> {
                        DashboardFragment.filter("Expense")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var itemView = item.itemId
        when(itemView){
            R.id.action_logout -> {
                auth.signOut()
                startActivity(Intent(baseContext, Login::class.java))
            }
            R.id.action_about ->{
                val AboutFragment =  AboutFragment()
                val fr = supportFragmentManager.findFragmentByTag(AboutFragment::class.java.simpleName)

                if (fr !is AboutFragment) {
                    supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.fragment_container, AboutFragment, AboutFragment::class.java.simpleName)
                            .commit()
                }
            }
        }

        return false
    }


}