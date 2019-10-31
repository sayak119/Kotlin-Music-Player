package com.text.textr01.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_welcome.*
import java.lang.Thread.sleep
import java.sql.Time
import java.util.*
import kotlin.concurrent.timer


//welcome page
class WelcomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        //test.setOnClickListener{
          //  Toast.makeText(this,"hs",Toast.LENGTH_LONG).show()
        val intent=Intent(this,ChoiceActivity::class.java)
            //startActivity(intent)

        //updown animation
        val uptodown=AnimationUtils.loadAnimation(this,R.anim.uptodown)
        l1.startAnimation(uptodown)

        val downtotop=AnimationUtils.loadAnimation(this,R.anim.downtoup)
        l2.startAnimation(downtotop)

        val fade = AnimationUtils.loadAnimation(this, R.anim.fade)
        harness.startAnimation(fade)

        //after 5 second choice page open to open login or registration page

        Timer().schedule(object : TimerTask() {
            override fun run() {
                startActivity(intent)
            }
        }, 5000)
    }

}
