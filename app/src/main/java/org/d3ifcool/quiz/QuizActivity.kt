package org.d3ifcool.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_quiz.*
import org.d3ifcool.quiz.ViewModel.QuizViewModel
import org.d3ifcool.quiz.ViewModel.QuizViewModelFactory
import org.d3ifcool.quiz.obj.Record
import org.d3ifcool.quiz.obj.Soal
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class QuizActivity() :AppCompatActivity(){

    var soal: ArrayList<Soal>? = null
    var jawaban: String? = null
    var jawabanArray: ArrayList<String>? = null
    var counter = 0
    var xcore = 0
    var button_mulai: Button? = null
    val database = FirebaseDatabase.getInstance().reference
    var tvsoal: TextView? = null
    var buttonNext: Button? = null
    var buttonPrev: Button? = null
    var radiogrup: RadioGroup? = null
    var radio1: RadioButton? = null
    var radio2: RadioButton? = null
    var radio3: RadioButton? = null
    var radio4: RadioButton? = null
    var noSoal: TextView? = null
    var back: Boolean=true
    var nStage:String?=null
    private lateinit var viewmodel: QuizViewModel


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        val id = intent.extras?.getString("id")
        val idSesir = intent.extras?.getString("idSesi")
        val sizer = intent.extras?.getStringArrayList("size")

        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_quiz)
        jawabanArray = arrayListOf()
        tvsoal = findViewById(R.id.tv_soal)
        radiogrup = findViewById(R.id.radioGrup)
        radio1 = findViewById(R.id.radio1)
        radio2 = findViewById(R.id.radio2)
        radio3 = findViewById(R.id.radio3)
        radio4 = findViewById(R.id.radio4)
        noSoal = findViewById(R.id.soalKe)
        val penjelasan = findViewById<TextView>(R.id.penjelasan)

        this.supportActionBar?.hide()

        val username = FirebaseAuth.getInstance()

        val app = requireNotNull(this).application
        val viewModelFactory = QuizViewModelFactory(database, app, id!!,username.currentUser!!.uid)
        viewmodel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel::class.java)
        viewmodel.getSesi(idSesir!!)


        viewmodel.soal.observe(this, Observer { soal ->
            soal.shuffle()
            this.soal = soal
        })

        button_mulai = findViewById(R.id.mulai_quiz)
        viewmodel.count.observe(this, Observer { count ->
            counter = count
        if(count== soal?.size?.minus(1)){

            button_mulai?.text="Finish"

        }

        viewmodel.penjelasan.observe(this, Observer {

            penjelasan.text=it.toString()
        })


        })

        viewmodel.score.observe(this, Observer { score ->
            xcore = score
            Log.i("SCOREE", score.toString())
        })
        
     
        
        

    val videoview = findViewById<VideoView>(R.id.video)

        viewmodel.link.observe(this, Observer {
            videoview.setVideoURI(Uri.parse(it))
        })

     val md : MediaController = MediaController(this)
        videoview.setMediaController(md)
        md.setAnchorView(videoview)
        videoview.requestFocus()
        videoview.start()
        videoview.setOnPreparedListener {
            findViewById<ProgressBar>(R.id.loading).visibility=View.GONE
        }
        button_mulai?.setOnClickListener {

            findViewById<ProgressBar>(R.id.loading).visibility=View.GONE
            back=false

            Log.i("GGG", soal?.get(counter)?.pertanyaan!!)

            if (button_mulai?.text!!.equals("NEXT") || button_mulai?.text!!.equals("Finish")) {

                if (counter == soal?.size!! - 1 ) {
                    checkJawaban()

                    database.child("Stage")

                    val calender = Calendar.getInstance()

                    val sdf1 = SimpleDateFormat("d MMM, YYYY", Locale.US)


                    val tanggal = sdf1.format(calender.time)
                    val scoreAkhir:Int = xcore * 100

                    val jadinyaa= scoreAkhir /soal?.size!!

                    Log.i("SCORENYAA",jadinyaa.toString())
                    Log.i("SCORENYA",soal?.size.toString())
                    var styleBg:Int?=null
                if(jadinyaa>75){
                    styleBg = R.style.MyDialogTheme
                }else{
                    styleBg= R.style.MyDialogTheme1
                }


                    val builder = AlertDialog.Builder(this,styleBg)






                        .setMessage("User       : ${viewmodel.namaUser} \nNilai       : $jadinyaa\nPeriode  : $tanggal")
                        .setPositiveButton("Oke") { dialog, which ->
                            val intent = Intent(this, MainActivity::class.java)


                                viewmodel.hapusUser(sizer!!,idSesir!!)




                            database.child("Record").push().setValue(
                                Record(
                                    viewmodel.namaStage,
                                    username.currentUser!!.uid,
                                    scoreAkhir.toString(),
                                    tanggal,
                                    viewmodel.namaSesi

                                )
                            )

                            var idx:Int?=viewmodel.namaStage.takeLast(1).toInt()
                            var jadi = (idx!!-1).toString()

                            Log.i("Indexnya",idx.toString())

                            database.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid).child("nilai").child(jadi).setValue(jadinyaa)

                            startActivity(intent)
                            finishAffinity()
                        }.setTitle("Quiz berakhir")


                        builder.setCancelable(false)

                    builder.create().show()



                } else {
                    checkJawaban()
                    viewmodel.tambahCount()
                    noSoal?.text =
                        "Soal : " + (counter + 1).toString() + "/" + soal?.size.toString()

                    setView()
                }

            } else {
                button_mulai?.text = "NEXT"
                tvsoal?.visibility = View.VISIBLE
                videoview.visibility=View.GONE
                penjelasan.visibility=View.GONE
                findViewById<TextView>(R.id.perhatian).visibility=View.GONE
                radiogrup?.visibility = View.VISIBLE
                setView()
                noSoal?.visibility=View.VISIBLE
                noSoal?.text = "Soal : " + (counter + 1).toString() + "/" + soal?.size.toString()
            }

        }


    }

    fun checkJawaban() {

        if (radio1?.isChecked!!) {

            if (radio1?.text.toString().equals(jawaban)) {
                viewmodel.tambahScore()
            }
        }
        if (radio2?.isChecked!!) {

            if (radio2?.text.toString().equals(jawaban)) {
                viewmodel.tambahScore()
            }
        }
        if (radio3?.isChecked!!) {

            if (radio3?.text.toString().equals(jawaban)) {
                viewmodel.tambahScore()
            }
        }
        if (radio4?.isChecked!!) {

            if (radio4?.text.toString().equals(jawaban)) {
                viewmodel.tambahScore()
            }
        }


    }

    fun setView() {

        radio1?.isChecked = true
        tv_soal.text = soal?.get(counter)?.pertanyaan
        soal?.get(counter)?.pilihan?.shuffle()
        radio1?.text = soal?.get(counter)?.pilihan?.get(0)
        radio2?.text = soal?.get(counter)?.pilihan?.get(1)
        radio3?.text = soal?.get(counter)?.pilihan?.get(2)
        radio4?.text = soal?.get(counter)?.pilihan?.get(3)
        jawaban = soal?.get(counter)?.jawaban


    }


    override fun onBackPressed() {

        if (back == true) {
            super.onBackPressed()
        }else{
            Toast.makeText(this,"Anda harus menyelesaikan quiz",Toast.LENGTH_SHORT).show()
        }

    }


}
