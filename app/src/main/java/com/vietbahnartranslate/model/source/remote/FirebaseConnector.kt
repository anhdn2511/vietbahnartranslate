package com.vietbahnartranslate.model.source.remote

import android.text.TextUtils
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vietbahnartranslate.model.data.Translation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

object FirebaseConnector {
    private val TAG = "FirebaseConnector"
    private val databaseRef = Firebase.database.reference

    private val firebaseData = MutableStateFlow(mutableListOf<Translation>())
    val firebaseDataStateFlow: StateFlow<List<Translation>>
        get() = firebaseData

    data class FirebaseItem(
        val email: String? = null,
        val translationList: List<Translation>? = null
    )

    fun readFirebaseDatabase(email: String) {
        databaseRef.child("data").get().addOnSuccessListener {dataSnapShot ->
            val translationList = mutableListOf<Translation>()
            val children = dataSnapShot.children
            for (child in children) {
                val item = child.getValue(FirebaseItem::class.java)!!
                if (TextUtils.equals(email, item.email)) {
                    item.translationList?.let { translationList.addAll(it) }
                    break
                }
            }
            CoroutineScope(Dispatchers.Default).launch { firebaseData.emit(translationList) }
        }.addOnFailureListener {err ->
            Log.e("Firebase", "error getting data", err)
        }
    }

    fun writeFirebaseDatabase(email: String, resultList: List<Translation>) {
        databaseRef.child("data").get().addOnSuccessListener { dataSnapShot ->
            var index = 0
            var newUser = true
            val children = dataSnapShot.children
            for (child in children) {
                val item = child.getValue(FirebaseItem::class.java)!!
                if (TextUtils.equals(email, item.email)) {
                    newUser = false
                    break
                }
                index += 1
            }
            databaseRef.child("data").child("$index").child("translationList").setValue(resultList)
            if (newUser) databaseRef.child("data").child("$index").child("email").setValue(email)
        }
    }
}