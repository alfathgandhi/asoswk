package org.d3ifcool.quiz.obj

import java.sql.Time

class Sesi {

    var sesi:String?=null
    var namaStage:String?=null
    var tanggal:String?=null
    var idUser:ArrayList<String>?=null
    var jam:String?=null

    constructor(){}

    constructor( sesi:String,namaStage:String,tanggal:String,idUser:ArrayList<String>,jam:String){
        this.sesi=sesi
        this.namaStage=namaStage
        this.tanggal = tanggal
        this.idUser=idUser
        this.jam=jam


    }

}