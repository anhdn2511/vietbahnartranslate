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

class TranslateRepo {
    private val TAG = "Translate Repo"
    private val remoteAPIInterface: RemoteAPIInterface = RemoteAPIInterface.create()
    private var translatedBahnaric = ""
    private var speech = ""

    /**
     * StateFlow
     */
    private val _translatedBahnaric = MutableStateFlow("")
    val translatedBahnaricFlow : StateFlow<String> = _translatedBahnaric.asStateFlow()

    private val _speech = MutableStateFlow("")
    val speechFlow : StateFlow<String> = _speech.asStateFlow()

    fun callAPITranslate(text: String) {
        val input = InputAPITranslate(text)
        val api = remoteAPIInterface.translate(input)
        api.enqueue(object: Callback<OutputAPITranslate> {
            override fun onResponse(
                call: Call<OutputAPITranslate>,
                response: Response<OutputAPITranslate>
            ) {
                if (response.body() != null) {
                    val res = response.body()
                    if (res != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            _translatedBahnaric.emit(res.tgt)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OutputAPITranslate>, t: Throwable) {
                translatedBahnaric = "Error in translating"
            }
        })
    }

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
                            _speech.emit(res.speech)
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