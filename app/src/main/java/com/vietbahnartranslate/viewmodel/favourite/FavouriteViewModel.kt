package com.vietbahnartranslate.viewmodel.favourite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.vietbahnartranslate.model.data.Translation
import com.vietbahnartranslate.model.repository.WordRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavouriteViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "FavouriteViewModel"

    private val wordRepo = WordRepo(application)

    /**
     * LiveData favourite List
     */
    private val _favouriteList = MutableLiveData<List<Translation>>()
    val favouriteList: LiveData<List<Translation>> = _favouriteList

    fun initFavouriteList() {
        viewModelScope.launch(Dispatchers.IO) {
            _favouriteList.postValue(wordRepo.getFavouriteListByGroup(0))
        }
    }

    fun toggleFavouriteList(groupId: Int?) {
        viewModelScope.launch(Dispatchers.IO) {
            _favouriteList.postValue(wordRepo.getFavouriteListByGroup(groupId))
        }
    }


    fun onFavouriteClick(id: Int?, isFavourite: Boolean) {
//        viewModelScope.launch(Dispatchers.IO) {
//            wordRepo.updateIsFavourite(id, isFavourite)
//        }
    }
}