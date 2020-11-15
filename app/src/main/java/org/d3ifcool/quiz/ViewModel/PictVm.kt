package org.d3ifcool.quiz.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PictVm:ViewModel() {

    private val xlink= MutableLiveData<String>()
    var link: LiveData<String> = xlink
init {
  xlink.value="None"
}


    fun gantiLink(link:String){
        xlink.value=link

    }

}

