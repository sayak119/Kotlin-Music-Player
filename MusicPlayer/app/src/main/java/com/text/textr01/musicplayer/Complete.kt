package com.text.textr01.musicplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RelativeLayout
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_complete.*

//complete activity to show the message to user that purchase complete
class Complete : AppCompatActivity() {

    lateinit var rl: RelativeLayout
    override fun onResume() {


        //get the intent content
        var phone:String=intent.getStringExtra("phone")
        rl=findViewById(R.id.completelayout)


        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")

        var themeValuenew:String="jgs"

        //listeners on token for check token value
        val tokenListener= object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                themeValuenew=p0.child("theme").getValue().toString()
                check(themeValuenew)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        RootRef.child(phone).addValueEventListener(tokenListener)






        //Toast.makeText(this,phone,Toast.LENGTH_LONG).show()
        super.onResume()
    }

    //check theme value
    private fun check(value: String) {
        if(value.equals("0"))
        {
            rl.setBackgroundResource(R.drawable.grad8)
        }
        if(value.equals("1"))
        {
            rl.setBackgroundResource(R.drawable.back2)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)

        var phone:String=intent.getStringExtra("phone")

        val intent= Intent(this,MainActivity::class.java)


        completemyplayer.setOnClickListener {
            intent.putExtra("phone",phone)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {

    }
}
