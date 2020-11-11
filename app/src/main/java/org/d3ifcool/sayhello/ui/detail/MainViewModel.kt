package org.d3ifcool.sayhello.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.d3ifcool.sayhello.data.Mahasiswa
import org.d3ifcool.sayhello.data.MahasiswaDao

class MainViewModel(private val db : MahasiswaDao) : ViewModel(){

    fun getData(kelas: String): LiveData<List<Mahasiswa>> {
        return db.getData(kelas)
    }

    fun insertData(mahasiswa: Mahasiswa){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                db.insertData(mahasiswa)
            }
        }
    }

    fun deleteData(ids: List<Int>){
        val newIds = ids.toList()
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                db.deleteData(newIds)
            }
        }
    }

    fun updateData(mahasiswa: Mahasiswa){
        viewModelScope.launch {
            withContext(Dispatchers.IO){
                db.updateData(mahasiswa)
            }
        }
    }

    fun getElementById(ids: List<Int>) = db.getById(ids.toList())
}