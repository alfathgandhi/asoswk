package org.d3ifcool.quiz.obj

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


class User {
    var nama : String?= null
    var nrp : String?= null
    var seksi : String?= null
    var noHp : String?= null
    var username : String?= null
    var nilai : ArrayList<Int>?=null
    var profile : String?=null
    var posisi: String?=null

    constructor(){}

    constructor(nama:String?,nrp:String?,seksi:String?,noHp:String?,username:String?,nilai: ArrayList<Int>?,profile : String?,posisi:String?)
    {
        this.nama = nama
        this.nrp = nrp
        this.seksi = seksi
        this.noHp = noHp
        this.username = username
        this.nilai = nilai
        this.profile=profile
        this.posisi=posisi




    }





}
