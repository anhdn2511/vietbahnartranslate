package com.vietbahnartranslate.model.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "TranslationBelongToGroup")
data class TranslationBelongToGroup(
    val a: Int
)
