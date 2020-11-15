package org.d3ifcool.quiz

import Session
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.database.FirebaseDatabase
import org.d3ifcool.quiz.Dialog.GantiPassDialog
import org.d3ifcool.quiz.ViewModel.QuizViewModel
import org.d3ifcool.quiz.ViewModel.QuizViewModelFactory
import org.d3ifcool.quiz.fragment.HomeFragment
import org.d3ifcool.quiz.fragment.ProfileFragment
import org.d3ifcool.quiz.fragment.QuizFragment

class MainActivity : AppCompatActivity(), GantiPassDialog.DialogListener{
    private var bottomNavigationView : BottomNavigationView?= null
    private var session: Session?= null
    private lateinit var password:String
    var auth= FirebaseAuth.getInstance().currentUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)
        setContentView(R.layout.activity_main)
    session=Session(this)
        if(!session!!.loggedIn()){
            logout()
        }

//        val db= FirebaseDatabase.getInstance().reference
//        val app = requireNotNull(this).application
//        val viewModelFactory = QuizViewModelFactory(db, app, FirebaseAuth.getInstance().currentUser!!.uid)
//       val viewmodel = ViewModelProviders.of(this, viewModelFactory).get(QuizViewModel::class.java)





        bottomNavigationView=findViewById(R.id.navigation)
        bottomNavigationView?.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->


            val id = menuItem.itemId
            if (id == R.id.navigation_home) {

                val fragment = HomeFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_layout, fragment)
                fragmentTransaction.commit()
                title = "Home"

                //                    getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.));
            } else if (id == R.id.navigation_joinQuiz) {
                val fragment =QuizFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_layout, fragment)
                fragmentTransaction.commit()
                title = "Quiz"

            } else if (id == R.id.navigation_profile) {
                val fragment = ProfileFragment()
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.frame_layout, fragment)
                fragmentTransaction.commit()
                title = "Profile"

            }
            true
        })
        bottomNavigationView?.setSelectedItemId(R.id.navigation_home)


    }


    private fun logout() {
        session!!.setLoggedin(false)
        finish()
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.bt_logout -> {
                session!!.setLoggedin(false)
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        R.id.ganti_pass ->{
        GantiPassDialog().show(supportFragmentManager,"Ganti Password")


        }

        }


        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       val menux:  MenuInflater= MenuInflater(this)
        menux.inflate(R.menu.menu_atas,menu)

    return true
    }

    override fun Lister(list: List<String>) {
        Log.i("testrr",list[0])
        Log.i("testrr1",list[1])
        Log.i("testrr2",list[2])

        val kreden = EmailAuthProvider.getCredential(auth?.email.toString(),list[0])

        auth?.reauthenticate(kreden)?.addOnCompleteListener {
            if(it.isSuccessful){
                auth!!.updatePassword(list[1])
                Toast.makeText(this,"Update password berhasil",Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"Password anda salah",Toast.LENGTH_LONG).show()

            }
        }
    }
}
