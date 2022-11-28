package com.vietbahnartranslate.model.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "Group",
    indices = [Index(value = ["name"], unique = true)]
)
data class Group(
    @ColumnInfo(name="name")
    val name: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int
)
