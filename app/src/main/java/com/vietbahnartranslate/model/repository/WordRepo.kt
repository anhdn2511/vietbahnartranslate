package com.vietbahnartranslate.model.repository

import android.app.Application
import android.text.TextUtils
import com.vietbahnartranslate.model.data.Translation
import com.vietbahnartranslate.model.source.local.AppLocalDatabase
import com.vietbahnartranslate.model.source.local.TranslationDAO
import com.vietbahnartranslate.model.source.remote.FirebaseConnector
import com.vietbahnartranslate.utils.DataUtils

object WordRepo {
    private lateinit var translationDAO: TranslationDAO
    private val TAG = "Word Repo"

    operator fun invoke(application: Application): WordRepo {
        this.translationDAO = AppLocalDatabase.buildDatabase(application).getTranslationDAO()
        return this
    }


    fun getHistory(): List<Translation> {
        return translationDAO.getAll()
    }

    fun getFavouriteList(): List<Translation> {
        return translationDAO.getAllByIsFavourite(true)
    }

    fun getFavouriteListByGroup(group: Int?): List<Translation> {
        return translationDAO.getAllByIsFavouriteAndGroup(true, group)
    }

    fun getWordById(id: Int?): Translation {
        return translationDAO.getItem(id)
    }

    fun insert(translation: Translation) {
        val history = getHistory()
        for (item in history) {
            if (TextUtils.equals(item.vietnamese, translation.vietnamese)) {
                val id = item.id
                translationDAO.deleteItem(id)
                break
            }
        }
        translationDAO.insert(translation)
    }

    fun updateIsFavourite(id: Int?, isFavourite: Boolean, groupId: Int?) {
        translationDAO.updateIsFavourite(id, isFavourite, groupId)
    }

    fun backupDataToRemote() {
        /**
         * Call Write to Firebase Database here
         */

        // Get data from local
        val dataFromLocal = getHistory()

        // Get data from remote
        val dataFromRemote = FirebaseConnector.readFirebaseDatabase(DataUtils.email)

        val resultList = mergeTwoSource(dataFromRemote, dataFromLocal)

        /**
         * Call Firebase Connector -> write data to Firebase Database
         */
    }

    fun restoreDataFromRemote() {
        /**
         * Call Read from Firebase Database here
         */

        // Get data from local
        val dataFromLocal = getHistory()

        // Get data from remote
        val dataFromRemote = FirebaseConnector.readFirebaseDatabase(DataUtils.email)

        val resultList = mergeTwoSource(dataFromLocal, dataFromRemote)

        /**
         * Call write data to local
         */
        translationDAO.deleteAll()
        translationDAO.insertAll(resultList)
    }

    private fun mergeTwoSource(firstList: List<Translation>, secondList: List<Translation>) : List<Translation> {
        /**
         * firstList is list need to merge
         * secondList is list merged to firstList
         */
        if (firstList.isEmpty() && secondList.isEmpty()) return listOf()

        if (firstList.isEmpty()) return secondList

        if (secondList.isEmpty()) return firstList

        val resultList = mutableListOf<Translation>()
        resultList.addAll(firstList)

        for (secondListItem in secondList) {
            var isReplaced = false
            var resultListIndex = 0
            for (resultListItem in resultList) {
                if (TextUtils.equals(secondListItem.vietnamese, resultListItem.vietnamese)) {
                    if (secondListItem.timestamp != null && resultListItem.timestamp != null) {
                        isReplaced = true
                        if (secondListItem.timestamp >= resultListItem.timestamp) {
                            resultList[resultListIndex] = secondListItem
                            break
                        }
                    }
                }
                resultListIndex += 1
            }
            if (!isReplaced) {
                resultList.add(secondListItem)
            }
        }

        return resultList
    }
}