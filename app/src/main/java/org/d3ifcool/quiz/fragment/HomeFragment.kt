package org.d3ifcool.quiz.fragment

import Session
import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.ContentResolver

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import id.zelory.compressor.Compressor
import kotlinx.android.synthetic.main.fragment_home.view.*
import org.d3ifcool.quiz.Adapter.HomeAdapter
import org.d3ifcool.quiz.LoginActivity
import org.d3ifcool.quiz.QuizActivity
import org.d3ifcool.quiz.R
import org.d3ifcool.quiz.ShowImage
import org.d3ifcool.quiz.ViewModel.PictVm
import org.d3ifcool.quiz.obj.Sesi
import org.d3ifcool.quiz.obj.Stage
import org.d3ifcool.quiz.obj.User
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.coroutines.Continuation


class HomeFragment:Fragment() {
    var session:Session?=null
    lateinit var uri: String
    lateinit var view_home:View
    lateinit var view_img:CircleImageView
    lateinit var vm:PictVm
    var list : ArrayList<Int>? = arrayListOf()
    val databas = FirebaseDatabase.getInstance().reference







    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {




         vm = ViewModelProviders.of(this).get(PictVm::class.java)

        vm.link.observe(this, androidx.lifecycle.Observer {
          if(it!="None") {
              Glide.with(this)
                  .load(it)
                  .into(view_img)
          }

        })
        session=Session(context)
        view_home = inflater.inflate(R.layout.fragment_home, container, false)
        view_img=view_home.findViewById<CircleImageView>(R.id.circleImageView)


view_img.setOnClickListener {

    val intent = Intent(context, ShowImage::class.java)
    intent.putExtra("id",uri)

    startActivity(intent)
}

        view_home.bt_pass_home.setOnClickListener {
            if(view_home.bt_pass_home.text == "Close"){
                val colorputih = ContextCompat.getColor(context!!, R.color.putih)
                view_home.constrait.setBackgroundColor(colorputih)
                view_home.et_berada_home.text="Sekarang anda berada di"
                view_home.bt_pass_home.text="YOUR PASS"
                view_home.welcome.textSize = resources.getDimension(R.dimen.font_medium)
                view_home.welcome.text = "Selamat Datang"



            }else{

                viewPass()
            }

        }

        val basid=  databas.child("User").child(FirebaseAuth.getInstance().currentUser!!.uid)

        basid.addValueEventListener(object : ValueEventListener{

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.toString(),Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                val user = snapshot.getValue(User::class.java)
                list= user?.nilai
                view_home.findViewById<TextView>(R.id.welcome).visibility=View.VISIBLE
                view_home.findViewById<CircleImageView>(R.id.circleImageView).visibility=View.VISIBLE
                view_home.findViewById<TextView>(R.id.et_nama_home).visibility=View.VISIBLE
                view_home.findViewById<TextView>(R.id.et_nrp_home).visibility=View.VISIBLE
                view_home.findViewById<TextView>(R.id.et_berada_home).visibility=View.VISIBLE
                view_home.findViewById<TextView>(R.id.et_stage_home).visibility=View.VISIBLE
                view_home.findViewById<Button>(R.id.bt_pass_home).visibility=View.VISIBLE

                view_home.findViewById<TextView>(R.id.et_nama_home).text = user?.nama
                view_home.findViewById<TextView>(R.id.et_nrp_home).text = user?.nrp

                uri = user?.profile!!

                vm.gantiLink(uri)

                if(user.posisi==null){
                    basid.child("posisi").setValue("Stage 1")
                }

                view_home.findViewById<TextView>(R.id.et_stage_home).text = user.posisi

                view_home.findViewById<ProgressBar>(R.id.progressBar_home).visibility=View.GONE

            }




        })


        return view_home
    }







    private fun viewPass(){
        val idx_nilai = (view_home.et_stage_home.text.toString().takeLast(1).toInt() - 1)
        val nilai= list?.get(idx_nilai)
        val colorKuning = ContextCompat.getColor(context!!, R.color.kuning)
        val colorhijau = ContextCompat.getColor(context!!, R.color.hijau)
        val colormerah = ContextCompat.getColor(context!!, R.color.belum_lulus)

        Log.d("indexx",idx_nilai.toString())


        if(nilai!! < 0){

            view_home.constrait.setBackgroundColor(colorKuning)
            view_home.et_berada_home.text="Anda belum  mengerjakan REFRESH FIOS"


        }else if( nilai!! >= 80){
            view_home.constrait.setBackgroundColor(colorhijau)
            view_home.et_berada_home.text="Anda telah Lulus"


        }else{
            view_home.constrait.setBackgroundColor(colormerah)
            view_home.et_berada_home.text="Anda lelum Lulus"
        }

        view_home.bt_pass_home.text = "Close"
        view_home.welcome.textSize = resources.getDimension(R.dimen.font_small)
        view_home.welcome.text="Final Inspection Pass"

    }
}


