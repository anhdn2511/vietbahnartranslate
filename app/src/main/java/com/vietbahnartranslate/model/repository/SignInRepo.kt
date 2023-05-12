package com.vietbahnartranslate.model.repository

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.vietbahnartranslate.R
import com.vietbahnartranslate.utils.DataUtils

object SignInRepo {
    private val TAG = "SignInRepo"
    const val REQ_ONE_TAP = 2

    private lateinit var application : Application

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    val userLiveData: LiveData<FirebaseUser> = _userLiveData

    private val _signedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val signedOutLiveData: LiveData<Boolean> = _signedOutLiveData

    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest

    operator fun invoke(application: Application) : SignInRepo {
        this.application = application
        return this
    }

    fun init() {
        Log.d(TAG, "init()")
        if (firebaseAuth.currentUser != null) {
            _userLiveData.postValue(firebaseAuth.currentUser)
            _signedOutLiveData.postValue(false)
        }
    }

    fun signIn(signInResultLauncher: ActivityResultLauncher<IntentSenderRequest>, webClientId: String) {
        Log.d(TAG, "signIn()")
        oneTapClient = Identity.getSignInClient(application)
        signInRequest = BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(webClientId)
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            .setAutoSelectEnabled(true)
            .build()
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener { result ->
                try {
                    //activity.startIntentSenderForResult(result.pendingIntent.intentSender, REQ_ONE_TAP, null, 0, 0, 0, null)
                    val ib = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                    signInResultLauncher.launch(ib)

                }
                catch (e: IntentSender.SendIntentException) {
                    Log.d(TAG, "Couldn't start One Tap UI: ${e.localizedMessage}")
                }
            }
            .addOnFailureListener {e ->
                e.localizedMessage?.let { Log.d(TAG, "signIn failured: $it") }
            }
    }

    fun signInWithGoogleToken(data: Intent?) {
        Log.d(TAG, "signInWithGoogleToken()")
        val googleCredential = oneTapClient.getSignInCredentialFromIntent(data)
        val googleIdToken = googleCredential.googleIdToken

        if (googleIdToken != null) {
            val firebaseCredential = GoogleAuthProvider.getCredential(googleIdToken, null)
            firebaseAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign In Success, update UI with user's information
                        Log.d(TAG, "signInWithCredential:success")
                        _userLiveData.postValue(firebaseAuth.currentUser)
                    } else {
                        // If sign in fails, display a message to user.
                        Log.d(TAG, "signInWithCredential:failure", task.exception)
                    }
                }
        } else {
            Log.d(TAG, "No ID Token")
        }
    }

    fun signOut() {
        firebaseAuth.signOut()
        _signedOutLiveData.postValue(true)
        _userLiveData.postValue(firebaseAuth.currentUser)

        // Config temp, Move this to view model
        DataUtils.isSignedIn = false
        DataUtils.email = ""
        DataUtils.displayName = ""
        DataUtils.photoURL = ""
    }
}