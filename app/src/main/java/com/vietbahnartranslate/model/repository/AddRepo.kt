package com.vietbahnartranslate.model.repository

import android.util.Log
import com.vietbahnartranslate.model.source.remote.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AddRepo {
    private val TAG = "Add Repo"
    private val remoteAPIInterface: RemoteAPIInterface = RemoteAPIInterface.create()

    fun callAPIAdd(text1: String, text2: String) {
        val input = InputAPIAdd(text1, text2)
        val api = AddRepo.remoteAPIInterface.add(input)
        api.enqueue(object: Callback<OutputAPIAdd> {
            override fun onResponse(
                call: Call<OutputAPIAdd>,
                response: Response<OutputAPIAdd>
            ) {
                if (response.body() != null) {
                    val res = response.body()
                    if (res != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OutputAPIAdd>, t: Throwable) {

            }
        })
    }
}