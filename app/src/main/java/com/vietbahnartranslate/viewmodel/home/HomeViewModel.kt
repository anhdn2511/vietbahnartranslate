package com.vietbahnartranslate.viewmodel.home

import android.app.Application
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.util.Base64
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vietbahnartranslate.model.repository.TranslateRepo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {

    private val TAG = "Home View Model"

    private val translateRepo = TranslateRepo()
    private val audioTrack = AudioTrack.Builder()
        .setAudioAttributes(AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build())
        .setAudioFormat(AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(48000)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build())
        .build()

    /**
     * LiveData
     */
    private val _translatedBahnaric = MutableLiveData(String())
    val translatedBahnaric : LiveData<String> = _translatedBahnaric

    fun translate(text: String) {
        viewModelScope.launch(Dispatchers.IO){
            translateRepo.callAPITranslate(text)
            launch {
                translateRepo.translatedBahnaricFlow.collect{
                    _translatedBahnaric.postValue(it)
                }
            }
        }
    }

    private fun decodeSpeech(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            translateRepo.callAPISpeak(text, "male")
            launch {
                translateRepo.speechFlow.collect{ speech ->
                    Log.d(TAG, "speech is $speech")
                    val data = Base64.decode(speech, Base64.DEFAULT)
                    Log.d(TAG, "data is $data")
                    audioTrack.write(data, 0, data.size)
                }

            }
        }
    }

    fun playSpeech(text: String) {
        decodeSpeech(text)
        //audioTrack.play()
        //audioTrack.release()
    }
}