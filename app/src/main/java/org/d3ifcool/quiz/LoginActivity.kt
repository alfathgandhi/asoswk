package org.d3ifcool.quiz

import Session
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import org.d3ifcool.quiz.Dialog.LoadingDialog

class LoginActivity:AppCompatActivity() {
    private var mUsername: EditText? = null
    private var mPassword: EditText? = null
    private var mDaftar: TextView? = null
    private var mButtonLogin: Button? = null
    private var auth: FirebaseAuth? = null
    private val mUserID: String? = null
    private var session: Session? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        session = Session(this)
        if(session!!.loggedIn()){
            startActivity(Intent(this@LoginActivity,MainActivity::class.java))
            finish()
        }
        auth = FirebaseAuth.getInstance()
        mUsername = findViewById(R.id.et_email_login)
        mButtonLogin = findViewById(R.id.bt_login)
        mButtonLogin?.setOnClickListener { login() }
        mDaftar = findViewById(R.id.tv_daftar)
        mPassword = findViewById(R.id.et_password_login)
        mDaftar?.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }


    }
    private fun login() {
        val dialog = LoadingDialog(this)

        dialog.loadingAlertDialog()

        val email = mUsername!!.text.toString().trim()
        val password = mPassword!!.text.toString().trim { it <= ' ' }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this@LoginActivity, "Masukan Email Address", Toast.LENGTH_LONG).show()
            return

        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this@LoginActivity, "Masukan Password", Toast.LENGTH_LONG).show()
            return

        }

        auth!!.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Toast.makeText(this@LoginActivity, "Gagal Login", Toast.LENGTH_LONG).show()
               dialog.dismissDialog()
            } else {

                session!!.setLoggedin(true)
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }
        }
    }
}