package com.text.textr01.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import kotlinx.android.synthetic.main.activity_subscription.*

class SubscriptionActivity : AppCompatActivity() {

    lateinit var rl: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subscription)



        val intent=Intent(this,PayementActivity::class.java)

        onemonth.setOnClickListener {
            intent.putExtra("rate","100")
            startActivity(intent)
        }

        threemonth.setOnClickListener {
            intent.putExtra("rate","200")
            startActivity(intent)
        }

        sixmonth.setOnClickListener {
            intent.putExtra("rate","300")
            startActivity(intent)
        }

        oneyear.setOnClickListener {
            intent.putExtra("rate","500")
            startActivity(intent)
        }
    }
}
