package org.d3ifcool.quiz.obj

class Stage

{
    var namaStage:String?=null
    var penjelasan:String?=null
    var linkVideo:String?=null
    var soal:ArrayList<Soal>?=null


    constructor(){}
    constructor( nama:String, soal:ArrayList<Soal>,linkVideo:String?,penjelasan:String?){
        this.linkVideo=linkVideo
        this.penjelasan=penjelasan
        this.namaStage=nama
        this.soal=soal
       }

}