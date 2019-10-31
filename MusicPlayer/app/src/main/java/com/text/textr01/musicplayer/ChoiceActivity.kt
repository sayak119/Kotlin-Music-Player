package com.text.textr01.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_choice.*


//choice activity to go page on either register or login
class ChoiceActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choice)

        val registerintent= Intent(this,RegisterActivity::class.java)
        val loginintent=Intent(this,LoginActivity::class.java)

        register.setOnClickListener {
            startActivity(registerintent)
        }
        login.setOnClickListener {
            startActivity(loginintent)
        }
    }
}
