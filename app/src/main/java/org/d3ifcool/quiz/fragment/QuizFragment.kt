package org.d3ifcool.quiz.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.d3ifcool.quiz.Adapter.StageAdapter
import org.d3ifcool.quiz.QuizActivity
import org.d3ifcool.quiz.R
import org.d3ifcool.quiz.Adapter.RecycleListClickListener
import org.d3ifcool.quiz.obj.Sesi
import org.d3ifcool.quiz.obj.Soal
import org.d3ifcool.quiz.obj.Stage
import java.sql.Time
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class QuizFragment : Fragment(),RecycleListClickListener {
    private var mDatabaseRef: DatabaseReference? = null
    private var datakey:ArrayList<String>?=null
   private  var stage: ArrayList<Stage>?=null
    lateinit var adapter:StageAdapter
    lateinit var sesiStage:String
    val db =FirebaseDatabase.getInstance()
    val auth =FirebaseAuth.getInstance()
    var bolean = false
    val time= Calendar.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        datakey = arrayListOf()
        stage = arrayListOf()
        val view_home = inflater.inflate(R.layout.fragment_quiz, container, false)
       var soal="Siapakah kamu?"
       val pilihan= mutableListOf(
           "A",
           "B",
           "C"
       )
        val jawaban="A"
mDatabaseRef=db.getReference("Stage")
     val id =mDatabaseRef!!.push().key
     val data= Soal(soal,pilihan,jawaban)
     val data1=Soal(soal,pilihan,jawaban)
        val soall= arrayListOf<Soal>()
        soall.add(data)
        soall.add(data1)
     val stage= Stage("STAGE 2",soall,"http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4","Lorem ipsum dolor sit amet")
  // mDatabaseRef!!.child(id!!).setValue(stage)
    val button=view_home.findViewById<Button>(R.id.testButton)

        adapter = StageAdapter(this)

        val thread=Thread()

        thread.start()


        val rv : RecyclerView=view_home.findViewById(R.id.rvQuiz)
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.reverseLayout = false
        linearLayoutManager.stackFromEnd = false
        rv.setLayoutManager(linearLayoutManager)

        rv.adapter= adapter





        return view_home



    }


    override fun onClicked(position: Int, soal: Stage) {

        if(bolean && sesiStage.equals(soal.namaStage)){

            val intent = Intent(context, QuizActivity::class.java)
            intent.putExtra("id",datakey?.get(position))
            Log.i("DATAKEY",datakey?.size.toString()  )

            startActivity(intent)


        }else{
            Toast.makeText(context,"Tidak ada Sesi", Toast.LENGTH_LONG).show()
        }




        }
    inner class Thread (): java.lang.Thread()
    {


        override fun run() {
            mDatabaseRef?.addValueEventListener(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    stage?.clear()

                    for(data in snapshot.children){
                        val id = data.key
                        val stagec = data.getValue(Stage::class.java)

                        stage?.add(stagec!!)

                        datakey?.add(id!!)

                        Log.i("DATAX",id)







                    }
                    adapter.data= stage as ArrayList<Stage>





                }
            })


            db.getReference("Sesi").addValueEventListener(object  : ValueEventListener{


                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.i("jamnya1",snapshot.childrenCount.toString())


                    for(data in snapshot.children){

                        val dataY = data.getValue(Sesi::class.java)


                        val tanggalya = dataY?.tanggal

                        val sdf1 = SimpleDateFormat("HH:mm", Locale.US).parse(dataY?.jam).time





                        val timeSekarang = System.currentTimeMillis()
                        val sdf2_tanggal= SimpleDateFormat("d MMM, YYYY", Locale.US)
                        val sdf2_jam = SimpleDateFormat("HH:mm", Locale.US)
                        val jam= sdf2_jam.format(timeSekarang)
                        val tanggal = sdf2_tanggal.format(timeSekarang)

                        val convert = SimpleDateFormat("HH:mm", Locale.US).parse(jam).time






                        Log.i("jamnya",sdf1.toString())
                        Log.i("jamnya1",convert.toString())


                        if(dataY?.idUser?.contains(auth.currentUser?.uid)!! && sdf1 > convert && tanggalya.equals(tanggal) ){
                            sesiStage=dataY.namaStage!!
                            bolean=true
                        }

                      Log.i("SESSI",auth.currentUser?.uid)





                    }
                }
            })


        }
    }

    }


