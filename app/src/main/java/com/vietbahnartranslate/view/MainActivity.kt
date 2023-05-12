package com.vietbahnartranslate.view

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vietbahnartranslate.R
import com.vietbahnartranslate.model.data.DataStoreManager
import com.vietbahnartranslate.model.data.Setting
import com.vietbahnartranslate.utils.DataUtils
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    private lateinit var bottomNavigationView: BottomNavigationView

    lateinit var dataStoreManager: DataStoreManager

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        supportActionBar?.hide()

        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // DataStore
        dataStoreManager = DataStoreManager(this)

        // Save to setting utils
        GlobalScope.launch(Dispatchers.IO) {
            dataStoreManager.getFromDataStore().catch { e ->
                e.printStackTrace()
            }.collect {
                withContext(Dispatchers.Main) {
                    DataUtils.gender = it.gender
                    DataUtils.speed = it.speed
                    DataUtils.isSignedIn = it.isSignedIn
                    DataUtils.displayName = it.displayName
                    DataUtils.email = it.email
                    DataUtils.photoURL = it.photoURL
                    Log.d(TAG, DataUtils.log())
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onStop() {
        super.onStop()
        val setting = Setting(
            DataUtils.gender,
            DataUtils.speed,
            DataUtils.isSignedIn,
            DataUtils.displayName,
            DataUtils.email,
            DataUtils.photoURL
        )
        // Save to DataStore
        GlobalScope.launch(Dispatchers.IO) {
            dataStoreManager.saveToDataStore(setting)
            Log.d(TAG, "saveToDataStore with ${setting.displayName}")
        }
    }

    fun hideBottomNavigationView() {
        bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavigationView() {
        bottomNavigationView.visibility = View.VISIBLE
    }
}