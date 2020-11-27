package org.d3ifcool.quiz

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ShowImage :AppCompatActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_showimage)

        val id = intent.extras?.getString("id")

        Glide.with(this)
            .load(id)
            .into(findViewById(R.id.imagee))
    }
}