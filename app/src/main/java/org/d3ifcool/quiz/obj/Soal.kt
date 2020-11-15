package org.d3ifcool.quiz.obj

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


class Soal

{
    var pertanyaan:String?=null
    var pilihan:MutableList<String>?=null
    var jawaban:String?=null

constructor(){}
    constructor( test:String, test1:MutableList<String>,  jawab:String?){
    this.pertanyaan=test
    this.pilihan=test1
    this.jawaban=jawab
    }

}





