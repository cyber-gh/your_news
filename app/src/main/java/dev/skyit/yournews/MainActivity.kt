package dev.skyit.yournews

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dev.skyit.yournews.api.client.INewsAPIClient
import dev.skyit.yournews.api.client.NewsAPIClient
import dev.skyit.yournews.ui.custom.AppToolbar

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import timber.log.Timber

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView = bottomNavigationView
        val controller = findNavController(R.id.nav_host_fragment)

        navView.setupWithNavController(controller)
    }





}