package com.text.textr01.musicplayer

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*
import kotlin.collections.HashMap

class RegisterActivity : AppCompatActivity() {

    lateinit var progerssProgressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val inputPhone:EditText
        val inputName:EditText
        val inputPassword:EditText

        progerssProgressDialog=ProgressDialog(this)

//field
        inputPhone=findViewById(R.id.phoneentry)
        inputName=findViewById(R.id.nameentry)
        inputPassword=findViewById(R.id.passwordentry)


        //check entry is empty or not
        registerNowbtn.setOnClickListener {
            val name:String=inputName.text.toString()
            val phone:String=inputPhone.text.toString()
            val password:String=inputPassword.text.toString()

            if(TextUtils.isEmpty(name))
            {
                Toast.makeText(this,"Please enter your name..",Toast.LENGTH_SHORT).show()
            }
            else if(TextUtils.isEmpty(phone))
            {
                Toast.makeText(this,"Please enter your phone..",Toast.LENGTH_SHORT).show()
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

                validatePhoneNumber(name,phone,password)
            }

        }
    }

    //if entry is not empty then validate phone number
    //that with the number user login is exist or not
    private fun validatePhoneNumber(name: String, phone: String, password: String)
    {
        //val is like final varable
        lateinit var  RootRef:DatabaseReference
        RootRef=FirebaseDatabase.getInstance().getReference("Users")

        val signup=SignUp(name,phone,password,"2","2","0")
        val intent= Intent(this,LoginActivity::class.java)

        val signupdetailsListener= object :ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                if(!p0.child(phone).exists())
                {
                    RootRef.child(phone).setValue(signup).addOnCompleteListener({
                        progerssProgressDialog.dismiss()

                        Toast.makeText(this@RegisterActivity,"Registration Successful",Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                    })
                }
                else
                {
                    progerssProgressDialog.dismiss()
                    Toast.makeText(this@RegisterActivity,"Account with this number already exists..",Toast.LENGTH_SHORT).show()
                    startActivity(intent)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }
        RootRef.addListenerForSingleValueEvent(signupdetailsListener)


    }
}
