package com.vietbahnartranslate.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Translation")
data class Translation(
    @ColumnInfo(name = "vietnamese")
    val vietnamese: String? = "",

    @ColumnInfo(name = "bahnaric")
    val bahnaric: String? = "",

    @ColumnInfo(name = "maleSpeech")
    val maleSpeech: String? = "",

    @ColumnInfo(name = "femaleSpeech")
    val femaleSpeech: String? = "",

    @ColumnInfo(name = "isFavourite")
    val isFavourite: Boolean? = false,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long? = 0L,

    // For demo, later config
    @ColumnInfo(name = "groupId")
    val groupName: Int? = null,

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
)
