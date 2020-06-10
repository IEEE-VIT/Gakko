package com.ieeevit.gakko.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ieeevit.gakko.R
import kotlinx.android.synthetic.main.activity_home_activty.*

class HomeActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_activty)
        navController = findNavController(R.id.nav_host_fragment_home_activity)
        bottom_navigation_view_home_activity.setupWithNavController(navController)
    }
}
