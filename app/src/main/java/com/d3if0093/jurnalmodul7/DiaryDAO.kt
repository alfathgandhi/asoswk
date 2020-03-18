package com.d3if0093.jurnalmodul7

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface DiaryDAO {

    @Insert
    fun insert(diary: Diary)

    @Query("SELECT * FROM DiaryS")
    fun  get():LiveData<List<Diary>>

    @Query("DELETE FROM DiaryS")
    fun clear()

}