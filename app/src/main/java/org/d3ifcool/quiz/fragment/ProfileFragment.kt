package org.d3ifcool.quiz.fragment

import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.d3ifcool.quiz.R
import org.d3ifcool.quiz.obj.User

class ProfileFragment: Fragment() {
private lateinit var databaseReference: DatabaseReference
private lateinit var id: String
private lateinit var user:User
private lateinit var nilai:ArrayList<Int>
    var nama : EditText? =null
    var nrp : EditText? =null
    var email : EditText? =null
    var seksi: EditText? =null
    var noHp: EditText? =null
    var pass: EditText? =null
   private lateinit var url:String
    val instance = FirebaseAuth.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view=inflater.inflate(R.layout.fragment_profile,container,false)


        id = instance.currentUser!!.uid



        databaseReference=FirebaseDatabase.getInstance().reference.child("User").child(id)




         nama = view.findViewById<EditText>(R.id.et_nama_profile)
         nrp = view.findViewById<EditText>(R.id.et_nrp_profile)
         email = view.findViewById<EditText>(R.id.et_username_profile)
         seksi = view.findViewById<EditText>(R.id.et_seksi_profile)
         noHp = view.findViewById<EditText>(R.id.et_noHp_profile)
         pass = view.findViewById<EditText>(R.id.et_password_profile)

        Thread().start()



        view.findViewById<Button>(R.id.bt_submit).setOnClickListener {

            if (pass?.text!!.isEmpty()||nama?.text!!.isEmpty()||nrp?.text!!.isEmpty()||email?.text!!.isEmpty()||seksi?.text!!.isEmpty()||noHp?.text!!.isEmpty()) {

                Toast.makeText(context, "Semua field harus terisi dengan benar", Toast.LENGTH_LONG).show()

            } else {

                val bajir = EmailAuthProvider.getCredential(
                    instance.currentUser?.email.toString(),
                    pass?.text.toString()
                )

                instance.currentUser!!.reauthenticate(bajir).addOnCompleteListener {
                    if (it.isSuccessful) {

                        databaseReference.child("nama").setValue( nama?.text.toString())
                        databaseReference.child("nrp").setValue(  nrp?.text.toString())
                        databaseReference.child("seksi").setValue(seksi?.text.toString())
                        databaseReference.child("noHp").setValue( noHp?.text.toString())
                        databaseReference.child("email").setValue( email?.text.toString())
                        Toast.makeText(context, "Edit Data Berhasil", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(context, "Password Anda Salah", Toast.LENGTH_LONG).show()

                    }

                }

            }
        }

        return view }


    inner class Thread():java.lang.Thread(){
        override fun run() {
            databaseReference.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    user= snapshot.getValue(User::class.java)!!


                    nama?.text = SpannableStringBuilder(user.nama)
                    nrp?.text = SpannableStringBuilder(user.nrp)
                    email?.text = SpannableStringBuilder(user.username)
                    seksi?.text = SpannableStringBuilder(user.seksi)
                    noHp?.text = SpannableStringBuilder(user.noHp)

                }
            })
        }
    }



    }
