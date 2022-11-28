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

object TranslateRepo {
    private val TAG = "Translate Repo"
    private val remoteAPIInterface: RemoteAPIInterface = RemoteAPIInterface.create()

    /**
     * StateFlow
     */
    private val translatedBahnaric = MutableStateFlow("")
    val translatedBahnaricFlow : StateFlow<String> = translatedBahnaric.asStateFlow()

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
                            translatedBahnaric.emit(res.tgt)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<OutputAPITranslate>, t: Throwable) {

            }
        })
    }
}