package com.vietbahnartranslate.model.repository

import android.util.Log
import com.vietbahnartranslate.model.source.remote.InputAPISpeak
import com.vietbahnartranslate.model.source.remote.OutputAPISpeak
import com.vietbahnartranslate.model.source.remote.RemoteAPIInterface
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SpeakRepo {
    private val TAG = "Speak Repo"
    private val remoteAPIInterface: RemoteAPIInterface = RemoteAPIInterface.create()

    /**
     * StateFlow
     */
    private val speech = MutableStateFlow("")
    val speechFlow : StateFlow<String> = speech.asStateFlow()

    fun callAPISpeak(text: String, gender: String) {
        val input = InputAPISpeak(text, gender)
        val api = remoteAPIInterface.speak(input)
        api.enqueue(object : Callback<OutputAPISpeak> {
            override fun onResponse(
                call: Call<OutputAPISpeak>,
                response: Response<OutputAPISpeak>
            ) {
                if (response.body() != null) {
                    val res = response.body()
                    if (res != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            Log.d(TAG, "emit")
                            speech.emit(res.speech)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OutputAPISpeak>, t: Throwable) {
                //TODO("Not yet implemented")
            }
        })
    }
}