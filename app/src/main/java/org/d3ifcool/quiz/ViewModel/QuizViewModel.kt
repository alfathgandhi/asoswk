package org.d3ifcool.quiz.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import org.d3ifcool.quiz.obj.Sesi
import org.d3ifcool.quiz.obj.Soal
import org.d3ifcool.quiz.obj.Stage
import org.d3ifcool.quiz.obj.User
import java.lang.StringBuilder

class QuizViewModel(val database: DatabaseReference,application:Application, val idData: String, val idUser: String): AndroidViewModel(application){

    private val xsoal=MutableLiveData<ArrayList<Soal>>()
    var soal:LiveData<ArrayList<Soal>> = xsoal

    lateinit var namaStage:String
    lateinit var namaUser:String
    lateinit var namaSesi:String


    private val xlink=MutableLiveData<String>()
    var link:LiveData<String> = xlink

    private val xpenjelasan=MutableLiveData<String>()
    var penjelasan:LiveData<String> = xpenjelasan

    private val xscore=MutableLiveData<Int>()
    var score:LiveData<Int> = xscore

    private val xcount=MutableLiveData<Int>()
    var count:LiveData<Int> = xcount

init {
Thread1().start()




    xscore.value=0
    xcount.value=0
}

    fun tambahScore(){
        xscore.value=xscore.value?.plus(1)
    }

    fun tambahCount(){
        xcount.value=xcount.value?.plus(1)
    }

    fun hapusUser(size:ArrayList<String>,idSesi:String ){

        for(x in 0 until size.size){

            if(size.get(x).equals(idUser)){

                size.removeAt(x)



            }


        }

        database.child("Sesi").child(idSesi).child("idUser").setValue(size)




    }

    fun getSesi(idSesi: String){

        Thread(){
            val sesi  = database.child("Sesi").child(idSesi)
            sesi.addListenerForSingleValueEvent(object : ValueEventListener{


                override fun onDataChange(snapshot: DataSnapshot) {
                    namaSesi = snapshot.getValue(Sesi::class.java)?.sesi!!
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }



            })






        }.start()


    }



inner class Thread1():Thread(){
    override fun run() {

        val basedId=database.child("Stage").child(idData)
        basedId.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val stage = snapshot.getValue(Stage::class.java)
                val cek = stage?.soal!!
                xsoal.value=cek
                namaStage = stage.namaStage.toString()
                xpenjelasan.value=stage.penjelasan.toString()
                xlink.value = stage.linkVideo.toString()
            }
        })



   val getUser=database.child("User").child(idUser)
        getUser.addListenerForSingleValueEvent(object  : ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(User::class.java)?.nama
                namaUser =user!!

            }
        })







    }
}


}