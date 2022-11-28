package com.vietbahnartranslate.viewmodel.setting

import android.app.Application
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseUser
import com.vietbahnartranslate.model.repository.SignInRepo

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "SignInViewModel"

    val userLiveData: LiveData<FirebaseUser>

    private val signInRepo = SignInRepo(application)

    init {
        signInRepo.init()
        userLiveData = signInRepo.userLiveData
    }

    fun signIn(signInResultLauncher: ActivityResultLauncher<IntentSenderRequest>, webClientId: String) {
        signInRepo.signIn(signInResultLauncher, webClientId)
    }

    fun signInWithGoogleToken(data: Intent?) {
        signInRepo.signInWithGoogleToken(data)
    }

    fun signOut() {
        signInRepo.signOut()
    }
}