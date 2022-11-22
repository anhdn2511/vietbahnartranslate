package com.vietbahnartranslate.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vietbahnartranslate.R

class MainActivity : AppCompatActivity() {
    private val TAG = "Main Activity"
//    var input: EditText? = null
//    var output: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
//        input = findViewById(R.id.vietnamese_input)
//        output = findViewById(R.id.bahnaric_output)
//
//
//
//        input?.addTextChangedListener(object : TextWatcher {
//            override fun afterTextChanged(s: Editable) {}
//            override fun beforeTextChanged(
//                s: CharSequence, start: Int,
//                count: Int, after: Int
//            ) {
//            }
//
//            override fun onTextChanged(
//                s: CharSequence, start: Int,
//                before: Int, count: Int
//            ) {
//                Log.d(TAG, s.toString())
//                val apiInterface = ApiInterface.create().getTranslatedWord(Vietnamese(s.toString(), "Combined"))
//                apiInterface.enqueue(object: Callback<Bahnaric> {
//                    override fun onResponse(call: Call<Bahnaric>, response: Response<Bahnaric>) {
//                        if (response.body() != null) {
//                            val bahnaric = response.body()
//                            Log.d(TAG, bahnaric.toString())
//                            output?.text = response.body().toString()
//                        }
//                    }
//
//                    override fun onFailure(call: Call<Bahnaric>, t: Throwable) {
//
//                    }
//
//                })
//            }
//        })
    }
}