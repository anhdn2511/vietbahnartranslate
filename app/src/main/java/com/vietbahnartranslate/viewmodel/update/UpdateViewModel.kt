package com.vietbahnartranslate.viewmodel.update

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.vietbahnartranslate.model.repository.AddRepo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "UpdateViewModel"

    fun update(text1: String, text2: String) {
        val launch = viewModelScope.launch(Dispatchers.IO) {
            AddRepo.callAPIAdd(text1, text2)
        }
    }
}