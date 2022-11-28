package com.vietbahnartranslate.model.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.vietbahnartranslate.model.data.Setting
import com.vietbahnartranslate.model.data.Translation

@Database(
    entities = [Translation::class],
    version = 2,
    exportSchema = false
)
abstract class AppLocalDatabase : RoomDatabase() {
    abstract fun getTranslationDAO(): TranslationDAO

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