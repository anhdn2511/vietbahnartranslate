package com.vietbahnartranslate.model.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class DataStoreManager(val context: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "userDataStore")

    companion object {
        val GENDER = booleanPreferencesKey("GENDER")
        val SPEED = floatPreferencesKey("SPEED")
        val IS_SIGNED_IN = booleanPreferencesKey("IS_SIGNED_IN")
    }

    suspend fun saveToDataStore(setting: Setting) {
        context.dataStore.edit {
            it[GENDER] = setting.gender
            it[SPEED] = setting.speed
            it[IS_SIGNED_IN] = setting.isSignedIn
        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        Setting(
            gender = it[GENDER]?:false,
            speed = it[SPEED]?:1.0f,
            isSignedIn = it[IS_SIGNED_IN]?:false
        )
    }
}