package org.d3ifcool.quiz.obj

class Record{

    var stage:String?=null
    var idUser:String?=null
    var sesi:String?=null
    var nilai:String?=null
    var tanggal:String?=null


    constructor(){}

    constructor(stage:String?, idUser:String?, nilai: String, tanggal:String?, sesi:String?)
    {
    this.stage =stage
    this.idUser =idUser
   this.nilai =nilai
        this.tanggal=tanggal
        this.sesi=sesi
    }



}