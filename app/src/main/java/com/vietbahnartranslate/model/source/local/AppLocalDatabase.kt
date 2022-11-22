package com.vietbahnartranslate.model.source.local

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

abstract class AppLocalDatabase : RoomDatabase() {
    companion object {
        @Volatile private var INSTANCE : AppLocalDatabase? = null
        private const val DB_NAME = "vietbahnartranslate.db"

        fun buildDatabase(context: Context) : AppLocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) return tempInstance
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppLocalDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}