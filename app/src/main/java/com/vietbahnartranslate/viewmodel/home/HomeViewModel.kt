package com.vietbahnartranslate.viewmodel.home

import android.app.Application
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.util.Base64
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vietbahnartranslate.model.data.Translation
import com.vietbahnartranslate.model.repository.SpeakRepo
import com.vietbahnartranslate.model.repository.TranslateRepo
import com.vietbahnartranslate.model.repository.WordRepo
import com.vietbahnartranslate.model.source.remote.FirebaseConnector
import com.vietbahnartranslate.utils.DataUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application): AndroidViewModel(application) {
    private val TAG = "HomeViewModel"

    /**
     * WordRepo to save Translation to History when click Translate Button
     */
    private val wordRepo = WordRepo(application)
    /**
     * LiveData
     */
    private val _translatedBahnaric = MutableLiveData(String())
    val translatedBahnaric : LiveData<String> = _translatedBahnaric

    private val _maleSpeech = MutableLiveData(String())
    val maleSpeech: LiveData<String> = _maleSpeech

    private val _femaleSpeech = MutableLiveData(String())
    val femaleSpeech : LiveData<String> = _femaleSpeech

    init {
        viewModelScope.launch(Dispatchers.IO) {
            FirebaseConnector.readFirebaseDatabase("")
            TranslateRepo.translatedBahnaricFlow.collect{
                _translatedBahnaric.postValue(it)
            }
        }

        viewModelScope.launch(Dispatchers.IO) {
            SpeakRepo.speechFlow.collect{ speech ->
                Log.d(TAG, "collect")
                Log.d(TAG, "speech is $speech")
                val data = Base64.decode(speech, Base64.DEFAULT)
                val mediaPlayer = MediaPlayer()
                mediaPlayer.setDataSource(object : MediaDataSource() {
                    override fun close() {

                    }
                    override fun readAt(
                        position: Long,
                        buffer: ByteArray?,
                        offset: Int,
                        size: Int
                    ): Int {
                        val length = getSize()
                        var sizeReturned = size
                        if (position >= length) return -1 // EOF
                        if (position + size > length) sizeReturned = (length - position).toInt()
                        System.arraycopy(data, position.toInt(), buffer, offset, sizeReturned)
                        return sizeReturned
                    }

                    override fun getSize(): Long {
                        return data.size.toLong()
                    }
                })
                mediaPlayer.prepareAsync()
                mediaPlayer.setVolume(100f, 100f)
                mediaPlayer.isLooping = false
                mediaPlayer.playbackParams = mediaPlayer.playbackParams.setSpeed(DataUtils.speed)
                mediaPlayer.setOnPreparedListener { mp -> mp?.start() }
            }
        }
    }

    fun translate(text: String) {
        viewModelScope.launch(Dispatchers.IO){
            TranslateRepo.callAPITranslate(text)
        }
    }

    fun speak() {
        viewModelScope.launch(Dispatchers.IO) {
            val gender = if (!DataUtils.gender) "male" else "female"
            Log.d(TAG, "text is ${_translatedBahnaric.value.toString()}")
            SpeakRepo.callAPISpeak(_translatedBahnaric.value.toString(), gender)
        }
    }

    fun onAddToFavouriteButtonClick(vietnamese: String) {
        // Bahnaric can get from LiveData
        viewModelScope.launch(Dispatchers.IO) {
            val translation = Translation(vietnamese, _translatedBahnaric.value.toString(), null, null, false, null, null)
            wordRepo.insert(translation)
        }
    }

}