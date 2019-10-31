package com.text.textr01.musicplayer

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_payement.*
import kotlinx.android.synthetic.main.activity_player.*

class PayementActivity : AppCompatActivity() {

    lateinit var progerssProgressDialog: ProgressDialog
    //lateinit var rl: RelativeLayout


    lateinit var rl:RelativeLayout
    override fun onResume() {

        var phone:String=intent.getStringExtra("phone")
        rl=findViewById(R.id.paymentlayout)


        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")

        var themeValuenew:String="jgs"

        //token listener to check token value and theme value
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
        setContentView(R.layout.activity_payement)

        var phone:String=intent.getStringExtra("phone")
       // var themeValue:String=intent.getStringExtra("themeValue")

       // rl=findViewById(R.id.paymentlayout)

       /** Toast.makeText(this,themeValue,Toast.LENGTH_LONG).show()


        if(themeValue.equals("0"))
        {
            //rl.setBackgroundResource(R.drawable.grad8)
        }
        else if(themeValue.equals("1"))
        {
            //rl.setBackgroundResource(R.drawable.back2)
        }**/

        progerssProgressDialog=ProgressDialog(this)



        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")



        var amountt:EditText
        var card:EditText
        var cvv:EditText
        var date:EditText

        amountt=findViewById(R.id.numberoftoken)
        card=findViewById(R.id.card)
        cvv=findViewById(R.id.cvv)
        date=findViewById(R.id.date)

        var amountvalue:String=amountt.text.toString()
        var cardvalue:String=card.text.toString()
        var cvvvalue:String=cvv.text.toString()
        var datevalue:String=date.text.toString()


        var tokenvalue:String="jgs"

        //listerner to check token value
        val tokenListener= object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                tokenvalue=p0.child("token").getValue().toString()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        RootRef.child(phone).addValueEventListener(tokenListener)


        var updatetoken:Int=0

        //do error handling
        paynowbtn.setOnClickListener {

            progerssProgressDialog.setTitle("Loading")
            progerssProgressDialog.setMessage("Please wait...")
            progerssProgressDialog.setCancelable(false)
            progerssProgressDialog.show()

            //Toast.makeText(this@PayementActivity,tokenvalue,Toast.LENGTH_LONG).show()

            if(amountt.text.isEmpty())
            {
                Toast.makeText(this,"please enter amount",Toast.LENGTH_LONG).show()
            }
            else if(card.text.isEmpty())
            {
                Toast.makeText(this,"please enter card number",Toast.LENGTH_LONG).show()
            }
            else if(cvv.text.isEmpty())
            {
                Toast.makeText(this,"please enter cvv",Toast.LENGTH_LONG).show()
            }
            else if(date.text.isEmpty())
            {
                Toast.makeText(this,"please enter date",Toast.LENGTH_LONG).show()
            }
            else
            {
                if(amountt.text.toString().equals("100"))
                {
                    updatetoken=10

                    purchaseToken(updatetoken,tokenvalue,phone)
                }
                else if(amountt.text.toString().equals("200"))
                {
                    updatetoken=20
                    //Toast.makeText(this,updatetoken.toString(),Toast.LENGTH_LONG).show()
                    purchaseToken(updatetoken,tokenvalue,phone)
                }
                else if(amountt.text.toString().equals("300"))

                {

                    updatetoken=30
                    //Toast.makeText(this,updatetoken.toString(),Toast.LENGTH_LONG).show()
                    purchaseToken(updatetoken,tokenvalue,phone)

                }
                else
                {
                    progerssProgressDialog.dismiss()
                    Toast.makeText(this,"Please enter valid amount to purchase token !",Toast.LENGTH_LONG).show()
                }

            }

        }

    }

//purchase token function increase the value of token that is store in database in user account by check amount
    private fun purchaseToken(updatetoken: Int, tokenvalue: String, phone: String) {

        var newtokenvalue:Int




        // Toast.makeText(this,updatetoken.toString(),Toast.LENGTH_LONG).show()
        //Toast.makeText(this,tokenvalue,Toast.LENGTH_LONG).show()

        newtokenvalue=updatetoken+tokenvalue.toInt()

        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")

        val intent= Intent(this,Complete::class.java)

//set value of token to child
        RootRef.child(phone).child("token").setValue(newtokenvalue)
            .addOnCompleteListener({
                progerssProgressDialog.dismiss()
                Toast.makeText(this,"token purchased ",Toast.LENGTH_SHORT).show()
                intent.putExtra("phone",phone)
                startActivity(intent)
            })
    }



}

