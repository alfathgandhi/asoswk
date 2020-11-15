package org.d3ifcool.quiz.fragment

import Session
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import de.hdodenhof.circleimageview.CircleImageView
import org.d3ifcool.quiz.Adapter.HomeAdapter
import org.d3ifcool.quiz.LoginActivity
import org.d3ifcool.quiz.R
import org.d3ifcool.quiz.ViewModel.PictVm
import org.d3ifcool.quiz.obj.Sesi
import org.d3ifcool.quiz.obj.User
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment:Fragment() {
    var session:Session?=null
    var Rview:RecyclerView?=null
    var grid:GridLayoutManager?=null
    lateinit var listX:ArrayList<String>
    var user:User?=null
    lateinit var adapter: HomeAdapter
    lateinit var view_home:View
    lateinit var vm:PictVm
    var list : ArrayList<Int>? = arrayListOf()
    val databas = FirebaseDatabase.getInstance().reference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

Thread().start()


         vm = ViewModelProviders.of(this).get(PictVm::class.java)

        vm.link.observe(this, androidx.lifecycle.Observer {
          if(it!="None") {

              Glide.with(this)
                  .load(it)
                  .into(view_home.findViewById<CircleImageView>(R.id.circleImageView))

          }

        })






        session=Session(context)
        view_home = inflater.inflate(R.layout.fragment_home, container, false)








        return view_home
    }




    inner class Thread():java.lang.Thread(){

        override fun run() {

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

                    vm.gantiLink(user?.profile!!)

                  view_home.findViewById<ProgressBar>(R.id.progressBar_home).visibility=View.GONE

                }




          })

        }
    }



}