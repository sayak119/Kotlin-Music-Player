package com.text.textr01.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.RelativeLayout
import android.widget.Switch
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_selection.*

//for selection of music library and subscription
class SelectionActivity : AppCompatActivity() {

    lateinit var sw:Switch
    lateinit var rl:RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection)

        val phone:String=intent.getStringExtra("phone")
        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")

        sw=findViewById(R.id.switch1)
        rl=findViewById(R.id.selectionlayout)
        //rl2=findViewById(R.id.paymentlayout)



//for change theme use switch listerner
        var switchListener=object :CompoundButton.OnCheckedChangeListener
        {
            //check button is checked or not
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {

                if(isChecked)
                {
                    rl.setBackgroundResource(R.drawable.back2)
                    RootRef.child(phone).child("theme").setValue("1")
                }
                else
                {
                    rl.setBackgroundResource(R.drawable.grad8)
                    RootRef.child(phone).child("theme").setValue("0")
                }
            }
        }
        sw.setOnCheckedChangeListener(switchListener)


        val intentplayer= Intent(this,MainActivity::class.java)

        intentplayer.putExtra("phone",phone)
        //startActivity(intentplayer)
        val intentsubscription=Intent(this,PayementActivity::class.java)

        intentsubscription.putExtra("phone",phone)
//        startActivity(intentsubscription)


      //start player and subscription activity

        myplayer.setOnClickListener({
            startActivity(intentplayer)
        })
        subscription.setOnClickListener({
            startActivity(intentsubscription)
        })

    }

    override fun onBackPressed() {

    }
}
