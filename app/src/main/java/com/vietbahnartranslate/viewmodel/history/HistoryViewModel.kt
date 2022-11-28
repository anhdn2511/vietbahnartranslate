package com.vietbahnartranslate.viewmodel.history

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vietbahnartranslate.model.data.Translation
import com.vietbahnartranslate.model.repository.WordRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "HistoryViewModel"

    private val wordRepo = WordRepo(application)

    /**
     * LiveData history List
     */
    private val _historyList = MutableLiveData<List<Translation>>()
    val historyList: LiveData<List<Translation>> = _historyList

    fun initHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            _historyList.postValue(wordRepo.getHistory())
        }
    }

    fun onFavouriteClick(id: Int?, isFavourite: Boolean, groupId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            wordRepo.updateIsFavourite(id, isFavourite, groupId)
        }
    }
}