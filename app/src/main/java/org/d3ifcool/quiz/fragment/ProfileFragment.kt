package org.d3ifcool.quiz.fragment

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableStringBuilder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageTask
import com.google.firebase.storage.UploadTask
import de.hdodenhof.circleimageview.CircleImageView
import org.d3ifcool.quiz.R
import org.d3ifcool.quiz.ViewModel.PictVm
import org.d3ifcool.quiz.obj.User
import java.io.ByteArrayOutputStream

class ProfileFragment: Fragment() {
private lateinit var databaseReference: DatabaseReference
var databas = FirebaseDatabase.getInstance().reference
private lateinit var id: String
private lateinit var vm: PictVm
private lateinit var imageBitmap:Bitmap
private lateinit var progressBar: ProgressDialog
private lateinit var user:User
private lateinit var nilai:ArrayList<Int>
private lateinit var view_fragment:View
    var nama : EditText? =null
    var nrp : EditText? =null
    lateinit var imageUri: Uri
    var email : EditText? =null
    var seksi: EditText? =null
    var noHp: EditText? =null
    var pass: EditText? =null
   private lateinit var url:String
    val instance = FirebaseAuth.getInstance()
    private val REQUEST_IMAGE_CAPTURE =100



    private val PERMISSION_CODE =1001
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
      view_fragment=inflater.inflate(R.layout.fragment_profile,container,false)


        id = instance.currentUser!!.uid

        vm = ViewModelProviders.of(this).get(PictVm::class.java)

        vm.link.observe(this, androidx.lifecycle.Observer {
            if(it!="None") {
                Glide.with(this)
                    .load(it)
                    .into(view_fragment.findViewById(R.id.imageCircle))
            }

        })

        view_fragment.findViewById<ImageView>(R.id.addFhoto).setOnClickListener {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED
                ) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else {

                    takeImage()

                }


            }
        }

        databaseReference=FirebaseDatabase.getInstance().reference.child("User").child(id)





         nama = view_fragment.findViewById<EditText>(R.id.et_nama_profile)
         nrp = view_fragment.findViewById<EditText>(R.id.et_nrp_profile)
         email = view_fragment.findViewById<EditText>(R.id.et_username_profile)
         seksi = view_fragment.findViewById<EditText>(R.id.et_seksi_profile)
         noHp = view_fragment.findViewById<EditText>(R.id.et_noHp_profile)
         pass = view_fragment.findViewById<EditText>(R.id.et_password_profile)

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
                vm.gantiLink(user.profile!!)


            }
        })



        view_fragment.findViewById<Button>(R.id.bt_submit).setOnClickListener {

            if (pass?.text!!.isEmpty()||nama?.text!!.isEmpty()||nrp?.text!!.isEmpty()||email?.text!!.isEmpty()||seksi?.text!!.isEmpty()||noHp?.text!!.isEmpty()) {

                Toast.makeText(context, "Semua field harus terisi dengan benar", Toast.LENGTH_LONG).show()

            } else {
                progressBar = ProgressDialog(context)
                progressBar.setMessage("Mohon tunggu..")
                progressBar.setCanceledOnTouchOutside(false)
                progressBar.show()


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

                        uploadImageAndSaveUri()

                    } else {
                        Toast.makeText(context, "Edit data gagal", Toast.LENGTH_LONG).show()
                        progressBar.dismiss()

                    }

                }

            }
        }

        return view_fragment }



    fun takeImage(){


        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_IMAGE_CAPTURE&& resultCode == Activity.RESULT_OK){

            imageUri = data?.data!!
            Glide.with(this)
                .load(imageUri).into(view_fragment.findViewById(R.id.imageCircle))





        }
    }

    private fun uploadImageAndSaveUri() {



        if(imageUri!=null)
        {
            imageBitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,imageUri)
            val baos= ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG,20,baos)
            val image=baos.toByteArray()
            val storageRef = FirebaseStorage.getInstance()
                .reference.child("pics/${FirebaseAuth.getInstance().currentUser?.uid}")

            val uploadTask : StorageTask<*>

            uploadTask = storageRef.putBytes(image)

            uploadTask.continueWithTask(com.google.android.gms.tasks.Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
                    task ->

                if(!task.isSuccessful){

                    task.exception?.let {
                        throw it
                    }


                }
                return@Continuation storageRef.downloadUrl

            }).addOnCompleteListener {
                    task ->

                if(task.isSuccessful){

                    val downloadUri = task.result
                    val url = downloadUri.toString()


                   databas.child("User")
                        .child(FirebaseAuth.getInstance().currentUser!!.uid)
                        .child("profile")
                        .setValue(url)

                    progressBar.dismiss()
                    Toast.makeText(context, "Edit Data Berhasil", Toast.LENGTH_LONG).show()



                }
            }


        }



    }



    }
