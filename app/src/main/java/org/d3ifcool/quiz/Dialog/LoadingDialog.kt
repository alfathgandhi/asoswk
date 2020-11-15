package org.d3ifcool.quiz.Dialog

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import org.d3ifcool.quiz.R

class LoadingDialog {
    lateinit var activity :Activity
    lateinit var alertDialog :Dialog

    constructor(activity:Activity){
        this.activity=activity

    }

    fun loadingAlertDialog(){

        val builder : AlertDialog.Builder = AlertDialog.Builder(activity)

        val inflater : LayoutInflater = activity.layoutInflater

        builder.setView(inflater.inflate(R.layout.loading_dialog,null))
        builder.setCancelable(false)


        alertDialog=builder.create()
        alertDialog.show()


    }

    fun dismissDialog(){

        alertDialog.dismiss()


    }




}