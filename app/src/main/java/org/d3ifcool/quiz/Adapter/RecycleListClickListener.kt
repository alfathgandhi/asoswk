package org.d3ifcool.quiz.Adapter


import org.d3ifcool.quiz.obj.Stage

interface RecycleListClickListener {

        fun onClicked(position:Int, soal: Stage)

}