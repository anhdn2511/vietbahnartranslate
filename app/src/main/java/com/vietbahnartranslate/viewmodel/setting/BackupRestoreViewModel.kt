package com.vietbahnartranslate.viewmodel.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.vietbahnartranslate.model.repository.WordRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BackupRestoreViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "BackupRestoreViewModel"

    private val wordRepo = WordRepo(application)

    fun onBackupButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            wordRepo.backupDataToRemote()
        }

    }

    fun onRestoreButtonClick() {
        viewModelScope.launch(Dispatchers.IO) {
            wordRepo.restoreDataFromRemote()
        }
    }
}