package com.text.textr01.musicplayer

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    lateinit var progerssProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        progerssProgressDialog=ProgressDialog(this)

        val inputLoginPhone: EditText
        val inputLoginPassword: EditText

        inputLoginPhone=findViewById(R.id.loginphoneentry)
        inputLoginPassword=findViewById(R.id.loginpasswordentry)

        //error handling on click button
        loginBtn.setOnClickListener {
            val phone:String=inputLoginPhone.text.toString()
            val password:String=inputLoginPassword.text.toString()
            
            if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(this,"Please enter your phone..", Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(password))
            {
                Toast.makeText(this,"Please enter your password..",Toast.LENGTH_SHORT).show()
            }
            else
            {
                progerssProgressDialog.setTitle("Loading")
                progerssProgressDialog.setMessage("Please wait , we are checking our credentials")
                progerssProgressDialog.setCancelable(false)
                progerssProgressDialog.show()
                
                loginuser(phone,password)
            }
        }
    }

    //run after information is filled in edit text
    private fun loginuser(phone: String, password: String) {

        lateinit var  RootRef: DatabaseReference
        RootRef= FirebaseDatabase.getInstance().getReference("Users")

        val login=Login(phone,password)

        var Mypassword:String

        //check number exist or not and if not exit then allow user to login to their  acount
        val loginUserListerner=object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(p0.child(phone).exists())
                {
                    Mypassword=p0.child(phone).child("password").getValue().toString()

                    if(login.password.equals(Mypassword))
                    {
                        progerssProgressDialog.dismiss()
                        Toast.makeText(this@LoginActivity,"login successful",Toast.LENGTH_LONG).show()
                        val intent= Intent(this@LoginActivity,SelectionActivity::class.java)
                        intent.putExtra("phone",phone)
                        startActivity(intent)

                    }
                    else
                    {
                        progerssProgressDialog.dismiss()
                        Toast.makeText(this@LoginActivity,"incorrect passowrd",Toast.LENGTH_LONG).show()

                    }
                }
                else
                {
                    progerssProgressDialog.dismiss()
                    Toast.makeText(this@LoginActivity,"Account with this number not exists please create account first",Toast.LENGTH_LONG).show()
                    val intent= Intent(this@LoginActivity,RegisterActivity::class.java)
                    startActivity(intent)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }
        RootRef.addListenerForSingleValueEvent(loginUserListerner)

    }
}
