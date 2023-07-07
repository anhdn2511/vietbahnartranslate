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

object FeedbackRepo {
    private val TAG = "Feedback Repo"
    private val remoteAPIInterface: RemoteAPIInterface = RemoteAPIInterface.create()


    fun callAPIAdd(text1: String, text2: String) {
        Log.d(TAG, "Call Feedback API")
        val input = InputAPIFeedback(text1, text2)
        val api = remoteAPIInterface.feedback(input)
        api.enqueue(object: Callback<OutputAPIFeedback> {
            override fun onResponse(
                call: Call<OutputAPIFeedback>,
                response: Response<OutputAPIFeedback>
            ) {
                if (response.body() != null) {
                    val res = response.body()
                    if (res != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OutputAPIFeedback>, t: Throwable) {

            }
        })
    }
}