package com.vietbahnartranslate.model.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vietbahnartranslate.model.data.Translation

@Dao
interface TranslationDAO {
    @Query("SELECT * FROM Translation ORDER BY id DESC")
    fun getAll(): List<Translation>

    @Query("SELECT * FROM Translation WHERE isFavourite = :isFavourite ORDER BY id DESC")
    fun getAllByIsFavourite(isFavourite: Boolean) : List<Translation>

    @Query("SELECT * FROM Translation WHERE isFavourite = :isFavourite AND groupId = :groupId ORDER BY id DESC")
    fun getAllByIsFavouriteAndGroup(isFavourite: Boolean, groupId: Int?) : List<Translation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg translation: Translation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(listTranslation: List<Translation>)

    @Query("UPDATE Translation SET isFavourite = :isFavourite, groupId = :groupId WHERE id = :id")
    fun updateIsFavourite( id: Int?, isFavourite: Boolean, groupId: Int?)

    @Query("SELECT * FROM Translation WHERE id = :id")
    fun getItem(id: Int?) : Translation

    @Query("DELETE FROM Translation WHERE id = :id")
    fun deleteItem(id: Int?)

    @Query("DELETE FROM Translation")
    fun deleteAll()
}