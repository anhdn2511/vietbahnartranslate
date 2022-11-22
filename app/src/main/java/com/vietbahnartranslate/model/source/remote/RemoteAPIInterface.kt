package com.vietbahnartranslate.model.source.remote

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface RemoteAPIInterface {
    @POST("translate/vi_ba")
    fun translate(@Body input:InputAPITranslate) : Call<OutputAPITranslate>

    @POST("speak")
    fun speak(@Body input:InputAPISpeak): Call<OutputAPISpeak>

    companion object {
        private const val BASE_URL = "https://bahnar-bk.dscilab.site:10001/"
        fun create() : RemoteAPIInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(RemoteAPIInterface::class.java)
        }
    }
}