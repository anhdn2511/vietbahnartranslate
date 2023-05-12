package com.vietbahnartranslate.model.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userDataStore")

    companion object {
        val GENDER = booleanPreferencesKey("GENDER")
        val SPEED = floatPreferencesKey("SPEED")
        val IS_SIGNED_IN = booleanPreferencesKey("IS_SIGNED_IN")
        val DISPLAY_NAME = stringPreferencesKey("DISPLAY_NAME")
        val EMAIL = stringPreferencesKey("EMAIL")
        val PHOTO_URL = stringPreferencesKey("PHOTO_URL")
    }

    suspend fun saveToDataStore(setting: Setting) {
        context.dataStore.edit {
            it[GENDER] = setting.gender
            it[SPEED] = setting.speed
            it[IS_SIGNED_IN] = setting.isSignedIn
            it[DISPLAY_NAME] = setting.displayName
            it[EMAIL] = setting.email
            it[PHOTO_URL] = setting.photoURL.toString()
        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        Setting(
            gender = it[GENDER]?:false,
            speed = it[SPEED]?:1.0f,
            isSignedIn = it[IS_SIGNED_IN]?:false,
            displayName = it[DISPLAY_NAME]?:"",
            email = it[EMAIL]?:"",
            photoURL = it[PHOTO_URL]?:""
        )
    }
}