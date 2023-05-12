package com.vietbahnartranslate.model.source.remote

import android.text.TextUtils
import android.util.Log
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.vietbahnartranslate.model.data.Translation

object FirebaseConnector {
    private val databaseRef = Firebase.database.reference

    data class FirebaseItem(
        val email: String? = null,
        val translationList: List<Translation>? = null
    )

    fun readFirebaseDatabase(email: String): List<Translation> {
        val translationList = mutableListOf<Translation>()
        databaseRef.child("data").get().addOnSuccessListener {dataSnapShot ->
            val children = dataSnapShot.children
            for (child in children) {
                val item = child.getValue(FirebaseItem::class.java)!!
                if (TextUtils.equals(email, item.email)) {
                    item.translationList?.let { translationList.addAll(it) }
                    break
                }
            }
        }.addOnFailureListener {err ->
            Log.e("Firebase", "error getting data", err)
        }
        return translationList
    }

    fun writeFirebaseDatabase(email: String, list: Translation) {
        //
    }
}