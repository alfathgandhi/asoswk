package com.d3if0093.jurnalmodul7

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Transformations
import kotlinx.coroutines.*


class DiaryViewModel(
    val database: DiaryDAO,
    application: Application
): AndroidViewModel(application) {


 private var viewModelJob = Job()
   private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
 val diarys=database.get()

val hasil= Transformations.map(diarys){
     diarys-> formatDiary(diarys,application.resources)
  }

fun onClickTambah(catat:String){
    uiScope.launch {
        val diary=Diary(0,catat)
        insert(diary)


    }

}
    fun onClickHapus(){
        uiScope.launch {
            clearAja()
        }
    }
    private  suspend fun clearAja(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }


    private suspend fun insert(diary: Diary) {
        withContext(Dispatchers.IO) {

            database.insert(diary)
        }
    }



}