package com.text.textr01.musicplayer

import android.app.ProgressDialog
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.text.textr01.musicplayer.adapters.DataAdpter
import com.text.textr01.musicplayer.model.DataModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_player.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var progerssProgressDialog: ProgressDialog
    var dataList = ArrayList<DataModel>()
    lateinit var recyclerView: RecyclerView
    lateinit var adapter: DataAdpter
    lateinit var phone:String


    lateinit var rl: RelativeLayout
    override fun onResume() {

        //get phone number using intent

        var phone:String=intent.getStringExtra("phone")
        rl=findViewById(R.id.mainlayout)


        //database reference with user
        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")

        var themeValuenew:String="jgs"

        //listerner to check the value of the token to display it to user
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

    //check theme value to display theme
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
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recycler_view)

        phone=intent.getStringExtra("phone")


        //setting up the adapter
        recyclerView.adapter= DataAdpter(dataList,this,phone)
        recyclerView.layoutManager= LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

        progerssProgressDialog=ProgressDialog(this)
        progerssProgressDialog.setTitle("Loading")
        progerssProgressDialog.setCancelable(false)
        progerssProgressDialog.show()
        getData()



        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")
        var tokenvalue:String="l"
        var nsong:String="0"

        //check token value in database
        val tokenListener= object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                tokenvalue=p0.child("token").getValue().toString()
                tokenmain.setText("MyTokens : "+tokenvalue)
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        RootRef.child(phone).addValueEventListener(tokenListener)

        //check number of songs of each user by nsong value
        val nsonglistener= object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot)
            {
                nsong=p0.child("nsong").getValue().toString()
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        RootRef.child(phone).addValueEventListener(nsonglistener)


        //show more is unlock button and this reduce the value of token and increase the number of song
        showmore.setOnClickListener {

            if(tokenvalue.toInt() > 0 )
            {
                tokenvalue= (tokenvalue.toInt()-1).toString()
                nsong= (nsong.toInt()+1).toString()
                RootRef.child(phone).child("token").setValue(tokenvalue)
                RootRef.child(phone).child("nsong").setValue(nsong)


            }
            else
            {
                Toast.makeText(this@MainActivity,"Please purchase tokens..",Toast.LENGTH_SHORT).show()
            }


        }


    }



    //get data from api in the datamodel form
    private fun getData() {
        val call: Call<List<DataModel>> = ApiClient.getClient.getStudio()
        call.enqueue(object : Callback<List<DataModel>> {

            //response of api call
            override fun onResponse(call: Call<List<DataModel>>?, response: Response<List<DataModel>>?) {

                lateinit var  RootRef: DatabaseReference
                RootRef= FirebaseDatabase.getInstance().getReference("Users")
                var nsong:Int

                val tokenListener= object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot)
                    {
                        nsong=p0.child("nsong").getValue().toString().toInt()

                        progerssProgressDialog.dismiss()

                        dataList.clear()
                        //var n:Int
                        for(i in 0..nsong)
                        {
                            dataList.add(response!!.body()!![i])

                        }


                        recyclerView.adapter?.notifyDataSetChanged()
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                }

                RootRef.child(phone).addValueEventListener(tokenListener)





            }

            //run when we dont get response from api call
            override fun onFailure(call: Call<List<DataModel>>?, t: Throwable?) {
                progerssProgressDialog.dismiss()
                Toast.makeText(this@MainActivity,"Fail...",Toast.LENGTH_LONG).show()
            }

        })
    }

}
