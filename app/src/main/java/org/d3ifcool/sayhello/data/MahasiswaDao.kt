package org.d3ifcool.sayhello.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MahasiswaDao {
    @Insert
    fun insertData(mahasiswa: Mahasiswa)

    @Query("SELECT * FROM mahasiswa WHERE kelas = :kelas ORDER BY nim")
    fun getData(kelas: String): LiveData<List<Mahasiswa>>


    @Query("DELETE FROM mahasiswa WHERE id IN (:ids)")
    fun deleteData(ids: List<Int>)

    @Update
    fun updateData(mahasiswa: Mahasiswa)

    @Query("SELECT * FROM mahasiswa WHERE id = :ids")
    fun getById(ids: List<Int>): LiveData<List<Mahasiswa>>
}