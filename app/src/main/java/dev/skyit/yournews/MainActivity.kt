package dev.skyit.yournews

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val navView = bottomNavigationView
//        val controller = findNavController(R.id.nav_host_fragment)
//
//        navView.setupWithNavController(controller)
    }





}